package com.farm.wheat.share.chan.dto;

import com.farm.common.utils.NullCheckUtils;
import com.farm.common.utils.Pair;
import com.farm.wheat.share.chan.util.FengXingTypeEnum;
import com.farm.wheat.share.chan.util.RunTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description: 分型处理
 * @author: xyc
 * @create: 2021-03-05 00:14
 */
public class FenXingUtil {

    /**
     * 得到顶底分型
     *
     * @param KLines
     * @return
     */
    public static List<KLine> topBottomType(List<KLine> KLines) {
        List<KLine> pairs = new ArrayList<>();
        int size;
        if (NullCheckUtils.isBlank(KLines) || (size = KLines.size()) < 3) {
            return pairs;
        }
        for (int index = 2; index < size; index++) {
            KLine KLine = KLines.get(index);
            RunTypeEnum priceType = KLine.getRunType();
            KLine containKLine = KLine.getContainKLine();
            // 判断是否是包含K线
            if (containKLine != null && containKLine == KLines.get(index - 1).getContainKLine()) {
                continue;
            }
            Pair<Integer, Integer> nodePair = twoPrePrice(KLines, index);
            // 和前两根进行比较
            Integer firstPre = nodePair.getFirst();
            Integer secondPre = nodePair.getSecond();
            if (null == secondPre || null == firstPre || priceType == KLines.get(firstPre).getRunType()) {
                // - 表示空点
                continue;
            }
            KLine firstPreKLine = KLines.get(firstPre);
            // 判断当前节点是否是包含K线或和上个方向一致
            if (priceType == firstPreKLine.getRunType()) {
                continue;
            }
            // 至此方向不在一致，确认分型
            KLine firstPreContainKLine = firstPreKLine.getContainKLine();
            KLine priceContainKLine = KLine.getContainKLine();
            double firstPrePriceTodayMaxPrice = firstPreContainKLine != null ? firstPreContainKLine.getMaxPrice() : firstPreKLine.getMaxPrice();
            double priceTodayMaxPrice = priceContainKLine != null ? priceContainKLine.getMaxPrice() : KLine.getMaxPrice();
            if (firstPrePriceTodayMaxPrice > priceTodayMaxPrice) {
                KLine topKLine = topPrice(KLines, firstPre, index);
                topKLine.setKType(FengXingTypeEnum.TOP);
                pairs.add(topKLine);// 添加新元素
            } else {
                KLine bottomKLine = bottomPrice(KLines, firstPre, index);
                bottomKLine.setKType(FengXingTypeEnum.BOTTOM);
                pairs.add(bottomKLine);
            }
        }
        return pairs;
    }

    /**
     * 顶底分型简单处理
     *
     * @param containedKLineList containedKLineList
     * @return List<FenXing>
     */
    public static List<FenXing> fenXingSimpleHandle(List<ContainedKLine> containedKLineList) {
        int size = containedKLineList.size();
        if (NullCheckUtils.isBlank(containedKLineList) || size <= 2) {
            return Collections.emptyList();
        }
        List<FenXing> fenXingList = new ArrayList<>();
        //第一根K线
        ContainedKLine startLine = containedKLineList.get(0);
        // 第二根K线
        ContainedKLine middleLine = containedKLineList.get(1);
        // 第三根K线
        ContainedKLine endLine;

        for (int index = 2; index < containedKLineList.size(); index++) {
            endLine = containedKLineList.get(index);
            FengXingTypeEnum kType = checkFengXing(startLine, middleLine, endLine);
            if (FengXingTypeEnum.NONE != kType) {
                FenXing fenXing = new FenXing();
                fenXing.setStartLine(startLine);
                fenXing.setMiddleLine(middleLine);
                fenXing.setEndLine(endLine);
                fenXing.setMaxPrice(fengXingMaxPrice(startLine, middleLine, endLine));
                fenXing.setMinPrice(fengXingMinPrice(startLine, middleLine, endLine));
                fenXing.setFengXingTypeEnum(kType);
                fenXingList.add(fenXing);
            }
            startLine = middleLine;
            middleLine = endLine;
        }
        return fenXingList;
    }
    
