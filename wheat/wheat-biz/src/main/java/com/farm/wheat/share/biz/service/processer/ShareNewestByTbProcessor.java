package com.farm.wheat.share.biz.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.constant.ShareSource;
import com.farm.wheat.share.biz.dto.SharePriceBaseDTO;
import com.farm.wheat.share.biz.service.pipeline.ShareNewestByTbPipeline;
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

import java.math.BigDecimal;
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
public class ShareNewestByTbProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(ShareNewestByTbProcessor.class);

    private static final String SPLIT = "~";
    @Autowired
    private ShareNewestByTbPipeline pipeline;
    private static String url = "http://qt.gtimg.cn/q=";
    //    private static String testUrl = "https://www.baidu.com/";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    public void start(String code) {
        List<Pipeline> list = new ArrayList<>();
        FilePipeline filePipeline = new FilePipeline();
        filePipeline.setPath("C:\\Users\\Rzxuser\\Desktop\\hosts");
        list.add(pipeline);
        Spider.create(new ShareNewestByTbProcessor()) // 实例化spider
                //从"https://github.com/code4craft"开始抓
                .addUrl(url + code)
                .thread(1)
                .setDownloader(new HttpClientDownloader())
                .setPipelines(list)
//                .setDownloader(getProxyDownload("127.0.0.1", "8118"))
                .run();  //启动爬虫
    }


    @Override
    public void process(Page page) {
        try {   // 部分二：定义如何抽取页面信息，并保存下来
            Html html = page.getHtml();
            ///div/div/div
            List<Selectable> nodes = html.xpath("body/text()").nodes();
            List<SharePriceBaseDTO> sharePriceBaseDTOList = new ArrayList<>();
            if (NullCheckUtils.isNotBlank(nodes)) {
                for (Selectable node : nodes) {
                    String sharePrices = node.get();
                    System.out.println(sharePrices);
                    if (NullCheckUtils.isNotBlank(sharePrices)) {
                        String[] prices = sharePrices.split(SPLIT);
                        if (NullCheckUtils.isNotBlank(prices)) {
                            SharePriceBaseDTO sharePriceBaseDTO = new SharePriceBaseDTO();
                            sharePriceBaseDTO.setSource(ShareSource.getSource(prices[0]));
                            sharePriceBaseDTO.setShareName(prices[1].replaceAll(" ", ""));
                            sharePriceBaseDTO.setShareCode(prices[2]);
                            sharePriceBaseDTO.setTodayPrice(new BigDecimal(prices[3]));
                            sharePriceBaseDTO.setYesterdayEndPrice(new BigDecimal(prices[4]));
                            sharePriceBaseDTO.setTodayOpenPrice(new BigDecimal(prices[5]));
                            sharePriceBaseDTO.setTheOuter(Integer.valueOf(prices[7]));
                            sharePriceBaseDTO.setTheInner(Integer.valueOf(prices[8]));
                            sharePriceBaseDTO.setTradingDate(DateUtils.stringToDate(prices[30].substring(0, 8), DateUtils.YYYYMMDD));
                            sharePriceBaseDTO.setPriceChange(new BigDecimal(prices[31]));
                            sharePriceBaseDTO.setPriceChangeRatio(new BigDecimal(prices[32]));
                            sharePriceBaseDTO.setTodayMaxPrice(new BigDecimal(prices[33]));
                            sharePriceBaseDTO.setTodayMinPrice(new BigDecimal(prices[34]));
                            sharePriceBaseDTO.setTradingVolume(Integer.valueOf(prices[36]));
                            sharePriceBaseDTO.setTradingMoney(new BigDecimal(prices[37]));
                            sharePriceBaseDTO.setTurnoverRate(new BigDecimal(prices[38]));
                            sharePriceBaseDTO.setPeRatio(new BigDecimal(prices[39]));
                            sharePriceBaseDTO.setAmplitude(new BigDecimal(prices[43]));
                            sharePriceBaseDTO.setCirculationMarketValue(new BigDecimal(prices[44]));
                            sharePriceBaseDTO.setTotalMarketValue(new BigDecimal(prices[45]));
                            sharePriceBaseDTO.setPbRatio(new BigDecimal(prices[46]));

                            sharePriceBaseDTOList.add(sharePriceBaseDTO);
                        }

                    }
                    page.putField(ShareConst.SHARE_NEWEST_BY_TB_FIELD, JSONObject.toJSONString(sharePriceBaseDTOList));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
