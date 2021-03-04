package com.farm.wheat.share.biz.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.NullCheckUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
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
public class ShareNewestBySinaProcessor implements PageProcessor {

    private static String url = "http://hq.sinajs.cn/list=sh601006";
    //    private static String testUrl = "https://www.baidu.com/";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);


    public static void main(String[] args) {
        List<Pipeline> list = new ArrayList<>();
        FilePipeline filePipeline = new FilePipeline();
        filePipeline.setPath("C:\\Users\\Rzxuser\\Desktop\\hosts");
//        list.add(SpringContextUtil.getBean(MysqlPipeline.class));
        list.add(filePipeline);
        Spider.create(new ShareNewestBySinaProcessor()) // 实例化spider
                //从"https://github.com/code4craft"开始抓
                .addUrl(url)
                //开启5个线程抓取
                .thread(1)
                .setDownloader(new HttpClientDownloader())
                .setPipelines(list)
//                .setDownloader(getProxyDownload("127.0.0.1", "8118"))
                //启动爬虫
                .run();
    }


    @Override
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        Html html = page.getHtml();
        ///div/div/div
        List<Selectable> nodes = html.xpath("body/text()").nodes();
        if (NullCheckUtils.isNotBlank(nodes)) {
            for (Selectable node : nodes) {
                String sharePrices = node.get();

                if (NullCheckUtils.isNotBlank(sharePrices)) {
                    String[] split = sharePrices.split(",");
                    for (String s : split) {
                        System.out.println(s);
                    }
                }
                page.putField("proxy", JSONObject.toJSONString(sharePrices));
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
