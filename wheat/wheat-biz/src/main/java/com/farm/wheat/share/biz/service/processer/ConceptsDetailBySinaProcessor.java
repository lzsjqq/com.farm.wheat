package com.farm.wheat.share.biz.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.NullCheckUtils;
import com.farm.common.utils.PatternUtil;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.dto.ConceptsShareSinaDTO;
import com.farm.wheat.share.biz.dto.ShareConceptDetailDTO;
import com.farm.wheat.share.biz.dto.ShareConceptInfoDTO;
import com.farm.wheat.share.biz.mapper.simple.ShareConceptDetailMapper;
import com.farm.wheat.share.biz.mapper.simple.ShareConceptInfoMapper;
import com.farm.wheat.share.biz.service.pipeline.ConceptsDetailSinaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import javax.annotation.Resource;
import java.util.*;

/**
 * 新浪股票最新数据接口
 *
 * @Author: xyc
 * @Date: 2018/12/29 15:20
 * @Version 1.0
 */
@Service
public class ConceptsDetailBySinaProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(ConceptsDetailBySinaProcessor.class);

    @Autowired
    private ConceptsDetailSinaPipeline pipeline;

    @Resource
    private ShareConceptInfoMapper shareConceptInfoMapper;


    private static String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?sort=symbol&asc=1&node=gn_%s&symbol=&_s_r_a=init";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("gb2312");

    public void start() {
        logger.info("...............start...............");
        List<ShareConceptInfoDTO> result = shareConceptInfoMapper.selectAll();
        if (NullCheckUtils.isNotBlank(result)) {
            for (ShareConceptInfoDTO shareConceptInfoDTO : result) {
                List<Pipeline> list = new ArrayList<>();
                FilePipeline filePipeline = new FilePipeline();
                filePipeline.setPath("C:\\Users\\Rzxuser\\Desktop\\hosts");
//            list.add(pipeline);
                list.add(pipeline);
                Spider.create(new ConceptsDetailBySinaProcessor()) // 实例化spider
                        .addUrl(String.format(url, shareConceptInfoDTO.getSimpleName()))
                        .thread(1)
                        .setDownloader(new HttpClientDownloader())
                        .setPipelines(list)
//                      .setDownloader(getProxyDownload("127.0.0.1", "8118"))
                        .run();  //启动爬虫
            }
        }
    }

    public static void main(String args[]) {
        ConceptsDetailBySinaProcessor shareBySinaProcessor = new ConceptsDetailBySinaProcessor();
        shareBySinaProcessor.start();
    }

    @Override
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        Html html = page.getHtml();
        String url = page.getRequest().getUrl();

        ///div/div/div
        List<Selectable> nodes = html.xpath("body/text()").nodes();
        if (NullCheckUtils.isNotBlank(nodes)) {
            List<ShareConceptDetailDTO> conceptDetailList = new ArrayList<>();
            for (Selectable node : nodes) {
                String arr = node.get();
                if (NullCheckUtils.isNotBlank(arr)) {
                    List<ConceptsShareSinaDTO> list = JSONObject.parseArray(arr, ConceptsShareSinaDTO.class);
                    for (ConceptsShareSinaDTO conceptsShareSina : list) {
                        ShareConceptDetailDTO shareConceptDetailDTO = new ShareConceptDetailDTO();
                        conceptDetailList.add(shareConceptDetailDTO);
                        shareConceptDetailDTO.setSimpleName(PatternUtil.text(url, "node=gn_(.+?)&"));
                        shareConceptDetailDTO.setShareCode(conceptsShareSina.getCode());
                        shareConceptDetailDTO.setShareName(conceptsShareSina.getName().replaceAll(" ",""));
                    }
                }
            }
            System.out.println(JSONObject.toJSONString(conceptDetailList));
            page.putField(ShareConst.CONCEPT_DETAIL_LIST, JSONObject.toJSONString(conceptDetailList));

        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
