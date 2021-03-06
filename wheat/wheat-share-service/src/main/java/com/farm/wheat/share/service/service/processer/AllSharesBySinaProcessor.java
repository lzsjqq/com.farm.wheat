package com.farm.wheat.share.service.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.service.constant.ShareConst;
import com.farm.wheat.share.service.constant.ShareSource;
import com.farm.wheat.share.service.dto.ShareInfoDto;
import com.farm.wheat.share.service.service.pipeline.AllSharesPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 新浪股票最新数据接口
 *
 * @Author: xyc
 * @Date: 2018/12/29 15:20
 * @Version 1.0
 */
@Service
public class AllSharesBySinaProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(AllSharesBySinaProcessor.class);

    @Autowired
    private AllSharesPipeline pipeline;
    private static String url = "http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=%s&sr_p=-1";
    //    private static String testUrl = "https://www.baidu.com/";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().addHeader("User-Agent","Mozilla/5.0 (Linux; U; Android 10; zh-CN; LYA-AL00 Build/HUAWEILYA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/12.7.6.1056 Mobile Safari/537.36").setRetryTimes(3).setSleepTime(1000);

    public void start() {
        List<Pipeline> list = new ArrayList<>();
        list.add(pipeline);
        Spider.create(new AllSharesBySinaProcessor()) // 实例化spider
                //从"https://github.com/code4craft"开始抓
                .addUrl(String.format(url, "1"))
                .thread(1)
                .setDownloader(new HttpClientDownloader())
                .setPipelines(list)
//                .setDownloader(getProxyDownload("127.0.0.1", "8118"))
                .run();  //启动爬虫
    }

    public static void main(String args[]) {
        AllSharesBySinaProcessor processor = new AllSharesBySinaProcessor();
        processor.start();
    }


    @Override
    public void process(Page page) {
        try {   // 部分二：定义如何抽取页面信息，并保存下来
            Html html = page.getHtml();
            ///div/div/div
            List<Selectable> nodes = html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/table/tbody/tr").nodes();
            List<ShareInfoDto> shareInfoDtos = new ArrayList<>();
            if (NullCheckUtils.isNotBlank(nodes)) {
                for (int i = 1, size = nodes.size(); i < size; i++) {
                    Selectable node = nodes.get(i);
                    List<Selectable> tds = node.xpath("td").nodes();
                    if (NullCheckUtils.isNotBlank(tds)) {
                        ShareInfoDto shareInfoDto = new ShareInfoDto();
                        shareInfoDtos.add(shareInfoDto);
                        // 解析
                        String shareCode = tds.get(0).xpath("a/text()").get().trim();
                        shareInfoDto.setShareCode(shareCode);
                        if (shareCode.startsWith("600") || shareCode.startsWith("601") || shareCode.startsWith("603")) {
                            shareInfoDto.setSource(ShareSource.SH.getSource());
                        } else if (shareCode.startsWith("000") || shareCode.startsWith("001")) {
                            shareInfoDto.setSource(ShareSource.SZ.getSource());
                        } else if (shareCode.startsWith("002")) {
                            shareInfoDto.setSource(ShareSource.SZ_ZXB.getSource());
                        } else if (shareCode.startsWith("3")) {
                            shareInfoDto.setSource(ShareSource.SZ_CY.getSource());
                        } else if (shareCode.startsWith("688")) {
                            shareInfoDto.setSource(ShareSource.KC.getSource());
                        } else if (shareCode.startsWith("83")||shareCode.startsWith("43")) {
                            shareInfoDto.setSource(ShareSource.XSB.getSource());
                        } else {
                            shareInfoDto.setSource(ShareSource.OTHER.getSource());
                        }
                        String shareName = tds.get(1).xpath("span/text()").get().trim();
                        if (!NullCheckUtils.isNotBlank(shareName)) {
                            System.out.println();
                        }
                        shareInfoDto.setShareName(shareName);

                        shareInfoDto.setIndustry(tds.get(8).xpath("td/text()").get());
                    }
                }
            }
            page.putField(ShareConst.SHARE_INFO_BY_TB_FIELD, JSONObject.toJSONString(shareInfoDtos));
            List<Selectable> pages = html.xpath("/html/body/div[1]/div[5]/div[2]/div/div[1]/div[3]/a/text()").nodes();
            if (NullCheckUtils.isNotBlank(pages)) {
                for (Selectable selectable : pages) {
                    String nextPage = selectable.get();
                    if (nextPage.matches("\\d+")) {
                        page.addTargetRequest(String.format(url, nextPage));
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
