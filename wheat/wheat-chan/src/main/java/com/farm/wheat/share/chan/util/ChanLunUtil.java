package com.farm.wheat.share.chan.util;

import com.farm.common.utils.Linked;
import com.farm.common.utils.Node;
import com.farm.common.utils.NullCheckUtils;
import com.farm.common.utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @program: wheat
 * @description: 用于构造处理链表
 * @author: xyc
 * @create: 2020-03-13 21:49
 */
public class ChanLunUtil {
    private ChanLunUtil() {
    }

    /**
     * 添加index
     *
     * @param prices prices
     * @return List<Price>
     */
    public static List<Price> addIndex(List<Price> prices) {
        for (int i = 0; i < prices.size(); i++) {
            prices.get(i).setIndex(i);
        }
        return prices;
    }

    /**
     * 构建链表
     *
     * @return
     */
    public static List<Price> buildLined(List<Price> prices) throws Exception {
        prices = addIndex(prices);
        // 处理包含关系包含关系
        handleContain(prices);
        // 处理顶底分型,过滤掉连续的同类型分型只保留最高或最低的
        List<Price> topBottoms = topBottomType(prices);
        // 画笔     -：表示空点  "1-"+ MaxPrice：顶分型    "0-"+ MinPrice：低分型
//        topBottoms = removeTogether(topBottoms, 0);
//        topBottoms = removeTogether(topBottoms, 1);
//        topBottoms = removeTogether(topBottoms, 2);
        List<Segment> segments = chanBi(topBottoms, prices);
        List<Bi> bis = huaBi2(segments);
        List<Integer> huaBiIndex = getHuaBiIndex(bis);
//        List<Integer> huaBiIndex = huaBi(segments, prices);
        for (int i = 0; i < prices.size(); i++) {
            if (!huaBiIndex.contains(i)) {
                prices.get(i).setPriceType(PriceTypeEnum.NONE);
            }
        }
        return prices;
    }

