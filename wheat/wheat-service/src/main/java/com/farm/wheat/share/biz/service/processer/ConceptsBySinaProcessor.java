package com.farm.wheat.share.biz.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.service.pipeline.ConceptsPipeline;
import com.farm.wheat.share.biz.dto.ShareConceptInfoDTO;
import com.farm.wheat.share.biz.dto.ShareConceptPriceDTO;
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
import java.math.RoundingMode;
import java.util.*;

/**
 * 新浪股票最新数据接口
 *
 * @Author: xyc
 * @Date: 2018/12/29 15:20
 * @Version 1.0
 */
@Service
public class ConceptsBySinaProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(ConceptsBySinaProcessor.class);

    @Autowired
    private ConceptsPipeline pipeline;


    private static String url = "http://money.finance.sina.com.cn/q/view/newFLJK.php?param=class";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setCharset("gb2312");

    public void start() {
        logger.info("start....................");
        List<Pipeline> list = new ArrayList<>();
        FilePipeline filePipeline = new FilePipeline();
        filePipeline.setPath("C:\\Users\\Rzxuser\\Desktop\\hosts");

//        list.add(pipeline);
        list.add(pipeline);
        Spider.create(new ConceptsBySinaProcessor()) // 实例化spider
                //从"https://github.com/code4craft"开始抓
                .addUrl(String.format(url, "1"))
                .thread(1)
                .setDownloader(new HttpClientDownloader())
                .setPipelines(list)
//                .setDownloader(getProxyDownload("127.0.0.1", "8118"))
                .run();  //启动爬虫
    }

    public static void main(String args[]) {
        ConceptsBySinaProcessor processor = new ConceptsBySinaProcessor();
        processor.start();
    }


    @Override
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        Html html = page.getHtml();
        ///div/div/div
        List<Selectable> nodes = html.xpath("body/text()").nodes();
        if (NullCheckUtils.isNotBlank(nodes)) {
            List<ShareConceptInfoDTO> conceptInfoList = new ArrayList<>();
            List<ShareConceptPriceDTO> conceptPriceList = new ArrayList<>();
            for (int i = 0, size = nodes.size(); i < size; i++) {
                String json = nodes.get(i).get();
                json = json.substring(json.indexOf("{"));
                Map<String, String> params = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {
                });
                if (NullCheckUtils.isNotBlank(params)) {
                    Set<Map.Entry<String, String>> entries = params.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        ShareConceptInfoDTO conceptInfo = new ShareConceptInfoDTO();
                        ShareConceptPriceDTO conceptPrice = new ShareConceptPriceDTO();
                        conceptPriceList.add(conceptPrice);
                        conceptInfoList.add(conceptInfo);
                        conceptInfo.setSimpleName(key.replaceAll("gn_", ""));
                        conceptPrice.setSimpleName(conceptInfo.getSimpleName());
                        String[] split = value.split(",");
                                     if (NullCheckUtils.isNotBlank(split)) {
                            conceptInfo.setConceptName(split[1]);
                            conceptInfo.setNumber(Integer.valueOf(split[2]));
                            conceptPrice.setAvgPrice(new BigDecimal(split[3]).setScale(2, RoundingMode.HALF_UP));
                            conceptPrice.setChange(new BigDecimal(split[4]).setScale(2, RoundingMode.HALF_UP));
                            conceptPrice.setPriceChangeRatio(new BigDecimal(split[5].replace("%", "")).setScale(2, RoundingMode.HALF_UP));
                            conceptPrice.setTradingVolume(new BigDecimal(split[6]).divide(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP));
                            conceptPrice.setTradingMoney(new BigDecimal(split[7]).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
                            conceptPrice.setLedShareCode(split[8].replaceAll("sh","").replaceAll("sz",""));
                            Date date = null;
                            try {
                                date = DateUtils.dateToDate(new Date(), DateUtils.YYYY_MM_DD);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            conceptPrice.setTradingDate(date);
                        }
                    }
                }
            }
            System.out.println(JSONObject.toJSONString(conceptInfoList));
            System.out.println(JSONObject.toJSONString(conceptPriceList));
            page.putField(ShareConst.CONCEPT_INFO_LIST, JSONObject.toJSONString(conceptInfoList));
            page.putField(ShareConst.CONCEPT_PRICE_LIST, JSONObject.toJSONString(conceptPriceList));

        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
