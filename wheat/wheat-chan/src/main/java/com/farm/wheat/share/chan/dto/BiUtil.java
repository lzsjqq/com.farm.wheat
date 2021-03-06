package com.farm.wheat.share.chan.dto;

import com.farm.common.utils.Linked;
import com.farm.common.utils.Node;
import com.farm.common.utils.NullCheckUtils;
import com.farm.common.utils.Pair;
import com.farm.wheat.share.chan.util.BiPriceTypeEnum;
import com.farm.wheat.share.chan.util.BiSequence;
import com.farm.wheat.share.chan.util.Duan;
import com.farm.wheat.share.chan.util.KTypeEnum;
import com.farm.wheat.share.chan.util.RunTypeEnum;
import com.farm.wheat.share.chan.util.Segment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 笔的处理
 * @author: xyc
 * @create: 2021-03-05 00:14
 */
public class BiUtil {
    /**
     * 笔的最小根数
     */
    private static final int BI_MIN_SIZE = 5;

    /**
     * 画笔
     *
     * @param topBottoms
     * @param KLines
     */
    public static List<Segment> chanBi(List<KLine> topBottoms, List<KLine> KLines) {
        // 找到笔的特征序列K线
        List<BiSequence> biSequences = new ArrayList<>();
        int size = topBottoms.size();
        for (int i = 0; i < size; i++) {
            if (i + 1 >= size) {
                break;
            }
            KLine one = topBottoms.get(i);
            KLine two = topBottoms.get(++i);
            KTypeEnum priceType = one.getPriceType();
            BiSequence biPrice = null;
            switch (priceType) {
                case TOP:
                    double todayMaxPrice = one.getMaxPrice();
                    double todayMinPrice = two.getMinPrice();
                    biPrice = BiSequence.builder()
                            .fromTradingDate(one.getTradingDate())
                            .fromIndex(one.getIndex())
                            .toTradingDate(two.getTradingDate())
                            .toIndex(two.getIndex())
                            .todayMaxPrice(todayMaxPrice)
                            .todayMinPrice(todayMinPrice)
                            .priceType(BiPriceTypeEnum.TOP)
                            .priceRunType(RunTypeEnum.NONE)
                            .build();
                    break;
                case BOTTOM:
                    todayMinPrice = one.getMinPrice();
                    todayMaxPrice = two.getMaxPrice();
                    biPrice = BiSequence.builder()
                            .fromTradingDate(one.getTradingDate())
                            .fromIndex(one.getIndex())
                            .toTradingDate(two.getTradingDate())
                            .toIndex(two.getIndex())
                            .todayMaxPrice(todayMaxPrice)
                            .todayMinPrice(todayMinPrice)
                            .priceType(BiPriceTypeEnum.BOTTOM)
                            .priceRunType(RunTypeEnum.NONE)
                            .build();
                    break;
                default:
                    break;
            }
            biSequences.add(biPrice);
        }
        // 做包含处理
        biSequences = handleSequencesContain(biSequences);
        // 得到顶底分型
        return crtSegmentsByDuan(topBottoms, duan(biSequences, KLines), KLines);
    }
    /**
     * 得到顶底分型
     *
     * @param biSequences
     * @return Pair<Integer, BiPrice> Integer:biPrices的index,  BiPrice:为biPrices的index
     */
    public static List<Duan> duan(List<BiSequence> biSequences, List<KLine> KLines) {
        List<Duan> duans = new ArrayList<>();
        List<BiSequence> types = new ArrayList<>();
        int size;
        if (NullCheckUtils.isBlank(biSequences) || (size = biSequences.size()) < 3) {
            return duans;
        }
        BiSequence biSequence = biSequences.get(0);
        Duan duan = new Duan();
        duan.setFrom(biSequence.getFromIndex());
        for (int index = 2; index < size; index++) {
            BiSequence price = biSequences.get(index);
            RunTypeEnum priceRunType = price.getPriceRunType();
            // 和前两根进行比较
            // 判断当前节点是否是包含K线或和上个方向一致
            BiSequence firstPrice = biSequences.get(index - 1);
            RunTypeEnum firstPriceRunType = firstPrice.getPriceRunType();
            if (priceRunType == firstPriceRunType) {
                if (index == size - 1) {
                    if (priceRunType == RunTypeEnum.UP) {
                        price.setPriceType(BiPriceTypeEnum.TOP);
                    } else {
                        price.setPriceType(BiPriceTypeEnum.BOTTOM);
                    }
                    // 添加新元素
                    types.add(price);
                }
                // - 表示空点
                continue;
            }
            // 至此方向不在一致，确认分型
            if (firstPriceRunType == RunTypeEnum.UP) {
                firstPrice.setPriceType(BiPriceTypeEnum.TOP);
                int mustIndex = getMustIndex(BiPriceTypeEnum.TOP, KLines, firstPrice);
                duan.setTo(mustIndex);
                duan.setPriceRunType(RunTypeEnum.UP);
                duans.add(duan);
                duan = new Duan();
                duan.setFrom(mustIndex);
            } else {
                firstPrice.setPriceType(BiPriceTypeEnum.BOTTOM);
                int mustIndex = getMustIndex(BiPriceTypeEnum.BOTTOM, KLines, firstPrice);
                duan.setTo(mustIndex);
                duan.setPriceRunType(RunTypeEnum.DOWN);
                duans.add(duan);
                duan = new Duan();
                duan.setFrom(mustIndex);
            }
            // 添加新元素
            types.add(firstPrice);
        }

        BiSequence lastType = types.get(types.size() - 1);
        BiSequence lastSequence = biSequences.get(biSequences.size() - 1);
        if (!lastType.getFromIndex().equals(lastSequence.getFromIndex())) {
            duan = new Duan();
            duan.setFrom(duans.get(duans.size() - 1).getTo());
            duan.setTo(lastSequence.getToIndex());
            duans.add(duan);
        }
        return duans;
    }
    private static int getMustIndex(BiPriceTypeEnum biPriceType, List<KLine> KLines, BiSequence biSequences) {
        KLine KLine = KLines.get(biSequences.getFromIndex());
        boolean isTop = KLine.getPriceType() == KTypeEnum.TOP;
        int index;
        if (biPriceType == BiPriceTypeEnum.BOTTOM) {
            index = isTop ? biSequences.getToIndex() : biSequences.getFromIndex();
        } else {
            index = isTop ? biSequences.getFromIndex() : biSequences.getToIndex();
        }
        return index;
    }