    private static List<Integer> getHuaBiIndex(List<Bi> bis) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < bis.size(); i++) {
            Bi bi = bis.get(i);
            addBiTopBottomsList(list, bi.getFromIndex());
            addBiTopBottomsList(list, bi.getToIndex());
        }
        return list;
    }

    /**
     * 去除共用的顶底分型
     *
     * @param topBottoms
     * @return
     */
    public static List<Price> removeTogether(List<Price> topBottoms, int beginIndex) {
        int size;
        if (NullCheckUtils.isBlank(topBottoms) || ((size = topBottoms.size()) - beginIndex) <= 2) {
            return topBottoms;
        }
        List<Price> newTopBottoms = new ArrayList<>();
        Price first = null;
        Price second = null;
        Price third = null;
        int validIndex = size - 1;
        for (int i = 0; i < beginIndex; i++) {
            newTopBottoms.add(topBottoms.get(i));
        }
        for (int i = beginIndex; i <= validIndex; i++) {

            if (topBottoms.get(i).getTradingDate().equals("2019-02-28")) {
                System.out.println();
            }
            if (null == first) {
                first = topBottoms.get(i);
                third = null;
                second = null;
                continue;
            }
            if (second == null) {
                second = topBottoms.get(i);
                third = null;
                continue;
            }
            third = topBottoms.get(i);
            if (third.getIndex() - second.getIndex() == 1) {
                if (moreCondition(first, third)) {
                    first = third;
                } else {
                    newTopBottoms.add(first);
                    first = null;
                }
                second = null;
                third = null;
                continue;
            }
            newTopBottoms.add(first);
            first = second;
            second = third;
            if (i == validIndex) {
                third = null;
            }
        }
        if (first != null) {
            newTopBottoms.add(first);
        }
        if (second != null) {
            newTopBottoms.add(second);
        }
        if (third != null) {
            newTopBottoms.add(third);
        }
        return newTopBottoms;
    }

    /**
     * 在共用K线的情况下是否符合添加条件
     *
     * @param first
     * @param third
     * @return
     */
    private static boolean moreCondition(Price first, Price third) {
        boolean condition;
        if (first.getPriceType() == PriceTypeEnum.BOTTOM) {
            double nextTodayMinPrice = third.getContainPrice() == null ? third.getTodayMinPrice() : third.getContainPrice().getTodayMinPrice();
            double preTodayMinPrice = first.getContainPrice() == null ? first.getTodayMinPrice() : first.getContainPrice().getTodayMinPrice();
            condition = nextTodayMinPrice - preTodayMinPrice < 0;
        } else {
            double nextTodayMaxPrice = third.getContainPrice() == null ? third.getTodayMaxPrice() : third.getContainPrice().getTodayMaxPrice();
            double preTodayMaxPrice = first.getContainPrice() == null ? first.getTodayMaxPrice() : first.getContainPrice().getTodayMaxPrice();
            condition = nextTodayMaxPrice - preTodayMaxPrice > 0;
        }
        return condition;
    }

    /**
     * 去掉重复元素
     *
     * @param biTopBottoms 组成笔的index
     * @param priceIndex   prices 的下标
     */
    private static void addBiTopBottomsList(List<Integer> biTopBottoms, int priceIndex) {
        if (!biTopBottoms.contains(priceIndex)) {
            biTopBottoms.add(priceIndex);
        }
    }

    /**
     * 获取最后一个值
     *
     * @param biPrices
     * @return
     */
    private static Price getLastPrice(List<Price> biPrices) {
        return biPrices.get(biPrices.size() - 1);
    }

    /**
     * 获取下一个值
     *
     * @param biPrices
     * @return
     */
    private static Price getNextPrice(List<Price> biPrices, Integer nowIndex) {
        return biPrices.size() - 1 > nowIndex ? biPrices.get(nowIndex + 1) : null;
    }

    /**
     * 画笔
     *
     * @param segments
     * @return
     */
    private static List<Integer> huaBi(List<Segment> segments, List<Price> prices) {
        List<Integer> biTopBottoms = new ArrayList<>();
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            List<Price> biPrices = segment.getBiPrices();
            int size = biPrices.size();

            if (size <= 2) {
                addBiTopBottomsList(biTopBottoms, biPrices.get(0).getIndex());
                addBiTopBottomsList(biTopBottoms, biPrices.get(1).getIndex());
                continue;
            }

            // 第一个笔是否成型
            Integer fromIndex = null;
            for (int j = 0; j < size; j++) {
                Price price = biPrices.get(j);
                if (NullCheckUtils.isBlank(fromIndex)) {
                    fromIndex = price.getIndex();
                }
                if (price.getFromToSize() < 5) {
                    j += 3;
                    // 判断
                    if (j >= size - 1) {
                        addBiTopBottomsList(biTopBottoms, fromIndex);
                        addBiTopBottomsList(biTopBottoms, getLastPrice(biPrices).getIndex());
                        continue;
                        // 什么都不做了
                    } else {
                        Triple<Price, Price, Integer> confirmBiPrice = confirmBiPrice(biTopBottoms, biPrices, fromIndex, j);
                        j = confirmBiPrice.getThird();
                        fromIndex = null;
                    }
                } else {
                    // 第一笔大于5
                    Triple<Price, Price, Integer> confirmBiPrice = confirmBiPrice(biTopBottoms, biPrices, fromIndex, j);
                    j = confirmBiPrice.getThird();
                    fromIndex = null;
                }
            }
        }
        return biTopBottoms;
    }

    /**
     * 画笔
     *
     * @param segments
     * @return
     */
    private static List<Bi> huaBi2(List<Segment> segments) {
        List<Bi> confirmBis = new ArrayList<>();
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            List<Price> biPrices = segment.getBiPrices();
            int size = biPrices.size();
            if (size <= 2) {
                justTwo(confirmBis, biPrices);
                continue;
            }
            // 第一个笔是否成型
            List<Bi> unConfirmOneBis = getUnConfirmBis(biPrices, size, 0);
            List<Bi> unConfirmTwoBis = getUnConfirmBis(biPrices, size, 1);
            confirmBis.addAll(getConfirmBis(unConfirmOneBis, unConfirmTwoBis));
        }
        return confirmBis;
    }

    private static List<Bi> getConfirmBis(List<Bi> unConfirmOneBis, List<Bi> unConfirmTwoBis) {
        List<Bi> confirmBis = new ArrayList<>();
        Bi bi = null;
        for (int i = 0; i < unConfirmOneBis.size(); i++) {
            Bi one = unConfirmOneBis.get(i);
            if (null == bi) {
                bi = new Bi();
                setBiFrom(bi, one.getFromPrice());
            }
            if (i <= unConfirmTwoBis.size() - 1) {
                Bi tow = unConfirmTwoBis.get(i);
                // 小于5说明，反向笔没有确定
                if (tow.getFromPrice().getFromToSize() < 5) {
                    continue;
                }
                // 大于5,反向笔确定
                // 判断是否为此时的i+1不是最后一个待定笔
                if (i + 1 < unConfirmOneBis.size() - 1) {
                    // 第一笔
                    setBiTo(bi, one.getToPrice());
                    // 添加确定笔
                    confirmBis.add(bi);
                    // 第二笔
                    bi = new Bi();
                    setBiFrom(bi, tow.getFromPrice());
                    setBiTo(bi, tow.getToPrice());
                    confirmBis.add(bi);
                    // 第三笔
                    bi = null;
                    continue;
                }
                // 判断是否为此时的i+1是最后一个待定笔且是最后一个待定笔时不足五根
                if (i + 1 == unConfirmOneBis.size() - 1 && unConfirmOneBis.get(i + 1).getFromPrice().getFromToSize() < 5) {
                    // 第一笔
                    setBiTo(bi, unConfirmOneBis.get(i + 1).getToPrice());
                    // 添加确定笔
                    confirmBis.add(bi);
                    break;
                }
                // 判断是否为此时的i+1是最后一个待定笔且是最后一个待定笔时足五根
                if (i + 1 == unConfirmOneBis.size() - 1 && unConfirmOneBis.get(i + 1).getFromPrice().getFromToSize() >= 5) {
                    // 第一笔
                    setBiTo(bi, one.getToPrice());
                    // 添加确定笔
                    confirmBis.add(bi);
                    // 第二笔
                    bi = new Bi();
                    setBiFrom(bi, one.getToPrice());
                    setBiTo(bi, tow.getToPrice());
                    confirmBis.add(bi);
                    // 第三笔
                    bi = new Bi();
                    setBiFrom(bi, tow.getToPrice());
                    setBiTo(bi, unConfirmOneBis.get(i + 1).getToPrice());
                    confirmBis.add(bi);
                    break;
                }


            } else {
                // 此时走到最后
                setBiTo(bi, one.getToPrice());
                // 添加确定笔
                confirmBis.add(bi);
                break;
            }

        }
        return confirmBis;
    }

    private static List<Bi> getUnConfirmBis(List<Price> biPrices, int size, int i2) {
        List<Bi> unConfirmBis = new ArrayList<>(size / 2);
        for (int j = i2; j + 1 < size; j += 2) {
            Bi bi = new Bi();
            setBiFrom(bi, biPrices.get(j));
            setBiTo(bi, biPrices.get(j + 1));
            unConfirmBis.add(bi);
        }
        return unConfirmBis;
    }

    private static void setBiFrom(Bi bi, Price price) {
        bi.setFromIndex(price.getIndex());
        bi.setFromPrice(price);
    }

    private static void justTwo(List<Bi> bis, List<Price> biPrices) {
        Bi bi = new Bi();
        Price price = biPrices.get(0);
        setBiFrom(bi, price);
        Price price1 = biPrices.get(1);
        setBiTo(bi, price1);
        bis.add(bi);
    }

    private static void lastTwo(int index, List<Bi> bis, List<Price> biPrices) {
        Bi bi = new Bi();
        Price price = biPrices.get(index);
        setBiFrom(bi, price);
        Price lastPrice = getLastPrice(biPrices);
        setBiTo(bi, lastPrice);
        bis.add(bi);
    }

    private static void lastTwo(Bi bi, List<Bi> bis, List<Price> biPrices) {
        Price lastPrice = getLastPrice(biPrices);
        setBiTo(bi, lastPrice);
        bis.add(bi);
    }

    private static void setBiTo(Bi bi, Price lastPrice) {
        bi.setToIndex(lastPrice.getIndex());
        bi.setToPrice(lastPrice);
    }

    /**
     * @param biTopBottoms
     * @param biPrices
     * @param fromIndex
     * @param j
     * @return Triple<Price, Price, Integer> Integer 增量
     */
    private static Triple<Price, Price, Integer> confirmBiPrice(List<Integer> biTopBottoms, List<Price> biPrices, Integer fromIndex, int j) {
        Triple<Price, Price, Integer> confirmBiPrice = getConfirmBiPrice(biPrices, j + 1, 0);
        if (null != confirmBiPrice.getFirst()) {
            addBiTopBottomsList(biTopBottoms, fromIndex);
            addBiTopBottomsList(biTopBottoms, confirmBiPrice.getFirst().getIndex());
        }
        if (null != confirmBiPrice.getSecond()) {
            addBiTopBottomsList(biTopBottoms, confirmBiPrice.getSecond().getIndex());
        }
        return confirmBiPrice;
    }

    /**
     * @param biPrices
     * @param j
     * @return Triple<Price, Price, Integer> Integer 增量
     */
    private static Triple<Price, Price, Integer> confirmBiPrice(List<Bi> bis, List<Price> biPrices, Bi unConfirmBi, int j) {
        Triple<Price, Price, Integer> confirmBiPrice = getConfirmBi(biPrices, j, 0);
        if (null != confirmBiPrice.getFirst()) {
            setBiTo(unConfirmBi, confirmBiPrice.getFirst());
            bis.add(unConfirmBi);
            unConfirmBi = new Bi();
            setBiFrom(unConfirmBi, confirmBiPrice.getFirst());
            if (null != confirmBiPrice.getSecond()) {
                setBiTo(unConfirmBi, confirmBiPrice.getSecond());
                bis.add(unConfirmBi);
                unConfirmBi = new Bi();
                setBiFrom(unConfirmBi, confirmBiPrice.getFirst());
            }
        }
        return confirmBiPrice;
    }

    /**
     * 获取成笔price
     *
     * @param biPrices
     * @param index
     * @param incr
     * @return
     */
    private static Triple<Price, Price, Integer> getConfirmBi(List<Price> biPrices, int index, int incr) {
        int size = biPrices.size();
        int lastIndex = size - 1;
        int nowIndex = incr + index;
        if (lastIndex <= nowIndex) {
            return new Triple<>(getLastPrice(biPrices), null, lastIndex);
        }
        Price nowPrice = biPrices.get(nowIndex);
        // 判断当前是否是5个
        int fromToSize = nowPrice.getFromToSize();
        Price nextPrice = getNextPrice(biPrices, nowIndex);
        int nextFromToSize = nextPrice.getFromToSize();
        // 都大于5，确定两笔
        if (fromToSize >= 5 && nextFromToSize >= 5) {
            int nextIndex = nowIndex + 1;
            return new Triple<>(nowPrice, nextPrice, nextIndex);
        }

        if (fromToSize >= 5 && nextFromToSize < 5) {
            if (getNextPrice(biPrices, nowIndex + 1) == null) {
                return new Triple<>(getLastPrice(biPrices), null, lastIndex);
            } else {
                return new Triple<>(null, null, nowIndex + 1);
            }
        }

        if (fromToSize < 5 && nowIndex + 2 <= lastIndex) {
            return getConfirmBiPrice(biPrices, nowIndex + 2, 0);
        }
        // 判断是否是最后一个
        if (nowIndex + 2 >= lastIndex) {
            return new Triple<>(biPrices.get(lastIndex), null, lastIndex);
        }
        return getConfirmBiPrice(biPrices, index, 2);

    }

    /**
     * 获取成笔price
     *
     * @param biPrices
     * @param index
     * @param incr
     * @return
     */
    private static Triple<Price, Price, Integer> getConfirmBiPrice(List<Price> biPrices, int index, int incr) {

        int size = biPrices.size();
        int lastIndex = size - 1;
        int nowIndex = incr + index;
        if (lastIndex <= nowIndex) {
            return new Triple<>(getLastPrice(biPrices), null, lastIndex);
        }
        Price nowPrice = biPrices.get(nowIndex);
        // 判断当前是否是5个
        int fromToSize = nowPrice.getFromToSize();
        Price nextPrice = getNextPrice(biPrices, nowIndex);
        int nextFromToSize = nextPrice.getFromToSize();
        if (fromToSize >= 5 && nextFromToSize >= 5) {
            return new Triple<>(nowPrice, nextPrice, index + 1);
        }

        if (fromToSize >= 5 && nextFromToSize < 5) {
            if (getNextPrice(biPrices, nowIndex + 1) == null) {
                return new Triple<>(getLastPrice(biPrices), null, lastIndex);
            } else {
                return new Triple<>(null, null, nowIndex + 1);
            }
        }

        if (fromToSize < 5 && nowIndex + 2 <= lastIndex) {
            return getConfirmBiPrice(biPrices, nowIndex + 2, 0);
        }
        // 判断是否是最后一个
        if (nowIndex + 2 >= lastIndex) {
            return new Triple<>(biPrices.get(lastIndex), null, lastIndex);
        }
        return getConfirmBiPrice(biPrices, index, 2);

    }
