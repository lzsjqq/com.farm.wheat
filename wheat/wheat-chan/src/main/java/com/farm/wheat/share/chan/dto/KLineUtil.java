package com.farm.wheat.share.chan.dto;

import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.chan.util.KLineTypeEnum;
import com.farm.wheat.share.chan.util.RunTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: K线工具类，主要处理包含关系
 * @author: xyc
 * @create: 2021-03-05 00:13
 */
public class KLineUtil {

    /**
     * 处理K线的趋势
     *
     * @param kLines
     */
    public static void priceRunType(List<KLine> kLines) {
        if (NullCheckUtils.isBlank(kLines) || kLines.size() == 1) {
            return;
        }
        int size = kLines.size();

        KLine first = kLines.get(0);
        KLine second;
        for (int index = 1; index < size; index++) {
            second = kLines.get(index);
            second.setRunType(runType(first, second));
            first = second;
        }
    }


    /**
     * 相邻两根K线的关系
     *
     * @param first
     * @param second
     * @return
     */
    private static RunTypeEnum runType(KLine first, KLine second) {
        if (NullCheckUtils.isBlank(first) || NullCheckUtils.isBlank(second)) {
            return RunTypeEnum.NONE;
        }
        double maxPrice = first.getMaxPrice() - second.getMaxPrice();
        double minPrice = first.getMinPrice() - second.getMinPrice();
        if (maxPrice > 0 && minPrice > 0 || maxPrice > 0 && minPrice >= 0 || maxPrice >= 0 && minPrice > 0) {
            return RunTypeEnum.DOWN;
        }
        if (maxPrice < 0 && minPrice < 0 || maxPrice < 0 && minPrice <= 0 || maxPrice <= 0 && minPrice < 0) {
            return RunTypeEnum.UP;
        }
        return RunTypeEnum.NONE;
    }

    /**
     * 处理包含关系包含关系
     *
     * @param kLines
     * @return false 不包含 true 包含
     */
    public static void handleContain(List<KLine> kLines) {
        if (NullCheckUtils.isBlank(kLines)) {
            return;
        }

        int size = kLines.size();
        for (int index = 0; index < size; index++) {
            KLine kLine = kLines.get(index);
            /**
             * 前一个
             */
            KLine firstKLine = getPre(kLines, index);
            if (firstKLine == null) {
                // 默认向上包含处理
                kLine.setRunType(RunTypeEnum.UP);
                continue;
            }
            double secondMaxPrice = kLine.getMaxPrice();
            double secondMinPrice = kLine.getMinPrice();
            // 判断是否是包含K线
            KLine firstContainKLine = firstKLine.getContainKLine();
            ContainedKLine containedKLine = firstKLine.getContainedKLine();
            double firstMaxPrice = firstContainKLine != null ? firstContainKLine.getMaxPrice() : firstKLine.getMaxPrice();
            double firstMinPrice = firstContainKLine != null ? firstContainKLine.getMinPrice() : firstKLine.getMinPrice();
            double max = secondMaxPrice - firstMaxPrice;
            double min = secondMinPrice - firstMinPrice;
            if ((max > 0 && min > 0) || (max > 0 && min >= 0) || (max >= 0 && min > 0)) {
                // 高高 向上
                kLine.setRunType(RunTypeEnum.UP);
                continue;
//                price.setBiSize(price.getBiSize() + 1);
            } else if ((max < 0 && min < 0) || (max < 0 && min <= 0) || (max <= 0 && min < 0)) {
                // 低低 向下
                kLine.setRunType(RunTypeEnum.DOWN);
                continue;
            }
            // 类型为包含
            RunTypeEnum priceType = firstKLine.getRunType();
            kLine.setRunType(priceType);
            // 包含类
            KLine containKLine = firstContainKLine != null ? firstContainKLine : new KLine();
            containedKLine = containedKLine != null ? containedKLine : new ContainedKLine(firstKLine.getOpenPrice(), firstMinPrice, firstMaxPrice, firstKLine.getIndex(), KLineTypeEnum.C);
            // 计算k线包含数量
            containKLine.setContainSize(getContainSize(firstContainKLine));
            if (priceType == RunTypeEnum.UP) {
                // 向上 包含
                contain(secondMaxPrice, secondMinPrice, firstMaxPrice, firstMinPrice, containKLine, max > 0, min > 0, RunTypeEnum.UP);
            } else if (priceType == RunTypeEnum.DOWN) {
                // 向下 包含
                contain(secondMaxPrice, secondMinPrice, firstMaxPrice, firstMinPrice, containKLine, max < 0, min < 0, RunTypeEnum.DOWN);
            }
            containedKLine.setEndPrice(kLine.getEndPrice());
            containedKLine.setEndIndex(kLine.getIndex());
            containedKLine.setMinPrice(containKLine.getMinPrice(), kLine.getIndex());
            containedKLine.setMaxPrice(containKLine.getMaxPrice(), kLine.getIndex());
            containedKLine.setRunType(containKLine.getRunType());
            kLine.setContainedKLine(containedKLine);
            firstKLine.setContainedKLine(containedKLine);
            kLine.setContainKLine(containKLine);
            firstKLine.setContainKLine(containKLine);
        }
    }