    /**
     * 处理包含关系
     *
     * @param biSequences
     */
    private static List<BiSequence> handleSequencesContain(List<BiSequence> biSequences) {
        int size = biSequences.size();
        List<BiSequence> handledSequence = new ArrayList<>();
        BiSequence first = null;
        BiSequence second = null;
        for (int index = 0; index < size; index++) {
            if (first == null) {
                first = biSequences.get(index);
                continue;
            }
            second = biSequences.get(index);
            /**
             * 前一个
             */
            double secondTodayMaxPrice = second.getTodayMaxPrice();
            double secondTodayMinPrice = second.getTodayMinPrice();
            // 判断是否是包含K线
            double firstPriceMaxPrice = first.getTodayMaxPrice();
            double firstPriceMinPrice = first.getTodayMinPrice();
            double max = secondTodayMaxPrice - firstPriceMaxPrice;
            double min = secondTodayMinPrice - firstPriceMinPrice;
            if (max > 0 && min > 0) {
                // 高高 向上
                second.setPriceRunType(RunTypeEnum.UP);
                // 确认方向添加
                handledSequence.add(first);
                first = second;
                second = null;
            } else if (max < 0 && min < 0) {
                // 低低 向下
                second.setPriceRunType(RunTypeEnum.DOWN);
                handledSequence.add(first);
                first = second;
                second = null;
            } else {
                // 类型为包含
                if (max >= 0) {
                    first = second;
                    handledSequence = removeContain(handledSequence, first);
                    BiSequence last = handledSequence.get(handledSequence.size() - 1);
                    // 设置方向
                    if (last != null) {
                        if (firstPriceMaxPrice > last.getTodayMaxPrice()) {
                            first.setPriceRunType(RunTypeEnum.UP);
                        } else {
                            first.setPriceRunType(RunTypeEnum.DOWN);
                        }
                    }
                    second = null;
                } else {
                    second = null;
                }
            }
        }
        if (first != null) {
            handledSequence.add(first);
        }
        if (second != null) {
            handledSequence.add(second);
        }
        // 判断是否是最后一个
        BiSequence biSequence = handledSequence.get(handledSequence.size() - 1);
        int biSequencesSize = biSequences.size() - 1;
        int lastIndex = biSequencesSize;
        for (int i = 0; i < biSequences.size(); i++) {
            if (biSequences.get(i).getFromIndex() == biSequence.getFromIndex()) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != biSequencesSize) {
            biSequences.subList(lastIndex, biSequencesSize);
        }


        return handledSequence;
    }