    /**
     * 顶分型定义：不含包含关系的3根K线，中间一根的高点最高，低点也最高；
     * 底分型定义：不含包含关系的3根K线，中间一根的低点最低，高点也最低；
     *
     * @param startLine  startLine
     * @param middleLine middleLine
     * @param endLine    endLine
     * @return KTypeEnum
     */
    private static FengXingTypeEnum checkFengXing(ContainedKLine startLine, ContainedKLine middleLine, ContainedKLine endLine) {
        int middleStartMax = Double.compare(middleLine.getMaxPrice(), startLine.getMaxPrice());
        int middleEndMax = Double.compare(middleLine.getMaxPrice(), endLine.getMaxPrice());
        int middleStartMin = Double.compare(middleLine.getMinPrice(), startLine.getMinPrice());
        int middleEndMin = Double.compare(middleLine.getMinPrice(), endLine.getMinPrice());

        if (middleStartMax >= 0 && middleEndMax >= 0 && middleStartMin >= 0 && middleEndMin >= 0) {
            return FengXingTypeEnum.TOP;
        }
        if (middleStartMax <= 0 && middleEndMax <= 0 && middleStartMin <= 0 && middleEndMin <= 0) {
            return FengXingTypeEnum.BOTTOM;
        }
        return FengXingTypeEnum.NONE;
    }

    /**
     * 顶分型定义：不含包含关系的3根K线，中间一根的高点最高，低点也最高；
     * 底分型定义：不含包含关系的3根K线，中间一根的低点最低，高点也最低；
     *
     * @param startLine  startLine
     * @param middleLine middleLine
     * @param endLine    endLine
     * @return KTypeEnum
     */
    private static double fengXingMaxPrice(ContainedKLine startLine, ContainedKLine middleLine, ContainedKLine endLine) {
        return Math.max(Math.max(startLine.getMaxPrice(), middleLine.getMaxPrice()), endLine.getMaxPrice());
    }

    /**
     * 顶分型定义：不含包含关系的3根K线，中间一根的高点最高，低点也最高；
     * 底分型定义：不含包含关系的3根K线，中间一根的低点最低，高点也最低；
     *
     * @param startLine  startLine
     * @param middleLine middleLine
     * @param endLine    endLine
     * @return KTypeEnum
     */
    private static double fengXingMinPrice(ContainedKLine startLine, ContainedKLine middleLine, ContainedKLine endLine) {
        return Math.min(Math.max(startLine.getMinPrice(), middleLine.getMinPrice()), endLine.getMinPrice());
    }


    private static KLine topPrice(List<KLine> KLines, int firstPre, int index) {
        KLine max = KLines.get(firstPre);
        KLine KLine;
        for (int i = firstPre + 1; i < index; i++) {
            KLine = KLines.get(i);
            if (KLine.getMaxPrice() > max.getMaxPrice()) {
                max = KLine;
            }
        }
        return max;
    }

    private static KLine bottomPrice(List<KLine> KLines, int firstPre, int index) {
        KLine min = KLines.get(firstPre);
        KLine KLine;
        for (int i = firstPre + 1; i < index; i++) {
            KLine = KLines.get(i);
            if (KLine.getMinPrice() < min.getMinPrice()) {
                min = KLine;
            }
        }
        return min;
    }

    /**
     * 获取处理完包含关系的前两根K线
     *
     * @param KLines
     * @param index
     * @return
     */
    private static Pair<Integer, Integer> twoPrePrice(List<KLine> KLines, int index) {
        Pair<Integer, Integer> pair = new Pair<>(null, null);
        // 判断前一个是否是包含节点,如果是包含节点获取到包含节点的第一根K线
        int pre = index - 1;
        KLine preKLine = KLines.get(pre);
        if (checkIsContain(preKLine)) {
            pre = pre - preKLine.getContainKLine().getContainSize() + 1;
        }
        // pre == 0 说明此时只有两个节点
        if (pre == 0) {
            return pair;
        }
        // 获取第二个节点
        int secondPre = pre - 1;
        KLine secondKLine = KLines.get(secondPre);
        if (checkIsContain(secondKLine)) {
            secondPre = secondPre - secondKLine.getContainKLine().getContainSize() + 1;
        }
        pair.setFirst(pre);
        pair.setSecond(secondPre);
        return pair;
    }

    /**
     * 判断是否是包含K线
     *
     * @param KLine
     * @return
     */
    private static boolean checkIsContain(KLine KLine) {
        return KLine.getContainKLine() != null;
    }
}