    /**
     * 处理包含关系
     *
     * @param kLines
     * @return false 不包含 true 包含
     */
    public static List<ContainedKLine> buildContained(List<KLine> kLines) {
        if (NullCheckUtils.isBlank(kLines)) {
            return Collections.emptyList();
        }
        List<ContainedKLine> containedKLineList = new ArrayList<>();
        int size = kLines.size();
        KLine KLine;
        ContainedKLine containedKLine;
        for (int index = 0; index < size; index++) {
            KLine = kLines.get(index);
            containedKLine = KLine.getContainedKLine();
            if (NullCheckUtils.isBlank(containedKLine)) {
                containedKLineList.add(buildContainKLine(KLine));
                continue;
            }
            ContainedKLine lastOne = getLastContainedKLine(containedKLineList);
            if (lastOne == containedKLine) {
                continue;
            }
            containedKLineList.add(containedKLine);
        }

        return containedKLineList;
    }

    private static ContainedKLine getLastContainedKLine(List<ContainedKLine> containedKLineList) {
        if (NullCheckUtils.isBlank(containedKLineList)) {
            return null;
        }
        return containedKLineList.get(containedKLineList.size() - 1);
    }

    private static ContainedKLine buildContainKLine(KLine KLine) {
        ContainedKLine containedKLine = new ContainedKLine();
        containedKLine.setStartIndex(KLine.getIndex());
        containedKLine.setEndIndex(KLine.getIndex());
        containedKLine.setMaxIndex(KLine.getIndex());
        containedKLine.setMinIndex(KLine.getIndex());
        containedKLine.setOpenPrice(KLine.getOpenPrice());
        containedKLine.setEndPrice(KLine.getEndPrice());
        containedKLine.setMinPrice(KLine.getMinPrice());
        containedKLine.setMaxPrice(KLine.getMaxPrice());
        containedKLine.setRunType(KLine.getRunType());
        containedKLine.setLineTypeEnum(KLineTypeEnum.NC);
        return containedKLine;
    }

    private static void contain(double priceMaxPrice, double priceMinPrice, double firstPriceMaxPrice, double firstPriceMinPrice, KLine containKLine, boolean b, boolean b2, RunTypeEnum up) {
        containKLine.setMaxPrice(b ? priceMaxPrice : firstPriceMaxPrice);
        containKLine.setMinPrice(b2 ? priceMinPrice : firstPriceMinPrice);
        containKLine.setRunType(up);
    }


    /**
     * 从当前确定和前面包含的K线算确定当前一共包含了几根K线
     *
     * @param firstContainKLine
     * @return
     */
    private static int getContainSize(KLine firstContainKLine) {
        return firstContainKLine == null ? 2 : firstContainKLine.getContainSize() + 1;
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