    /**
     * 所有和 first 具有包含关系的都去除掉
     *
     * @param handledSequence
     * @param first
     * @return
     */
    public static List<BiSequence> removeContain(List<BiSequence> handledSequence, BiSequence first) {
        int size = handledSequence.size();
        if (size <= 0) {
            return handledSequence;
        }

        BiSequence lastSequence = handledSequence.get(size - 1);
        double todayMaxPrice = lastSequence.getTodayMaxPrice();
        double todayMinPrice = lastSequence.getTodayMinPrice();
        double firstTodayMaxPrice = first.getTodayMaxPrice();
        double firstTodayMinPrice = first.getTodayMinPrice();
        if (firstTodayMaxPrice > todayMaxPrice && firstTodayMinPrice < todayMinPrice) {
            handledSequence.remove(size - 1);
            return removeContain(handledSequence, first);
        } else {
            return handledSequence;
        }
    }
    /**
     * 创建线段
     *
     * @param topBottoms
     * @param KLines
     * @return
     */
    private static List<Segment> crtSegmentsByDuan(List<KLine> topBottoms, List<Duan> duans, List<KLine> KLines) {
        List<Segment> segments = new ArrayList<>();
        for (Duan duan : duans) {
            Integer from = duan.getFrom();
            Integer to = duan.getTo();
            Segment segment = new Segment();
            segment.setFromIndex(from);
            segment.setToIndex(to);
            segment.setBiKLines(getSegmentBiPrice(topBottoms, segment, KLines));
            segments.add(segment);
        }
        return segments;
    }


    /**
     * 获得段之间的顶低分型
     *
     * @param topBottoms
     * @param segment
     * @return
     */
    private static List<KLine> getSegmentBiPrice(List<KLine> topBottoms, Segment segment, List<KLine> KLines) {
        Integer fromIndex = segment.getFromIndex();
        Integer toIndex = segment.getToIndex();
        List<KLine> biKLines = new ArrayList<>();
        KLine KLine;
        KLine last;
        KLine pre;
        for (int i = 0; i < topBottoms.size(); i++) {
            KLine = topBottoms.get(i);
            Integer first = KLine.getIndex();
            if (fromIndex <= first && first <= toIndex) {
                // 处理两个顶底分型的K线数量
                biKLines.add(KLine);
                last = biKLines.get(biKLines.size() - 1);
                if (1 == biKLines.size()) {
                    continue;
                }
                pre = biKLines.get(biKLines.size() - 2);
                // 计算两者之间的K线数量
                pre.setFromToSize(nonContainSize(KLines, pre.getIndex(), last.getIndex()));
            }
        }
        return biKLines;
    }

    /**
     * 确定两个坐标之间的非包含K线的数量
     *
     * @param KLines
     * @param firstIndex
     * @param secondIndex
     * @return
     */
    public static int nonContainSize(List<KLine> KLines, Integer firstIndex, Integer secondIndex) {
        int size = 0;
        boolean isContain = false;
        for (int i = firstIndex; i <= secondIndex; i++) {
            KLine KLine1 = KLines.get(i);
            KLine containKLine = KLine1.getContainKLine();
            if (null == containKLine) {
                size++;
                isContain = false;
                continue;
            }
            if (!isContain && null != containKLine) {
                isContain = true;
                size++;
                continue;
            }
        }
        return size;
    }

    /**
     * 获取两个顶底分型之间的K线数量
     *
     * @param KLines
     * @param biKLines
     * @return
     */
    public static void containSize(List<KLine> KLines, List<KLine> biKLines) {
        KLine first;
        KLine second;
        int size = biKLines.size();
        for (int i = 0; i < size; i++) {
            if (size - i == 1) {
                break;
            }
            first = biKLines.get(i);
            second = biKLines.get(i + 1);
            first.setFromToSize(nonContainSize(KLines, first.getIndex(), second.getIndex()));
        }
    }


