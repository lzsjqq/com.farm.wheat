package com.farm.wheat.share.biz.service.processer;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.dto.SharePriceBaseDTO;
import com.farm.wheat.share.biz.mapper.simple.ShareInfoMapper;
import com.farm.wheat.share.biz.service.pipeline.SharePricePipeline;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新浪股票最新数据接口
 *
 * @Author: xyc
 * @Date: 2018/12/29 15:20
 * @Version 1.0
 */
@Service
public class ShareHistoryDataByWYProcessor implements PageProcessor {
    private Logger logger = LoggerFactory.getLogger(ShareHistoryDataByWYProcessor.class);

    private static String url = "http://quotes.money.163.com/trade/lsjysj_%s.html?year=%s&season=%d";

    @Resource
    private ShareInfoMapper shareInfoMapper;

    private static final String SPLIT = "~";
    @Autowired
    private SharePricePipeline pipeline;

    //    private static String testUrl = "https://www.baidu.com/";
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    public void start() {
        List<Pipeline> list = new ArrayList<>();
        list.add(pipeline);
        //
        Date date = new Date();
        int year = DateUtils.getYear(date);
        int quarter = DateUtils.getQuarter(date);
        List<ShareInfoDto> shareInfos = shareInfoMapper.selectAll();

        if (NullCheckUtils.isNotBlank(shareInfos)) {
            for (ShareInfoDto shareInfo : shareInfos) {
                String shareCode = shareInfo.getShareCode();
                int q = 1;
                while (q <= quarter) {
                    Spider.create(new ShareHistoryDataByWYProcessor()) // 实例化spider
                            .addUrl(String.format(url, shareCode, year, q))
                            .thread(1)
                            .setDownloader(new HttpClientDownloader())
                            .setPipelines(list)
                            .run();  //启动爬虫
                    q++;
                }

            }
        }

    }

    @Override
    public void process(Page page) {
        try {
            Html html = page.getHtml();
            ///div/div/div
            List<Selectable> nodes = html.xpath("/html/body/div[2]/div[4]/table/tbody/tr").nodes();
            List<SharePriceBaseDTO> sharePriceBaseDTOList = new ArrayList<>();

            String code = html.xpath("/html/body/div[2]/h1/span/text()").get();
            if (NullCheckUtils.isBlank(code)) {
                return;
            }
            code = code.replaceAll("\\D+", "");

            if (NullCheckUtils.isNotBlank(nodes)) {
                for (Selectable node : nodes) {
                    List<Selectable> tds = node.xpath("td/text()").nodes();
                    if (NullCheckUtils.isNotBlank(tds)) {
                        int index = 0;
                        SharePriceBaseDTO sharePriceBaseDTO = new SharePriceBaseDTO();
                        Date date = DateUtils.stringToDate(tds.get(index++).get(), DateUtils.YYYY_MM_DD);
                        sharePriceBaseDTO.setTradingDate(date);
                        sharePriceBaseDTO.setShareCode(code);
                        sharePriceBaseDTO.setYear(DateUtils.getYear(date));
                        sharePriceBaseDTO.setQuarter(DateUtils.getQuarter(date));
                        sharePriceBaseDTO.setTodayOpenPrice(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setTodayMaxPrice(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setTodayMinPrice(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setTodayEndPrice(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setPriceChange(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setPriceChangeRatio(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setTradingVolume(Integer.valueOf(tds.get(index++).get().replaceAll(",","")));
                        sharePriceBaseDTO.setTradingMoney(new BigDecimal(tds.get(index++).get().replaceAll(",","")));
                        sharePriceBaseDTO.setAmplitude(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setTurnoverRate(new BigDecimal(tds.get(index++).get()));
                        sharePriceBaseDTO.setCreateBy(ShareConst.SOURCE_WY);
                        sharePriceBaseDTO.setUpdateBy(ShareConst.SOURCE_WY);
                        sharePriceBaseDTOList.add(sharePriceBaseDTO);
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
