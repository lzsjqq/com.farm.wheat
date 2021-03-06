package test;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.DateUtils;
import com.farm.wheat.share.chan.dto.ContainedKLine;
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

    String xx = "[{\"amplitude\":2.57,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159922,\"priceChange\":-0.01,\"priceChangeRatio\":-0.26,\"quarter\":1,\"todayEndPrice\":3.88,\"todayMaxPrice\":3.91,\"todayMinPrice\":3.81,\"todayOpenPrice\":3.89,\"tradingDate\":1546358400000,\"tradingMoney\":3805.00,\"tradingVolume\":98226,\"turnoverRate\":0.36,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":1.80,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159921,\"priceChange\":-0.04,\"priceChangeRatio\":-1.03,\"quarter\":1,\"todayEndPrice\":3.84,\"todayMaxPrice\":3.90,\"todayMinPrice\":3.83,\"todayOpenPrice\":3.87,\"tradingDate\":1546444800000,\"tradingMoney\":4361.00,\"tradingVolume\":113078,\"turnoverRate\":0.41,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":2.86,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159920,\"priceChange\":0.07,\"priceChangeRatio\":1.82,\"quarter\":1,\"todayEndPrice\":3.91,\"todayMaxPrice\":3.91,\"todayMinPrice\":3.80,\"todayOpenPrice\":3.80,\"tradingDate\":1546531200000,\"tradingMoney\":7308.00,\"tradingVolume\":188832,\"turnoverRate\":0.69,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":5.88,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159919,\"priceChange\":0.16,\"priceChangeRatio\":4.09,\"quarter\":1,\"todayEndPrice\":4.07,\"todayMaxPrice\":4.15,\"todayMinPrice\":3.92,\"todayOpenPrice\":3.95,\"tradingDate\":1546790400000,\"tradingMoney\":17637.00,\"tradingVolume\":433498,\"turnoverRate\":1.58,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":5.41,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159918,\"priceChange\":0.07,\"priceChangeRatio\":1.72,\"quarter\":1,\"todayEndPrice\":4.14,\"todayMaxPrice\":4.25,\"todayMinPrice\":4.03,\"todayOpenPrice\":4.07,\"tradingDate\":1546876800000,\"tradingMoney\":20030.00,\"tradingVolume\":481873,\"turnoverRate\":1.76,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":2.42,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159917,\"priceChange\":-0.05,\"priceChangeRatio\":-1.21,\"quarter\":1,\"todayEndPrice\":4.09,\"todayMaxPrice\":4.16,\"todayMinPrice\":4.06,\"todayOpenPrice\":4.12,\"tradingDate\":1546963200000,\"tradingMoney\":14714.00,\"tradingVolume\":356877,\"turnoverRate\":1.30,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":2.20,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159916,\"priceChange\":-0.08,\"priceChangeRatio\":-1.96,\"quarter\":1,\"todayEndPrice\":4.01,\"todayMaxPrice\":4.09,\"todayMinPrice\":4.00,\"todayOpenPrice\":4.08,\"tradingDate\":1547049600000,\"tradingMoney\":8240.00,\"tradingVolume\":203778,\"turnoverRate\":0.74,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":2.00,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159915,\"priceChange\":0.05,\"priceChangeRatio\":1.25,\"quarter\":1,\"todayEndPrice\":4.06,\"todayMaxPrice\":4.06,\"todayMinPrice\":3.98,\"todayOpenPrice\":4.01,\"tradingDate\":1547136000000,\"tradingMoney\":9525.00,\"tradingVolume\":237609,\"turnoverRate\":0.87,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":2.22,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159914,\"priceChange\":-0.05,\"priceChangeRatio\":-1.23,\"quarter\":1,\"todayEndPrice\":4.01,\"todayMaxPrice\":4.09,\"todayMinPrice\":4.00,\"todayOpenPrice\":4.08,\"tradingDate\":1547395200000,\"tradingMoney\":6773.00,\"tradingVolume\":168114,\"turnoverRate\":0.61,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019},{\"amplitude\":1.75,\"createBy\":\"WY\",\"createTime\":1586062314000,\"idShareInfo\":39746,\"idSharePrice\":1159913,\"priceChange\":0.03,\"priceChangeRatio\":0.75,\"quarter\":1,\"todayEndPrice\":4.04,\"todayMaxPrice\":4.05,\"todayMinPrice\":3.98,\"todayOpenPrice\":4.01,\"tradingDate\":1547481600000,\"tradingMoney\":6744.00,\"tradingVolume\":167665,\"turnoverRate\":0.61,\"updateBy\":\"WY\",\"updateTime\":1586062314000,\"year\":2019}]";
    @Test
    public void xx() throws Exception {
        List<SharePriceDto> sharePrices = JSONObject.parseArray(xx, SharePriceDto.class);
        List<KLine> KLines = convertToKLine(sharePrices);
//        List<KLine> linked = ChanLunUtil.buildLined(list);

        // 处理包含关系包含关系
        KLineUtil.handleContain(KLines);
        // 获取包含处理后的K线
        List<ContainedKLine> containedKLineList = KLineUtil.buildContained(KLines);
        System.out.println(JSONObject.toJSONString(containedKLineList));
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