    /**
     * 取人两个顶底分型之间的笔
     *
     * @param containSize
     * @param fromIndex
     * @param toIndex
     */
    private static Set<Integer> confirmBi(List<Integer> containSize, Integer fromIndex, Integer toIndex) {
        Set<Integer> biTopBottoms = new HashSet<>();
        Integer first = null;
        Integer second = null;
        Integer third = null;
        if (toIndex - fromIndex <= 1) {
            biTopBottoms.add(fromIndex);
            biTopBottoms.add(toIndex);
            return biTopBottoms;
        }
        int i = fromIndex;
        first = containSize.get(i);
        if (first < 5) {
        }

        for (i = fromIndex; i <= toIndex - 1; i++) {
            if (null == first) {
                first = containSize.get(i);
                biTopBottoms.add(i);
                if (first >= 5) {
                    // 当前笔的终点
                    biTopBottoms.add(i + 1);
                    i = i + 1;
                } else {
                    if (i + 3 <= toIndex) {
                        biTopBottoms.add(i + 3);
                        i = i + 3;
                    }
                }
            }

            if (i <= toIndex - 2) {
                second = containSize.get(i);
                third = containSize.get(i + 1);
                if (second >= 5 && third >= 5) {
                    biTopBottoms.add(i + 1);
                    i = i + 1;
                    continue;
                }
            }

        }
        return biTopBottoms;
    }

    /**
     * 创建线段
     *
     * @param topBottoms
     * @param sequences
     * @param KLines
     * @return
     */
    private static List<Segment> crtSegments(List<KLine> topBottoms, List<BiSequence> sequences, List<KLine> KLines) {
        List<Segment> segments = new ArrayList<>();
        // 初始化
        Segment segment = null;
        int size = sequences.size();
        BiSequence sequence = sequences.get(0);
        KLine topBottomKLine = topBottoms.get(0);
        int index;
        if (BiPriceTypeEnum.TOP == sequence.getPriceType()) {
            index = sequence.getToIndex();
        } else {
            index = sequence.getFromIndex();
        }
        if (topBottomKLine.getIndex() != index) {
            segment = new Segment();
            segment.setFromIndex(topBottomKLine.getIndex());
        }
        Boolean is = null;
        for (int i = 0; i < size; i++) {
            sequence = sequences.get(i);
            if (null == segment) {
                segment = new Segment();
                segment.setFromIndex(0);
                continue;
            }
            if (null == is) {
                KLine KLine = KLines.get(segment.getFromIndex());
                KTypeEnum priceType = KLine.getPriceType();
                is = priceType == KTypeEnum.TOP;
            }
            if (BiPriceTypeEnum.TOP == sequence.getPriceType()) {
                Integer toIndex = is ? sequence.getFromIndex() : sequence.getToIndex();
                if (null == segment.getFromIndex()) {
                    segment.setFromIndex(toIndex);
                }
                segment = setSegment(topBottoms, KLines, segments, segment, toIndex, null, segment.getToIndex());
                continue;
            }
            if (BiPriceTypeEnum.BOTTOM == sequence.getPriceType()) {
                Integer fromIndex = is ? sequence.getToIndex() : sequence.getFromIndex();
                segment = setSegment(topBottoms, KLines, segments, segment, fromIndex, segment.getToIndex(), null);
            }
        }
        // 处理最后一个
        sequence = sequences.get(sequences.size() - 1);
        topBottomKLine = topBottoms.get(topBottoms.size() - 1);
        if (BiPriceTypeEnum.TOP == sequence.getPriceType()) {
            index = is ? sequence.getFromIndex() : sequence.getToIndex();
        } else {
            index = is ? sequence.getToIndex() : sequence.getFromIndex();
        }
        if (topBottomKLine.getIndex() != index) {
            segment = new Segment();
            Integer fromIndex = segments.get(segments.size() - 1).getToIndex();
            segment.setFromIndex(fromIndex);
            segment.setToIndex(topBottomKLine.getIndex());
            setSegment(topBottoms, KLines, segments, segment, fromIndex, segment.getToIndex(), null);
        }
        return segments;
    }

    private static Segment setSegment(List<KLine> topBottoms, List<KLine> KLines, List<Segment> segments, Segment segment, Integer fromIndex, Integer toIndex, Integer o) {
        if (toIndex == o) {
            segment.setToIndex(fromIndex);
        }
        segments.add(segment);
        // 段之间的顶低分型
        segment.setBiKLines(getSegmentBiPrice(topBottoms, segment, KLines));
        segment = new Segment();
        segment.setFromIndex(fromIndex);
        return segment;
    }


    private static List<KLine> linkToList(Linked<KLine> biPrices) {
        List<KLine> list = new ArrayList<>();
        Node<KLine> first = biPrices.getFirst();
        if (first == null) {
            return list;
        }
        list.add(first.data);
        while (first.next != null) {
            list.add(first.next.data);
            first = first.next;
        }
        return list;

    }

