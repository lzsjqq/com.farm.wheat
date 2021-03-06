package test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.farm.common.utils.DateUtils;
import com.farm.wheat.share.chan.dto.ContainedKLine;
import com.farm.wheat.share.chan.dto.FenXing;
import com.farm.wheat.share.chan.dto.FenXingUtil;
import com.farm.wheat.share.chan.dto.KLineUtil;
import com.farm.wheat.share.chan.util.ChanLunUtil;
import com.farm.wheat.share.chan.dto.KLine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: xyc
 * @create: 2020-03-13 22:28
 */
public class Testxx {


    @Test
    public void xx() throws Exception {

        String line = FileUtil.readFileAll("C:\\Users\\41328\\Desktop\\xx.txt");
        List<SharePriceDto> sharePrices = JSONObject.parseArray(line, SharePriceDto.class);
        List<KLine> KLines = convertToKLine(sharePrices);
//        List<KLine> linked = ChanLunUtil.buildLined(list);

        // 处理包含关系包含关系
        KLineUtil.handleContain(KLines);
        // 获取包含处理后的K线
        List<ContainedKLine> containedKLineList = KLineUtil.buildContained(KLines);
        List<FenXing> fenXingList = FenXingUtil.fenXingSimpleHandle(containedKLineList);
        System.out.println(JSONObject.toJSONString(fenXingList, SerializerFeature.DisableCircularReferenceDetect));
        // 处理包含关系
//        int size = linked.size();
//        for (int i = 0; i < size; i++) {
//            System.out.println(linked.get(i));
//        }
    }
    private static List<KLine> convertToKLine(List<SharePriceDto> sharePrices) throws Exception {
        List<KLine> list = new ArrayList<>();
        KLine kLine;
        SharePriceDto sharePrice;
        for (int index = 0; index < sharePrices.size(); index++) {
            sharePrice = sharePrices.get(index);
            kLine = new KLine();
            kLine.setIndex(index);
            kLine.setMinPrice(sharePrice.getTodayMinPrice().doubleValue());
            kLine.setMaxPrice(sharePrice.getTodayMaxPrice().doubleValue());
            kLine.setEndPrice(sharePrice.getTodayEndPrice().doubleValue());
            kLine.setOpenPrice(sharePrice.getTodayOpenPrice().doubleValue());
            kLine.setTradingDate(DateUtils.dateToString(sharePrice.getTradingDate(), DateUtils.YYYY_MM_DD));
            list.add(kLine);
        }
        return list;
    }
}