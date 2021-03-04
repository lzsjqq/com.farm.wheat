package com.farm.wheat.share.chan.dto;

import com.farm.common.utils.NullCheckUtils;
import com.farm.common.utils.Pair;
import com.farm.wheat.share.chan.util.KTypeEnum;
import com.farm.wheat.share.chan.util.PriceRunTypeEnum;

import java.util.ArrayList;
import java.util.List;

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
            PriceRunTypeEnum priceType = KLine.getPriceRunType();
            KLine containKLine = KLine.getContainKLine();
            // 判断是否是包含K线
            if (containKLine != null && containKLine == KLines.get(index - 1).getContainKLine()) {
                continue;
            }
            Pair<Integer, Integer> nodePair = twoPrePrice(KLines, index);
            // 和前两根进行比较
            Integer firstPre = nodePair.getFirst();
            Integer secondPre = nodePair.getSecond();
            if (null == secondPre || null == firstPre || priceType == KLines.get(firstPre).getPriceRunType()) {
                // - 表示空点
                continue;
            }
            KLine firstPreKLine = KLines.get(firstPre);
            // 判断当前节点是否是包含K线或和上个方向一致
            if (priceType == firstPreKLine.getPriceRunType()) {
                continue;
            }
            // 至此方向不在一致，确认分型
            KLine firstPreContainKLine = firstPreKLine.getContainKLine();
            KLine priceContainKLine = KLine.getContainKLine();
            double firstPrePriceTodayMaxPrice = firstPreContainKLine != null ? firstPreContainKLine.getTodayMaxPrice() : firstPreKLine.getTodayMaxPrice();
            double priceTodayMaxPrice = priceContainKLine != null ? priceContainKLine.getTodayMaxPrice() : KLine.getTodayMaxPrice();
            if (firstPrePriceTodayMaxPrice > priceTodayMaxPrice) {
                KLine topKLine = topPrice(KLines, firstPre, index);
                // 判断上个分型是否是顶分型，如果是去掉，只保留最高的，
//                Pair<Integer, Price> last = getLastPair(pairs);
//                if (last != null && PriceTypeEnum.TOP == last.getSecond().getPriceType()) {
//                    double todayMaxPrice = last.getSecond().getTodayMaxPrice();
//                    if (pair.getSecond().getTodayMaxPrice() > todayMaxPrice) {
//                        pair.getSecond().setPriceType(PriceTypeEnum.TOP);
//                        pairs.add(pair);// 添加新元素
//                    } else {
//                        last.getSecond().setPriceType(PriceTypeEnum.NONE);
//                        pairs.remove(pairs.size() - 1);
//                    }
//                } else {
//
//                }
                topKLine.setPriceType(KTypeEnum.TOP);
                pairs.add(topKLine);// 添加新元素
            } else {
                KLine bottomKLine = bottomPrice(KLines, firstPre, index);
                // 判断上个分型是否是底分型，如果是去掉，只保留最底的，
//                Pair<Integer, Price> last = getLastPair(pairs);
//                if (last != null && PriceTypeEnum.BOTTOM == last.getSecond().getPriceType()) {
//                    double todayMinPrice = last.getSecond().getTodayMinPrice();
//                    if (pair.getSecond().getTodayMinPrice() < todayMinPrice) {
//                        pair.getSecond().setPriceType(PriceTypeEnum.BOTTOM);
//                        pairs.add(pair);
//                    } else {
//                        last.getSecond().setPriceType(PriceTypeEnum.NONE);
//                        pairs.remove(pairs.size() - 1);
//                    }
//                } else {
//                    pair.getSecond().setPriceType(PriceTypeEnum.BOTTOM);
//                    pairs.add(pair);
//                }
                bottomKLine.setPriceType(KTypeEnum.BOTTOM);
                pairs.add(bottomKLine);
            }
        }
        return pairs;
    }

    private static KLine topPrice(List<KLine> KLines, int firstPre, int index) {
        KLine max = KLines.get(firstPre);
        KLine KLine;
        for (int i = firstPre + 1; i < index; i++) {
            KLine = KLines.get(i);
            if (KLine.getTodayMaxPrice() > max.getTodayMaxPrice()) {
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
            if (KLine.getTodayMinPrice() < min.getTodayMinPrice()) {
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