    /**
     * 得到笔
     */
    public static void bi(List<Pair<Integer, KLine>> topBottoms, List<KLine> KLines) {
        if (NullCheckUtils.isBlank(KLines) || NullCheckUtils.isBlank(topBottoms)) {
            return;
        }
        // 潜在笔
        List<Pair<Integer, KLine>> biPairs = new ArrayList<>();
        // 确定笔
        List<Pair<Integer, KLine>> surePairs = new ArrayList<>();
        int topBottomsSize = topBottoms.size();
        for (int index = 0; index < topBottomsSize; index++) {
            Pair<Integer, KLine> pair1 = topBottoms.get(index);
            Integer firstIndex1 = pair1.getFirst();
            KLine firstKLine1 = pair1.getSecond();
            KTypeEnum firstPriceType1 = firstKLine1.getPriceType();
            // 直接判断下一个
            if (index + 1 == topBottomsSize) {
                continue;
            }
            // 现在已经是一顶一底了
            index = index + 1;
            Pair<Integer, KLine> pair2 = topBottoms.get(index);
            Integer firstIndex2 = pair2.getFirst();
            KLine firstKLine2 = pair2.getSecond();
            // 判断是否存在5根K线,确定两个坐标之间的非包含K线的数量
            int size = nonContainSize(KLines, firstIndex1, firstIndex2);
            if (size >= BI_MIN_SIZE) {
                biPairs.add(new Pair<>(firstIndex1, firstKLine1));
                biPairs.add(new Pair<>(firstIndex2, firstKLine2));
                continue;
            } else {
                // 得到下一个
                index = index + 1;
                Pair<Integer, KLine> pair3 = topBottoms.get(index);
                Integer secondIndex1 = pair2.getFirst();
                KLine secondKLine1 = pair2.getSecond();
                // 升笔
                if (KTypeEnum.TOP == firstPriceType1) {

                }
                // 降笔
                if (KTypeEnum.BOTTOM == firstPriceType1) {

                }


            }
            // 下次循环开始 index
        }
    }

    public static void bi(List<Pair<Integer, KLine>> topBottoms) {
        if (NullCheckUtils.isBlank(topBottoms)) {
            return;
        }
        try {
            int i = 0;
            Pair<Integer, KLine> one = topBottoms.get(i);
            KLine KLine = one.getSecond();
            Integer first = one.getFirst();
            KTypeEnum priceType = KLine.getPriceType();
            if (priceType == KTypeEnum.TOP) {
                Pair<Integer, KLine> two = getNextTopBottom(topBottoms, ++i);
            }
            if (priceType == KTypeEnum.BOTTOM) {
                Pair<Integer, KLine> two = getNextTopBottom(topBottoms, ++i);
                Pair<Integer, KLine> three = getNextTopBottom(topBottoms, ++i);
                Pair<Integer, KLine> four = getNextTopBottom(topBottoms, ++i);


            }
        } catch (Exception e) {
            return;
        }
    }
    /**
     * @param topBottoms
     * @param index
     * @return
     */
    private static Pair<Integer, KLine> getNextTopBottom(List<Pair<Integer, KLine>> topBottoms, int index) throws Exception {
        int size = topBottoms.size();
        if (size > index + 1) {
            return topBottoms.get(index);
        }
        throw new Exception("is null");
    }

    /**
     * 获取下一个相反的分型
     *
     * @param priceType1
     * @param topBottoms
     * @param index
     * @return Pair<Integer, Pair < Integer, Price>>
     */
    private static Pair<Integer, Pair<Integer, KLine>> getNextType(KTypeEnum priceType1, List<Pair<Integer, KLine>> topBottoms, int index) {
        boolean isHaveNext = false;
        Pair<Integer, KLine> target = null;
        Integer topBottomIndex = null;
        for (int i = index; i < topBottoms.size(); i++) {
            Pair<Integer, KLine> pair = topBottoms.get(i);
            KLine second = pair.getSecond();
            KTypeEnum priceType2 = second.getPriceType();
            // 找到第一个相反分型
            if (!isHaveNext && priceType1 != priceType2) {
                isHaveNext = true;
                target = pair;
                topBottomIndex = i;
                continue;
            }
            //
            if (isHaveNext && priceType1 == priceType2) {
                target = pair;
                break;
            }
            // 找到下一个相反分型
            if (priceType1 != priceType2) {
                target = pair;
                topBottomIndex = i;
                continue;
            }
        }
        return target == null ? null : new Pair<>(topBottomIndex, target);
    }
}