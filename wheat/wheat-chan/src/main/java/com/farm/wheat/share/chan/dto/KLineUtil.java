package com.farm.wheat.share.chan.dto;

import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.chan.util.BiSequence;
import com.farm.wheat.share.chan.util.PriceRunTypeEnum;

import java.util.List;

/**
 * @description: K线工具类，主要处理包含关系
 * @author: xyc
 * @create: 2021-03-05 00:13
 */
public class KLineUtil {


    /**
     * 处理包含关系包含关系
     *
     * @param KLines
     * @return false 不包含 true 包含
     */
    public static void handleContain(List<KLine> KLines) {
        int size = KLines.size();
        for (int index = 0; index < size; index++) {
            KLine KLine = KLines.get(index);
            /**
             * 前一个
             */
            KLine preKLine = getPre(KLines, index);
            if (preKLine == null) {
                KLine.setPriceRunType(PriceRunTypeEnum.NONE);
                continue;
            }
            double priceMaxPrice = KLine.getTodayMaxPrice();
            double priceMinPrice = KLine.getTodayMinPrice();
            // 判断是否是包含K线
            KLine preContainKLine = preKLine.getContainKLine();
            double prePriceMaxPrice = preContainKLine != null ? preContainKLine.getTodayMaxPrice() : preKLine.getTodayMaxPrice();
            double prePriceMinPrice = preContainKLine != null ? preContainKLine.getTodayMinPrice() : preKLine.getTodayMinPrice();
            double max = priceMaxPrice - prePriceMaxPrice;
            double min = priceMinPrice - prePriceMinPrice;
            if (max > 0 && min > 0) {
                // 高高 向上
                KLine.setPriceRunType(PriceRunTypeEnum.UP);
//                price.setBiSize(price.getBiSize() + 1);
            } else if (max < 0 && min < 0) {
                // 低低 向下
                KLine.setPriceRunType(PriceRunTypeEnum.DOWN);
//                price.setBiSize(price.getBiSize() + 1);
            } else {
                // 类型为包含
                PriceRunTypeEnum priceType = preKLine.getPriceRunType();
                KLine.setPriceRunType(priceType);
                // 包含类
                KLine containKLine = preContainKLine != null ? preContainKLine : new KLine();
                // 计算k线包含数量
                containKLine.setContainSize(getContainSize(preContainKLine));
                if (priceType == PriceRunTypeEnum.UP) {
                    // 向上 包含
                    contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containKLine, max > 0, min > 0, PriceRunTypeEnum.UP);
                } else if (priceType == PriceRunTypeEnum.DOWN) {
                    // 向下 包含
                    contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containKLine, max < 0, min < 0, PriceRunTypeEnum.DOWN);
                }
                KLine.setContainKLine(containKLine);
                preKLine.setContainKLine(containKLine);
            }
        }
    }
    private static void contain(double priceMaxPrice, double priceMinPrice, double prePriceMaxPrice, double prePriceMinPrice, KLine containKLine, boolean b, boolean b2, PriceRunTypeEnum up) {
        containKLine.setTodayMaxPrice(b ? priceMaxPrice : prePriceMaxPrice);
        containKLine.setTodayMinPrice(b2 ? priceMinPrice : prePriceMinPrice);
        containKLine.setPriceRunType(up);
    }


    /**
     * 从当前确定和前面包含的K线算确定当前一共包含了几根K线
     *
     * @param preContainKLine
     * @return
     */
    private static int getContainSize(KLine preContainKLine) {
        return preContainKLine == null ? 2 : preContainKLine.getContainSize() + 1;
    }




    /**
     * 获取i 的前一个值
     *
     * @param KLines
     * @param i
     * @return
     */
    private static KLine getPre(List<KLine> KLines, int i) {
        return i > 0 ? KLines.get(i - 1) : null;
    }
}