//    private static Set<Integer> huaBi(List<Price> prices, List<Pair<Integer, Price>> topBottoms, Map<Integer, BiPrice> biMap) {
//        Set<Integer> biTopBottoms = new HashSet<>();
//        Integer fromIndex = null;
//        Integer toIndex = null;
//        List<Integer> containSize = containSize(prices, topBottoms);
//        for (int i = 0; i < topBottoms.size(); i++) {
//            if (fromIndex == null) {
//                fromIndex = i;
//                continue;
//            }
//            BiPrice biPrice = biMap.get(i);
//            if (biPrice != null) {
//                toIndex = i;
//            }
//            if (fromIndex != null && toIndex != null) {
//                biTopBottoms.addAll(confirmBi(containSize, fromIndex, toIndex));
//                // 重新置为空
//                fromIndex = null;
//                toIndex = null;
//            }
//        }
//        return biTopBottoms;
//    }

    /**
     * 获取两个顶底分型之间的K线数量
     *
     * @param prices
     * @param biPrices
     * @return
     */
    public static void containSize(List<Price> prices, List<Price> biPrices) {
        Price first;
        Price second;
        int size = biPrices.size();
        for (int i = 0; i < size; i++) {
            if (size - i == 1) {
                break;
            }
            first = biPrices.get(i);
            second = biPrices.get(i + 1);
            first.setFromToSize(nonContainSize(prices, first.getIndex(), second.getIndex()));
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
     * @param prices
     * @return
     */
    private static List<Segment> crtSegments(List<Price> topBottoms, List<BiSequence> sequences, List<Price> prices) {
        List<Segment> segments = new ArrayList<>();
        // 初始化
        Segment segment = null;
        int size = sequences.size();
        BiSequence sequence = sequences.get(0);
        Price topBottomPrice = topBottoms.get(0);
        int index;
        if (BiPriceTypeEnum.TOP == sequence.getPriceType()) {
            index = sequence.getToIndex();
        } else {
            index = sequence.getFromIndex();
        }
        if (topBottomPrice.getIndex() != index) {
            segment = new Segment();
            segment.setFromIndex(topBottomPrice.getIndex());
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
                Price price = prices.get(segment.getFromIndex());
                PriceTypeEnum priceType = price.getPriceType();
                is = priceType == PriceTypeEnum.TOP;
            }
            if (BiPriceTypeEnum.TOP == sequence.getPriceType()) {
                Integer toIndex = is ? sequence.getFromIndex() : sequence.getToIndex();
                if (null == segment.getFromIndex()) {
                    segment.setFromIndex(toIndex);
                }
                segment = setSegment(topBottoms, prices, segments, segment, toIndex, null, segment.getToIndex());
                continue;
            }
            if (BiPriceTypeEnum.BOTTOM == sequence.getPriceType()) {
                Integer fromIndex = is ? sequence.getToIndex() : sequence.getFromIndex();
                segment = setSegment(topBottoms, prices, segments, segment, fromIndex, segment.getToIndex(), null);
            }
        }
        // 处理最后一个
        sequence = sequences.get(sequences.size() - 1);
        topBottomPrice = topBottoms.get(topBottoms.size() - 1);
        if (BiPriceTypeEnum.TOP == sequence.getPriceType()) {
            index = is ? sequence.getFromIndex() : sequence.getToIndex();
        } else {
            index = is ? sequence.getToIndex() : sequence.getFromIndex();
        }
        if (topBottomPrice.getIndex() != index) {
            segment = new Segment();
            Integer fromIndex = segments.get(segments.size() - 1).getToIndex();
            segment.setFromIndex(fromIndex);
            segment.setToIndex(topBottomPrice.getIndex());
            setSegment(topBottoms, prices, segments, segment, fromIndex, segment.getToIndex(), null);
        }
        return segments;
    }

    private static Segment setSegment(List<Price> topBottoms, List<Price> prices, List<Segment> segments, Segment segment, Integer fromIndex, Integer toIndex, Integer o) {
        if (toIndex == o) {
            segment.setToIndex(fromIndex);
        }
        segments.add(segment);
        // 段之间的顶低分型
        segment.setBiPrices(getSegmentBiPrice(topBottoms, segment, prices));
        segment = new Segment();
        segment.setFromIndex(fromIndex);
        return segment;
    }

    /**
     * 获得段之间的顶低分型
     *
     * @param topBottoms
     * @param segment
     * @return
     */
    private static List<Price> getSegmentBiPrice(List<Price> topBottoms, Segment segment, List<Price> prices) {
        Integer fromIndex = segment.getFromIndex();
        Integer toIndex = segment.getToIndex();
        List<Price> biPrices = new ArrayList<>();
        Price price;
        Price last;
        Price pre;
        for (int i = 0; i < topBottoms.size(); i++) {
            price = topBottoms.get(i);
            Integer first = price.getIndex();
            if (fromIndex <= first && first <= toIndex) {
                // 处理两个顶底分型的K线数量
                biPrices.add(price);
                last = biPrices.get(biPrices.size() - 1);
                if (1 == biPrices.size()) {
                    continue;
                }
                pre = biPrices.get(biPrices.size() - 2);
                // 计算两者之间的K线数量
                pre.setFromToSize(nonContainSize(prices, pre.getIndex(), last.getIndex()));
            }
        }
        return biPrices;
    }

    private static List<Price> linkToList(Linked<Price> biPrices) {
        List<Price> list = new ArrayList<>();
        Node<Price> first = biPrices.getFirst();
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
     * 分型 -：表示空点  "1-"+ MaxPrice：顶分型  "0-"+ MinPrice：低分型
     *
     * @param prices
     * @return
     */
    public static List<String> priceType(List<Price> prices) {
        List<String> bi = new ArrayList<>();
        int size = 0;
        if (null == prices || (size = prices.size()) == 0) {
            return bi;
        }
        PriceTypeEnum priceType;
        Price price;
        for (int i = 0; i < size; i++) {
            price = prices.get(i);
            priceType = price.getPriceType();
            switch (priceType) {
                case TOP:
                    bi.add("1-" + price.getTodayMaxPrice());
                    break;
                case BOTTOM:
                    bi.add("0-" + price.getTodayMinPrice());
                    break;
                default:
                    bi.add("-");
                    break;
            }
        }

        return bi;
    }

    /**
     * 笔的最小根数
     */
    private static final int BI_MIN_SIZE = 5;

    /**
     * 画笔
     *
     * @param topBottoms
     * @param prices
     */
    public static List<Segment> chanBi(List<Price> topBottoms, List<Price> prices) {
        // 找到笔的特征序列K线
        List<BiSequence> biSequences = new ArrayList<>();
        int size = topBottoms.size();
        for (int i = 0; i < size; i++) {
            if (i + 1 >= size) {
                break;
            }
            Price one = topBottoms.get(i);
            Price two = topBottoms.get(++i);
            PriceTypeEnum priceType = one.getPriceType();
            BiSequence biPrice = null;
            switch (priceType) {
                case TOP:
                    double todayMaxPrice = one.getTodayMaxPrice();
                    double todayMinPrice = two.getTodayMinPrice();
                    biPrice = BiSequence.builder()
                            .fromTradingDate(one.getTradingDate())
                            .fromIndex(one.getIndex())
                            .toTradingDate(two.getTradingDate())
                            .toIndex(two.getIndex())
                            .todayMaxPrice(todayMaxPrice)
                            .todayMinPrice(todayMinPrice)
                            .priceType(BiPriceTypeEnum.TOP)
                            .priceRunType(PriceRunTypeEnum.NONE)
                            .build();
                    break;
                case BOTTOM:
                    todayMinPrice = one.getTodayMinPrice();
                    todayMaxPrice = two.getTodayMaxPrice();
                    biPrice = BiSequence.builder()
                            .fromTradingDate(one.getTradingDate())
                            .fromIndex(one.getIndex())
                            .toTradingDate(two.getTradingDate())
                            .toIndex(two.getIndex())
                            .todayMaxPrice(todayMaxPrice)
                            .todayMinPrice(todayMinPrice)
                            .priceType(BiPriceTypeEnum.BOTTOM)
                            .priceRunType(PriceRunTypeEnum.NONE)
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
        return crtSegmentsByDuan(topBottoms, duan(biSequences, prices), prices);
    }

    /**
     * 创建线段
     *
     * @param topBottoms
     * @param prices
     * @return
     */
    private static List<Segment> crtSegmentsByDuan(List<Price> topBottoms, List<Duan> duans, List<Price> prices) {
        List<Segment> segments = new ArrayList<>();
        for (Duan duan : duans) {
            Integer from = duan.getFrom();
            Integer to = duan.getTo();
            Segment segment = new Segment();
            segment.setFromIndex(from);
            segment.setToIndex(to);
            segment.setBiPrices(getSegmentBiPrice(topBottoms, segment, prices));
            segments.add(segment);
        }
        return segments;
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
                second.setPriceRunType(PriceRunTypeEnum.UP);
                // 确认方向添加
                handledSequence.add(first);
                first = second;
                second = null;
            } else if (max < 0 && min < 0) {
                // 低低 向下
                second.setPriceRunType(PriceRunTypeEnum.DOWN);
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
                            first.setPriceRunType(PriceRunTypeEnum.UP);
                        } else {
                            first.setPriceRunType(PriceRunTypeEnum.DOWN);
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
     * 得到笔
     */
    public static void bi(List<Pair<Integer, Price>> topBottoms, List<Price> prices) {
        if (NullCheckUtils.isBlank(prices) || NullCheckUtils.isBlank(topBottoms)) {
            return;
        }
        // 潜在笔
        List<Pair<Integer, Price>> biPairs = new ArrayList<>();
        // 确定笔
        List<Pair<Integer, Price>> surePairs = new ArrayList<>();
        int topBottomsSize = topBottoms.size();
        for (int index = 0; index < topBottomsSize; index++) {
            Pair<Integer, Price> pair1 = topBottoms.get(index);
            Integer firstIndex1 = pair1.getFirst();
            Price firstPrice1 = pair1.getSecond();
            PriceTypeEnum firstPriceType1 = firstPrice1.getPriceType();
            // 直接判断下一个
            if (index + 1 == topBottomsSize) {
                continue;
            }
            // 现在已经是一顶一底了
            index = index + 1;
            Pair<Integer, Price> pair2 = topBottoms.get(index);
            Integer firstIndex2 = pair2.getFirst();
            Price firstPrice2 = pair2.getSecond();
            // 判断是否存在5根K线,确定两个坐标之间的非包含K线的数量
            int size = nonContainSize(prices, firstIndex1, firstIndex2);
            if (size >= BI_MIN_SIZE) {
                biPairs.add(new Pair<>(firstIndex1, firstPrice1));
                biPairs.add(new Pair<>(firstIndex2, firstPrice2));
                continue;
            } else {
                // 得到下一个
                index = index + 1;
                Pair<Integer, Price> pair3 = topBottoms.get(index);
                Integer secondIndex1 = pair2.getFirst();
                Price secondPrice1 = pair2.getSecond();
                // 升笔
                if (PriceTypeEnum.TOP == firstPriceType1) {

                }
                // 降笔
                if (PriceTypeEnum.BOTTOM == firstPriceType1) {

                }


            }
            // 下次循环开始 index
        }
    }

    public static void bi(List<Pair<Integer, Price>> topBottoms) {
        if (NullCheckUtils.isBlank(topBottoms)) {
            return;
        }
        try {
            int i = 0;
            Pair<Integer, Price> one = topBottoms.get(i);
            Price price = one.getSecond();
            Integer first = one.getFirst();
            PriceTypeEnum priceType = price.getPriceType();
            if (priceType == PriceTypeEnum.TOP) {
                Pair<Integer, Price> two = getNextTopBottom(topBottoms, ++i);
            }
            if (priceType == PriceTypeEnum.BOTTOM) {
                Pair<Integer, Price> two = getNextTopBottom(topBottoms, ++i);
                Pair<Integer, Price> three = getNextTopBottom(topBottoms, ++i);
                Pair<Integer, Price> four = getNextTopBottom(topBottoms, ++i);


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
    private static Pair<Integer, Price> getNextTopBottom(List<Pair<Integer, Price>> topBottoms, int index) throws Exception {
        int size = topBottoms.size();
        if (size > index + 1) {
            return topBottoms.get(index);
        }
        throw new Exception("is null");
    }

    /**
     * 确定两个坐标之间的非包含K线的数量
     *
     * @param prices
     * @param firstIndex
     * @param secondIndex
     * @return
     */
    public static int nonContainSize(List<Price> prices, Integer firstIndex, Integer secondIndex) {
        int size = 0;
        boolean isContain = false;
        for (int i = firstIndex; i <= secondIndex; i++) {
            Price price1 = prices.get(i);
            Price containPrice = price1.getContainPrice();
            if (null == containPrice) {
                size++;
                isContain = false;
                continue;
            }
            if (!isContain && null != containPrice) {
                isContain = true;
                size++;
                continue;
            }
        }
        return size;
    }

    /**
     * 获取下一个相反的分型
     *
     * @param priceType1
     * @param topBottoms
     * @param index
     * @return Pair<Integer, Pair < Integer, Price>>
     */
    private static Pair<Integer, Pair<Integer, Price>> getNextType(PriceTypeEnum priceType1, List<Pair<Integer, Price>> topBottoms, int index) {
        boolean isHaveNext = false;
        Pair<Integer, Price> target = null;
        Integer topBottomIndex = null;
        for (int i = index; i < topBottoms.size(); i++) {
            Pair<Integer, Price> pair = topBottoms.get(i);
            Price second = pair.getSecond();
            PriceTypeEnum priceType2 = second.getPriceType();
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


    /**
     * 判断是否是包含K线
     *
     * @param price
     * @return
     */
    private static boolean checkIsContain(Price price) {
        return price.getContainPrice() != null;
    }

    /**
     * 判断是否是包含K线
     *
     * @param price
     * @return
     */
    private static boolean checkIsBiContain(BiSequence price) {
        return price.getContainPrice() != null;
    }

    /**
     * 得到顶底分型
     *
     * @param prices
     * @return
     */
    public static List<Price> topBottomType(List<Price> prices) {
        List<Price> pairs = new ArrayList<>();
        int size;
        if (NullCheckUtils.isBlank(prices) || (size = prices.size()) < 3) {
            return pairs;
        }
        for (int index = 2; index < size; index++) {
            Price price = prices.get(index);
            PriceRunTypeEnum priceType = price.getPriceRunType();
            Price containPrice = price.getContainPrice();
            // 判断是否是包含K线
            if (containPrice != null && containPrice == prices.get(index - 1).getContainPrice()) {
                continue;
            }
            Pair<Integer, Integer> nodePair = twoPrePrice(prices, index);
            // 和前两根进行比较
            Integer firstPre = nodePair.getFirst();
            Integer secondPre = nodePair.getSecond();
            if (null == secondPre || null == firstPre || priceType == prices.get(firstPre).getPriceRunType()) {
                // - 表示空点
                continue;
            }
            Price firstPrePrice = prices.get(firstPre);
            // 判断当前节点是否是包含K线或和上个方向一致
            if (priceType == firstPrePrice.getPriceRunType()) {
                continue;
            }
            // 至此方向不在一致，确认分型
            Price firstPreContainPrice = firstPrePrice.getContainPrice();
            Price priceContainPrice = price.getContainPrice();
            double firstPrePriceTodayMaxPrice = firstPreContainPrice != null ? firstPreContainPrice.getTodayMaxPrice() : firstPrePrice.getTodayMaxPrice();
            double priceTodayMaxPrice = priceContainPrice != null ? priceContainPrice.getTodayMaxPrice() : price.getTodayMaxPrice();
            if (firstPrePriceTodayMaxPrice > priceTodayMaxPrice) {
                Price topPrice = topPrice(prices, firstPre, index);
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
                topPrice.setPriceType(PriceTypeEnum.TOP);
                pairs.add(topPrice);// 添加新元素
            } else {
                Price bottomPrice = bottomPrice(prices, firstPre, index);
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
                bottomPrice.setPriceType(PriceTypeEnum.BOTTOM);
                pairs.add(bottomPrice);
            }
        }
        return pairs;
    }

    /**
     * 得到顶底分型
     *
     * @param biSequences
     * @return Pair<Integer, BiPrice> Integer:biPrices的index,  BiPrice:为biPrices的index
     */
    public static List<BiSequence> biTopBottomType(List<BiSequence> biSequences) {
        List<BiSequence> types = new ArrayList<>();
        int size;
        if (NullCheckUtils.isBlank(biSequences) || (size = biSequences.size()) < 3) {
            return types;
        }
        for (int index = 2; index < size; index++) {
            BiSequence price = biSequences.get(index);
            PriceRunTypeEnum priceRunType = price.getPriceRunType();
            // 和前两根进行比较
            int firstPre = index - 1;
            // 判断当前节点是否是包含K线或和上个方向一致
            BiSequence firstPrice = biSequences.get(firstPre);
            PriceRunTypeEnum firstPriceRunType = firstPrice.getPriceRunType();
            if (priceRunType == firstPriceRunType) {
                if (index == size - 1) {
                    if (priceRunType == PriceRunTypeEnum.UP) {
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
            if (firstPriceRunType == PriceRunTypeEnum.UP) {
                firstPrice.setPriceType(BiPriceTypeEnum.TOP);
            } else {
                firstPrice.setPriceType(BiPriceTypeEnum.BOTTOM);
            }
            types.add(firstPrice);// 添加新元素
        }
        return types;
    }


    private static int getMustIndex(BiPriceTypeEnum biPriceType, List<Price> prices, BiSequence biSequences) {
        Price price = prices.get(biSequences.getFromIndex());
        boolean isTop = price.getPriceType() == PriceTypeEnum.TOP;
        int index;
        if (biPriceType == BiPriceTypeEnum.BOTTOM) {
            index = isTop ? biSequences.getToIndex() : biSequences.getFromIndex();
        } else {
            index = isTop ? biSequences.getFromIndex() : biSequences.getToIndex();
        }
        return index;
    }

    /**
     * 得到顶底分型
     *
     * @param biSequences
     * @return Pair<Integer, BiPrice> Integer:biPrices的index,  BiPrice:为biPrices的index
     */
    public static List<Duan> duan(List<BiSequence> biSequences, List<Price> prices) {
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
            PriceRunTypeEnum priceRunType = price.getPriceRunType();
            // 和前两根进行比较
            // 判断当前节点是否是包含K线或和上个方向一致
            BiSequence firstPrice = biSequences.get(index - 1);
            PriceRunTypeEnum firstPriceRunType = firstPrice.getPriceRunType();
            if (priceRunType == firstPriceRunType) {
                if (index == size - 1) {
                    if (priceRunType == PriceRunTypeEnum.UP) {
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
            if (firstPriceRunType == PriceRunTypeEnum.UP) {
                firstPrice.setPriceType(BiPriceTypeEnum.TOP);
                int mustIndex = getMustIndex(BiPriceTypeEnum.TOP, prices, firstPrice);
                duan.setTo(mustIndex);
                duan.setPriceRunType(PriceRunTypeEnum.UP);
                duans.add(duan);
                duan = new Duan();
                duan.setFrom(mustIndex);
            } else {
                firstPrice.setPriceType(BiPriceTypeEnum.BOTTOM);
                int mustIndex = getMustIndex(BiPriceTypeEnum.BOTTOM, prices, firstPrice);
                duan.setTo(mustIndex);
                duan.setPriceRunType(PriceRunTypeEnum.DOWN);
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

    /**
     * 获取最后一个item
     *
     * @param pairs
     * @return
     */
    public static Pair<Integer, Price> getLastPair(List<Pair<Integer, Price>> pairs) {
        return NullCheckUtils.isBlank(pairs) ? null : pairs.get(pairs.size() - 1);
    }

    /**
     * 获取最后一个item
     *
     * @param pairs
     * @return
     */
    public static Pair<Integer, BiSequence> getLastBiPair(List<Pair<Integer, BiSequence>> pairs) {
        return NullCheckUtils.isBlank(pairs) ? null : pairs.get(pairs.size() - 1);
    }

    /**
     * 得到所有的包含K线
     *
     * @param node
     * @param list
     */
    private static void getAllContainPrice(Node<Price> node, List<Price> list) {
        if (null == node) {
            return;
        }
        Price data = node.getData();
        Price containPrice = data.getContainPrice();
        list.add(data);
        if (containPrice == null) {
            return;
        }
        getAllContainPrice(node.getPre(), list);
    }

    private static Price topPrice(List<Price> prices, int firstPre, int index) {
        Price max = prices.get(firstPre);
        Price price;
        for (int i = firstPre + 1; i < index; i++) {
            price = prices.get(i);
            if (price.getTodayMaxPrice() > max.getTodayMaxPrice()) {
                max = price;
            }
        }
        return max;
    }

    private static BiSequence topBiPrice(List<BiSequence> biPrices, int firstPre, int index) {
        BiSequence max = biPrices.get(firstPre);
        BiSequence price;
        for (int i = firstPre + 1; i < index; i++) {
            price = biPrices.get(i);
            if (price.getTodayMaxPrice() > max.getTodayMaxPrice()) {
                max = price;
            }
        }
        return max;
    }

    private static Price bottomPrice(List<Price> prices, int firstPre, int index) {
        Price min = prices.get(firstPre);
        Price price;
        for (int i = firstPre + 1; i < index; i++) {
            price = prices.get(i);
            if (price.getTodayMinPrice() < min.getTodayMinPrice()) {
                min = price;
            }
        }
        return min;
    }

    private static BiSequence bottomBiPrice(List<BiSequence> prices, int firstPre, int index) {
        BiSequence min = prices.get(firstPre);
        BiSequence price;
        for (int i = firstPre + 1; i < index; i++) {
            price = prices.get(i);
            if (price.getTodayMinPrice() < min.getTodayMinPrice()) {
                min = price;
            }
        }
        return min;
    }


    /**
     * 获取处理完包含关系的前两根K线
     *
     * @param prices
     * @param index
     * @return
     */
    private static Pair<Integer, Integer> twoPrePrice(List<Price> prices, int index) {
        Pair<Integer, Integer> pair = new Pair<>(null, null);
        // 判断前一个是否是包含节点,如果是包含节点获取到包含节点的第一根K线
        int pre = index - 1;
        Price prePrice = prices.get(pre);
        if (checkIsContain(prePrice)) {
            pre = pre - prePrice.getContainPrice().getContainSize() + 1;
        }
        // pre == 0 说明此时只有两个节点
        if (pre == 0) {
            return pair;
        }
        // 获取第二个节点
        int secondPre = pre - 1;
        Price secondPrice = prices.get(secondPre);
        if (checkIsContain(secondPrice)) {
            secondPre = secondPre - secondPrice.getContainPrice().getContainSize() + 1;
        }
        pair.setFirst(pre);
        pair.setSecond(secondPre);
        return pair;
    }

    /**
     * 获取处理完包含关系的前两根K线
     *
     * @param prices
     * @param index
     * @return
     */
    private static Pair<Integer, Integer> twoBiPrePrice(List<BiSequence> prices, int index) {
        Pair<Integer, Integer> pair = new Pair<>(null, null);
        // 判断前一个是否是包含节点,如果是包含节点获取到包含节点的第一根K线
        int pre = index - 1;
        BiSequence prePrice = prices.get(pre);
        if (checkIsBiContain(prePrice)) {
            pre = pre - prePrice.getContainPrice().getContainSize() + 1;
        }
        // pre == 0 说明此时只有两个节点
        if (pre == 0) {
            return pair;
        }
        // 获取第二个节点
        int secondPre = pre - 1;
        BiSequence secondPrice = prices.get(secondPre);
        if (checkIsBiContain(secondPrice)) {
            secondPre = secondPre - secondPrice.getContainPrice().getContainSize() + 1;
        }
        pair.setFirst(pre);
        pair.setSecond(secondPre);
        return pair;
    }

    /**
     * 获取i 的前一个值
     *
     * @param prices
     * @param i
     * @return
     */
    private static Price getPre(List<Price> prices, int i) {
        return i > 0 ? prices.get(i - 1) : null;
    }

    /**
     * 获取i 的前一个值
     *
     * @param biPrices
     * @param i
     * @return
     */
    private static BiSequence getBiPre(List<BiSequence> biPrices, int i) {
        return i > 0 ? biPrices.get(i - 1) : null;
    }

    /**
     * 处理包含关系包含关系
     *
     * @param prices
     * @return false 不包含 true 包含
     */
    private static void handleContain(List<Price> prices) {
        int size = prices.size();
        for (int index = 0; index < size; index++) {
            Price price = prices.get(index);
            /**
             * 前一个
             */
            Price prePrice = getPre(prices, index);
            if (prePrice == null) {
                price.setPriceRunType(PriceRunTypeEnum.NONE);
                continue;
            }
            double priceMaxPrice = price.getTodayMaxPrice();
            double priceMinPrice = price.getTodayMinPrice();
            // 判断是否是包含K线
            Price preContainPrice = prePrice.getContainPrice();
            double prePriceMaxPrice = preContainPrice != null ? preContainPrice.getTodayMaxPrice() : prePrice.getTodayMaxPrice();
            double prePriceMinPrice = preContainPrice != null ? preContainPrice.getTodayMinPrice() : prePrice.getTodayMinPrice();
            double max = priceMaxPrice - prePriceMaxPrice;
            double min = priceMinPrice - prePriceMinPrice;
            if (max > 0 && min > 0) {
                // 高高 向上
                price.setPriceRunType(PriceRunTypeEnum.UP);
//                price.setBiSize(price.getBiSize() + 1);
            } else if (max < 0 && min < 0) {
                // 低低 向下
                price.setPriceRunType(PriceRunTypeEnum.DOWN);
//                price.setBiSize(price.getBiSize() + 1);
            } else {
                // 类型为包含
                PriceRunTypeEnum priceType = prePrice.getPriceRunType();
                price.setPriceRunType(priceType);
                // 包含类
                Price containPrice = preContainPrice != null ? preContainPrice : new Price();
                // 计算k线包含数量
                containPrice.setContainSize(getContainSize(preContainPrice));
                if (priceType == PriceRunTypeEnum.UP) {
                    // 向上 包含
                    contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max > 0, min > 0, PriceRunTypeEnum.UP);
                } else if (priceType == PriceRunTypeEnum.DOWN) {
                    // 向下 包含
                    contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max < 0, min < 0, PriceRunTypeEnum.DOWN);
                }
                price.setContainPrice(containPrice);
                prePrice.setContainPrice(containPrice);
            }
        }
    }

    private static void contain(double priceMaxPrice, double priceMinPrice, double prePriceMaxPrice, double prePriceMinPrice, Price containPrice, boolean b, boolean b2, PriceRunTypeEnum up) {
        containPrice.setTodayMaxPrice(b ? priceMaxPrice : prePriceMaxPrice);
        containPrice.setTodayMinPrice(b2 ? priceMinPrice : prePriceMinPrice);
        containPrice.setPriceRunType(up);
    }

    private static void biContain(double priceMaxPrice, double priceMinPrice, double prePriceMaxPrice, double prePriceMinPrice, BiSequence containPrice, boolean b, boolean b2, PriceRunTypeEnum up) {
        containPrice.setTodayMaxPrice(b ? priceMaxPrice : prePriceMaxPrice);
        containPrice.setTodayMinPrice(b2 ? priceMinPrice : prePriceMinPrice);
        containPrice.setPriceRunType(up);
    }

    /**
     * 从当前确定和前面包含的K线算确定当前一共包含了几根K线
     *
     * @param preContainPrice
     * @return
     */
    private static int getContainSize(Price preContainPrice) {
        return preContainPrice == null ? 2 : preContainPrice.getContainSize() + 1;
    }

    /**
     * 从当前确定和前面包含的K线算确定当前一共包含了几根K线
     *
     * @param preContainPrice
     * @return
     */
    private static int getBiContainSize(BiSequence preContainPrice) {
        return preContainPrice == null ? 2 : preContainPrice.getContainSize() + 1;
    }
}