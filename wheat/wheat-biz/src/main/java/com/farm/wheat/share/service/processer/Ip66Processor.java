package com.farm.wheat.share.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.snow.tiger.ip.proxy.bean.FreeProxyBean;
import com.snow.tiger.ip.proxy.download.PhantomJSDownloader;
import com.snow.tiger.ip.proxy.download.PhantomJSDownloaderSetting;
import com.snow.tiger.ip.proxy.util.CrtProxyBeanUtil;
import com.snow.tiger.ip.proxy.util.SpringContextUtil;
import com.snow.tiger.pipeline.MysqlPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xyc
 * @Date: 2018/12/29 15:20
 * @Version 1.0
 */
public class Ip66Processor implements PageProcessor {

    private static String url = "http://www.66ip.cn/areaindex_33/1.html";
    private static String testUrl = "https://twitter.com/";
    //    private static String testUrl = "https://www.baidu.com/";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);


    public static void main(String[] args) {
        List<Pipeline> list = new ArrayList<>();
        FilePipeline filePipeline = new FilePipeline();
        filePipeline.setPath("C:\\Users\\Rzxuser\\Desktop\\hosts");
        list.add(SpringContextUtil.getBean(MysqlPipeline.class));
        list.add(filePipeline);
        Spider.create(new Ip66Processor()) // 实例化spider
                //从"https://github.com/code4craft"开始抓
                .addUrl(url)
                //开启5个线程抓取
                .thread(5)
                .setDownloader(new PhantomJSDownloader(new PhantomJSDownloaderSetting()))
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
        List<Selectable> hosts = html.xpath("//div[@id='main']/div/div/div/div/div/table/tbody/tr/td[1]/text()").nodes();
        List<Selectable> ports = html.xpath("//div[@id='main']/div/div/div/div/div/table/tbody/tr/td[2]/text()").nodes();
        List<Selectable> anonymitys = html.xpath("//div[@id='main']/div/div/div/div/div/table/tbody/tr/td[4]/text()").nodes();
//    List<Selectable> ports = page.getHtml().xpath("//table/tbody/tr/td[3]/text()").nodes();
        List<FreeProxyBean> proxyBean = CrtProxyBeanUtil.getProxyBean(page, hosts, ports, anonymitys);
        page.putField("proxy", JSONObject.toJSONString(proxyBean));
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
//        // 部分三： 从页面发现后续的url地址来抓取
        Selectable links = html.links();
        page.addTargetRequests(links.regex("/areaindex_\\d+/\\d+.html").all());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
