package com.farm.wheat.share.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.snow.tiger.dto.share.SharePriceBaseDTO;
import com.snow.tiger.ip.proxy.util.DateUtils;
import com.snow.tiger.ip.proxy.util.NullCheckUtils;
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
public class ShareNewestByTbProcessor implements PageProcessor {

    private static final String SPLIT = "~";

    private static String url = "http://qt.gtimg.cn/q=sz000858";
    //    private static String testUrl = "https://www.baidu.com/";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);


    public static void main(String[] args) {
        List<Pipeline> list = new ArrayList<>();
        FilePipeline filePipeline = new FilePipeline();
        filePipeline.setPath("C:\\Users\\Rzxuser\\Desktop\\hosts");
//        list.add(SpringContextUtil.getBean(MysqlPipeline.class));
        list.add(filePipeline);
        Spider.create(new ShareNewestByTbProcessor()) // 实例化spider
                //从"https://github.com/code4craft"开始抓
                .addUrl(url)
                //开启5个线程抓取
                .thread(5)
                .setDownloader(new HttpClientDownloader())
                .setPipelines(list)
//                .setDownloader(getProxyDownload("127.0.0.1", "8118"))
                //启动爬虫
                .run();
    }


    @Override
    public void process(Page page) {
        try {   // 部分二：定义如何抽取页面信息，并保存下来
            Html html = page.getHtml();
            ///div/div/div
            List<Selectable> nodes = html.xpath("body/text()").nodes();
            List<SharePriceBaseDTO> list = new ArrayList<>();
            if (NullCheckUtils.isNotBlank(nodes)) {
                for (Selectable node : nodes) {
                    String sharePrices = node.get();
                    System.out.println(sharePrices);
                    if (NullCheckUtils.isNotBlank(sharePrices)) {

                        String[] prices = sharePrices.split(SPLIT);
                        for (String price : prices) {
                            System.out.println(price);
                        }
                        if (NullCheckUtils.isNotBlank(prices)) {
                            SharePriceBaseDTO sharePriceBaseDTO = new SharePriceBaseDTO();
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
                            sharePriceBaseDTO.setPERatio(new BigDecimal(prices[39]));
                            sharePriceBaseDTO.setAmplitude(new BigDecimal(prices[43]));
                            sharePriceBaseDTO.setCirculationMarketValue(new BigDecimal(prices[44]));
                            sharePriceBaseDTO.setTotalMarketValue(new BigDecimal(prices[45]));
                            sharePriceBaseDTO.setPBRatio(new BigDecimal(prices[46]));
                            list.add(sharePriceBaseDTO);
                            System.out.println(JSONObject.toJSONString(sharePriceBaseDTO));
                        }

                    }
                    page.putField("sharePrice", JSONObject.toJSONString(list));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
