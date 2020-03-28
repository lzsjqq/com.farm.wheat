package com.farm.wheat.share.chan.util;

import com.farm.common.utils.NullCheckUtils;

import java.util.*;

/**
 * @program: wheat
 * @description: 用于构造处理链表
 * @author: xyc
 * @create: 2020-03-13 21:49
 */
public class ChanLunUtil {
    private ChanLunUtil() {
    }

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
//        bi(pairs, prices);
        List<Segment> segments = chanBi(topBottoms, prices);
//        Map<Integer, BiPrice> biMap = null;
//        if (NullCheckUtils.isBlank(biMap)) {
//            return prices;
//        }
        //
        Set<Integer> huaBiIndex = huaBi(segments);
        for (int i = 0; i < prices.size(); i++) {
            if (!huaBiIndex.contains(i)) {
                prices.get(i).setPriceType(PriceTypeEnum.NONE);
            }
        }
        return prices;
    }

    /**
     * 画笔
     *
     * @param segments
     * @return
     */
    private static Set<Integer> huaBi(List<Segment> segments) {
        Set<Integer> biTopBottoms = new HashSet<>();
        List<Bi> bis = new ArrayList<>();
        for (Segment segment : segments) {
            Linked<Price> biPrices = segment.getBiPrices();
            int size = biPrices.getSize();

            if (size <= 2) {
                biTopBottoms.add(biPrices.get(0).getIndex());
                biTopBottoms.add(biPrices.get(1).getIndex());
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
                    if (j <= size - 1) {
                        biTopBottoms.add(fromIndex);
                        continue;
                        // 什么都不做了
                    } else {
                        Triple<Price, Price, Integer> confirmBiPrice = confirmBiPrice(biTopBottoms, biPrices, fromIndex, j);
                        j += confirmBiPrice.getThird();
                        fromIndex = null;
                    }
                } else {
                    // 第一笔大于5
                    Triple<Price, Price, Integer> confirmBiPrice = confirmBiPrice(biTopBottoms, biPrices, fromIndex, j);
                    j += confirmBiPrice.getThird();
                    fromIndex = null;
                }
            }
        }
        return biTopBottoms;
    }

    private static Triple<Price, Price, Integer> confirmBiPrice(Set<Integer> biTopBottoms, Linked<Price> biPrices, Integer fromIndex, int j) {
        Triple<Price, Price, Integer> confirmBiPrice = getConfirmBiPrice(biPrices, j + 1, 0);
        confirmBiPrice.getFirst();
        if (null != confirmBiPrice.getFirst()) {
            biTopBottoms.add(fromIndex);
            biTopBottoms.add(confirmBiPrice.getFirst().getIndex());
        }
        if (null != confirmBiPrice.getSecond()) {
            biTopBottoms.add(confirmBiPrice.getSecond().getIndex());
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
    private static Triple<Price, Price, Integer> getConfirmBiPrice(Linked<Price> biPrices, int index, int incr) {

        int size = biPrices.getSize();
        int nowIndex = incr + index;
        if (size - 1 < nowIndex) {
            return new Triple<>(null, null, index + 1);
        }
        Node<Price> now = biPrices.getNode(nowIndex);
        Price nowPrice = now.getData();

        // 判断当前是否是5个
        int fromToSize = nowPrice.getFromToSize();
        Node<Price> next = now.getNext();
        Price nextPrice = next.data;
        if (fromToSize >= 5 && nextPrice.getFromToSize() >= 5) {
            return new Triple<>(nowPrice, nextPrice, index + 1);
        }

        if (fromToSize >= 5 && nextPrice.getFromToSize() < 5) {
            if (next.getNext() == null) {
                return new Triple<>(biPrices.get(nowIndex + 2), null, size - 1);
            } else {
                return new Triple<>(nowPrice, nextPrice, index + 1);
            }
        }

        if (fromToSize < 5 && nowIndex + 2 < size) {
            return getConfirmBiPrice(biPrices, index + 2, 0);
        }
        // 判断是否是最后一个
        if (nowIndex + 2 >= size - 1) {
            return new Triple<>(biPrices.get(size - 1), null, size - 1);
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


    private static List<Segment> crtSegments(List<Price> topBottoms, List<Pair<Integer, BiSequence>> sequences, List<Price> prices) {
        List<Segment> segments = new ArrayList<>();
        // 初始化
        Segment segment = new Segment();
        BiSequence sequence;
        for (Pair<Integer, BiSequence> pair : sequences) {
            sequence = pair.getSecond();
            BiSequence second = sequence;
            if (second.getPriceType() == BiPriceTypeEnum.TOP) {
                Integer toIndex = sequence.getToIndex();
                if (segment.getFromIndex() == null) {
                    segment.setFromIndex(toIndex);
                }

                if (null == segment.getToIndex()) {
                    segment.setToIndex(toIndex);
                }
                segments.add(segment);
                // 段之间的顶低分型
                segment.setBiPrices(getSegmentBiPrice(topBottoms, segment, prices));
                segment = new Segment();
                segment.setFromIndex(toIndex);
                continue;
            }
            if (second.getPriceType() == BiPriceTypeEnum.BOTTOM) {
                Integer fromIndex = second.getFromIndex();
                if (segment.getToIndex() == null) {
                    segment.setToIndex(fromIndex);
                }
                segments.add(segment);
                // 段之间的顶低分型
                segment.setBiPrices(getSegmentBiPrice(topBottoms, segment, prices));
                segment = new Segment();
                segment.setFromIndex(fromIndex);
                continue;
            }
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
    private static Linked<Price> getSegmentBiPrice(List<Price> topBottoms, Segment segment, List<Price> prices) {
        Integer fromIndex = segment.getFromIndex();
        Integer toIndex = segment.getToIndex();
        Linked<Price> biPrices = new Linked<>();
        Price price;
        Node<Price> last;
        Price pre;
        for (int i = 0; i < topBottoms.size(); i++) {
            price = topBottoms.get(i);
            Integer first = price.getIndex();
            if (fromIndex <= first && first <= toIndex) {
                // 处理两个顶底分型的K线数量
                biPrices.add(price);
                last = biPrices.getNode(biPrices.getSize() - 1);
                if (1 == biPrices.getSize()) {
                    continue;
                }
                pre = last.getPre().data;
                // 计算两者之间的K线数量
                pre.setFromToSize(nonContainSize(prices, pre.getIndex(), last.data.getIndex()));
            }
        }
        return biPrices;
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
                    double todayMaxPrice = one.getContainPrice() != null ? one.getContainPrice().getTodayMaxPrice() : one.getTodayMaxPrice();
                    double todayMinPrice = two.getContainPrice() != null ? two.getTodayMinPrice() : two.getTodayMinPrice();
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
                    todayMinPrice = one.getContainPrice() != null ? one.getTodayMinPrice() : one.getTodayMinPrice();
                    todayMaxPrice = two.getContainPrice() != null ? two.getTodayMinPrice() : two.getTodayMinPrice();
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
        handleSequencesContain(biSequences);
        // 得到顶底分型
        List<Segment> segments = new ArrayList<>();
        segments.addAll(crtSegments(topBottoms, biTopBottomType(biSequences), prices));
//        for (int i = 1; i < size; i++) {
//            if (i + 1 >= size) {
//                break;
//            }
//            Price one = topBottoms.get(i).getSecond();
//            Price two = topBottoms.get(++i).getSecond();
//            PriceTypeEnum priceType = one.getPriceType();
//            BiPrice biPrice = null;
//            switch (priceType) {
//                case TOP:
//                    double todayMaxPrice = one.getContainPrice() != null ? one.getContainPrice().getTodayMaxPrice() : one.getTodayMaxPrice();
//                    double todayMinPrice = two.getContainPrice() != null ? two.getTodayMinPrice() : two.getTodayMinPrice();
//                    biPrice = BiPrice.builder()
//                            .fromTradingDate(one.getTradingDate())
//                            .toTradingDate(two.getTradingDate())
//                            .todayMaxPrice(todayMaxPrice)
//                            .todayMinPrice(todayMinPrice)
//                            .priceType(BiPriceTypeEnum.TOP)
//                            .priceRunType(PriceRunTypeEnum.NONE)
//                            .build();
//                    break;
//                case BOTTOM:
//                    todayMinPrice = one.getContainPrice() != null ? one.getTodayMinPrice() : one.getTodayMinPrice();
//                    todayMaxPrice = two.getContainPrice() != null ? two.getTodayMinPrice() : two.getTodayMinPrice();
//                    biPrice = BiPrice.builder()
//                            .fromTradingDate(one.getTradingDate())
//                            .toTradingDate(two.getTradingDate())
//                            .todayMaxPrice(todayMaxPrice)
//                            .todayMinPrice(todayMinPrice)
//                            .priceType(BiPriceTypeEnum.DOWN)
//                            .priceRunType(PriceRunTypeEnum.NONE)
//                            .build();
//                    break;
//                default:
//                    break;
//            }
//            twoTopBottoms.add(biPrice);
//        }
//        // 做包含处理
//        biHandleContain(twoTopBottoms);
//        // 得到顶底分型
//        biMap.putAll(biMap(biTopBottomType(twoTopBottoms)));
        return segments;
    }

    /**
     * 处理包含关系
     *
     * @param biPrices
     */
    private static void handleSequencesContain(List<BiSequence> biPrices) {
        int size = biPrices.size();
        for (int index = 0; index < size; index++) {
            BiSequence price = biPrices.get(index);
            /**
             * 前一个
             */
            BiSequence prePrice = getBiPre(biPrices, index);
            if (prePrice == null) {
                continue;
            }
            double priceMaxPrice = price.getTodayMaxPrice();
            double priceMinPrice = price.getTodayMinPrice();
            // 判断是否是包含K线
            BiSequence preContainPrice = prePrice.getContainPrice();
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
                BiSequence containPrice = preContainPrice != null ? preContainPrice : new BiSequence();
                // 计算k线包含数量
                containPrice.setContainSize(getBiContainSize(preContainPrice));
                if (priceType == PriceRunTypeEnum.UP) {
                    // 向上 包含
                    biContain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max > 0, min > 0, PriceRunTypeEnum.UP);
                } else if (priceType == PriceRunTypeEnum.DOWN) {
                    // 向下 包含
                    biContain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max < 0, min < 0, PriceRunTypeEnum.DOWN);
                }
                price.setContainPrice(containPrice);
                prePrice.setContainPrice(containPrice);
            }
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
     * @param biPrices
     * @return Pair<Integer, BiPrice> Integer:biPrices的index,  BiPrice:为biPrices的index
     */
    public static List<Pair<Integer, BiSequence>> biTopBottomType(List<BiSequence> biPrices) {
        List<Pair<Integer, BiSequence>> pairs = new ArrayList<>();
        int size;
        if (NullCheckUtils.isBlank(biPrices) || (size = biPrices.size()) < 3) {
            return pairs;
        }
        for (int index = 2; index < size; index++) {
            BiSequence price = biPrices.get(index);
            PriceRunTypeEnum priceType = price.getPriceRunType();
            BiSequence containPrice = price.getContainPrice();
            // 判断是否是包含K线
            if (containPrice != null && containPrice == biPrices.get(index - 1).getContainPrice()) {
                continue;
            }
            Pair<Integer, Integer> nodePair = twoBiPrePrice(biPrices, index);
            // 和前两根进行比较
            Integer firstPre = nodePair.getFirst();
            Integer secondPre = nodePair.getSecond();
            if (null == secondPre || null == firstPre || priceType == biPrices.get(firstPre).getPriceRunType()) {
                // - 表示空点
                continue;
            }
            BiSequence firstPrePrice = biPrices.get(firstPre);
            // 判断当前节点是否是包含K线或和上个方向一致
            if (priceType == firstPrePrice.getPriceRunType()) {
                continue;
            }
            // 至此方向不在一致，确认分型
            BiSequence firstPreContainPrice = firstPrePrice.getContainPrice();
            BiSequence priceContainPrice = price.getContainPrice();
            double firstPrePriceTodayMaxPrice = firstPreContainPrice != null ? firstPreContainPrice.getTodayMaxPrice() : firstPrePrice.getTodayMaxPrice();
            double priceTodayMaxPrice = priceContainPrice != null ? priceContainPrice.getTodayMaxPrice() : price.getTodayMaxPrice();
            if (firstPrePriceTodayMaxPrice > priceTodayMaxPrice) {
                Pair<Integer, BiSequence> pair = topBiPrice(biPrices, firstPre, index);
                // 判断上个分型是否是顶分型，如果是去掉，只保留最高的，
//                Pair<Integer, BiPrice> last = getLastBiPair(pairs);
//                if (last != null && BiPriceTypeEnum.TOP == last.getSecond().getPriceType()) {
//                    last.getSecond().setPriceType(BiPriceTypeEnum.NONE);
//                    pairs.remove(pairs.size() - 1);
//                }
                pair.getSecond().setPriceType(BiPriceTypeEnum.TOP);
                pairs.add(pair);// 添加新元素
            } else {
                Pair<Integer, BiSequence> pair = bottomBiPrice(biPrices, firstPre, index);
                // 判断上个分型是否是底分型，如果是去掉，只保留最底的，
//                Pair<Integer, BiPrice> last = getLastBiPair(pairs);
//                if (last != null && BiPriceTypeEnum.BOTTOM == last.getSecond().getPriceType()) {
//                    last.getSecond().setPriceType(BiPriceTypeEnum.NONE);
//                    pairs.remove(pairs.size() - 1);
//                }
                pair.getSecond().setPriceType(BiPriceTypeEnum.BOTTOM);
                pairs.add(pair);
            }
        }
        return pairs;
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
        Pair<Integer, Price> pair = new Pair<>();
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

    private static Pair<Integer, BiSequence> topBiPrice(List<BiSequence> biPrices, int firstPre, int index) {
        Pair<Integer, BiSequence> pair = new Pair<>();
        BiSequence max = biPrices.get(firstPre);
        BiSequence price;
        Integer maxIndex = firstPre;
        for (int i = firstPre + 1; i < index; i++) {
            price = biPrices.get(i);
            if (price.getTodayMaxPrice() > max.getTodayMaxPrice()) {
                max = price;
                maxIndex = i;
            }
        }
        pair.setFirst(maxIndex);
        pair.setSecond(max);
        return pair;
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

    private static Pair<Integer, BiSequence> bottomBiPrice(List<BiSequence> prices, int firstPre, int index) {
        Pair<Integer, BiSequence> pair = new Pair<>();
        BiSequence min = prices.get(firstPre);
        Integer minIndex = firstPre;
        BiSequence price;
        for (int i = firstPre + 1; i < index; i++) {
            price = prices.get(i);
            if (price.getTodayMinPrice() < min.getTodayMinPrice()) {
                min = price;
                minIndex = i;
            }
        }
        pair.setFirst(minIndex);
        pair.setSecond(min);
        return pair;
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