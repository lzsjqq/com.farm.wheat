package com.farm.wheat.share.chan.util;

import com.farm.common.utils.Node;
import com.farm.common.utils.NullCheckUtils;
import com.farm.common.utils.Pair;
import com.farm.wheat.share.chan.dto.BiUtil;
import com.farm.wheat.share.chan.dto.ContainedKLine;
import com.farm.wheat.share.chan.dto.FenXing;
import com.farm.wheat.share.chan.dto.FenXingUtil;
import com.farm.wheat.share.chan.dto.KLine;
import com.farm.wheat.share.chan.dto.KLineUtil;
import com.farm.wheat.share.chan.dto.XinBi;

import java.util.ArrayList;
import java.util.List;


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
     * 构建链表
     *
     * @return
     */
    public static List<KLine> buildLined(List<KLine> KLines) throws Exception {
        // 处理包含关系包含关系
        KLineUtil.handleContain(KLines);
        // 处理顶底分型,过滤掉连续的同类型分型只保留最高或最低的
        List<KLine> topBottoms = FenXingUtil.topBottomType(KLines);
        List<Segment> segments = BiUtil.chanBi(topBottoms, KLines);
        List<Bi> bis = huaBi2(segments);
        List<Integer> huaBiIndex = getHuaBiIndex(bis);
//        List<Integer> huaBiIndex = huaBi(segments, prices);
        for (int i = 0; i < KLines.size(); i++) {
            if (!huaBiIndex.contains(i)) {
                KLines.get(i).setKType(FengXingTypeEnum.NONE);
            }
        }
        return KLines;
    }


    /**
     * 构建分型
     *
     * @return
     */
    public static List<FenXing> buildFengXing(List<KLine> KLines) throws Exception {
        // 处理包含关系包含关系
        KLineUtil.handleContain(KLines);
        // 获取包含处理后的K线
        List<ContainedKLine> containedKLineList = KLineUtil.buildContained(KLines);
        return FenXingUtil.fenXingSimpleHandle(containedKLineList);
    }

    /**
     * 构建分型
     *
     * @return
     */
    public static List<XinBi> buildBi(List<KLine> KLines) throws Exception {
        // 处理包含关系包含关系
        KLineUtil.handleContain(KLines);
        // 获取包含处理后的K线
        List<ContainedKLine> containedKLineList = KLineUtil.buildContained(KLines);
        List<FenXing> fenXingList = FenXingUtil.fenXingSimpleHandle(containedKLineList);
        List<XinBi> xinBiList = BiUtil.biHandle(fenXingList);

        return xinBiList;
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
    public static List<KLine> removeTogether(List<KLine> topBottoms, int beginIndex) {
        int size;
        if (NullCheckUtils.isBlank(topBottoms) || ((size = topBottoms.size()) - beginIndex) <= 2) {
            return topBottoms;
        }
        List<KLine> newTopBottoms = new ArrayList<>();
        KLine first = null;
        KLine second = null;
        KLine third = null;
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
    private static boolean moreCondition(KLine first, KLine third) {
        boolean condition;
        if (first.getKType() == FengXingTypeEnum.BOTTOM) {
            double nextTodayMinPrice = third.getContainKLine() == null ? third.getMinPrice() : third.getContainKLine().getMinPrice();
            double preTodayMinPrice = first.getContainKLine() == null ? first.getMinPrice() : first.getContainKLine().getMinPrice();
            condition = nextTodayMinPrice - preTodayMinPrice < 0;
        } else {
            double nextTodayMaxPrice = third.getContainKLine() == null ? third.getMaxPrice() : third.getContainKLine().getMaxPrice();
            double preTodayMaxPrice = first.getContainKLine() == null ? first.getMaxPrice() : first.getContainKLine().getMaxPrice();
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
     * @param biKLines
     * @return
     */
    private static KLine getLastPrice(List<KLine> biKLines) {
        return biKLines.get(biKLines.size() - 1);
    }

    /**
     * 获取下一个值
     *
     * @param biKLines
     * @return
     */
    private static KLine getNextPrice(List<KLine> biKLines, Integer nowIndex) {
        return biKLines.size() - 1 > nowIndex ? biKLines.get(nowIndex + 1) : null;
    }

    /**
     * 画笔
     *
     * @param segments
     * @return
     */
    private static List<Integer> huaBi(List<Segment> segments, List<KLine> KLines) {
        List<Integer> biTopBottoms = new ArrayList<>();
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            List<KLine> biKLines = segment.getBiKLines();
            int size = biKLines.size();

            if (size <= 2) {
                addBiTopBottomsList(biTopBottoms, biKLines.get(0).getIndex());
                addBiTopBottomsList(biTopBottoms, biKLines.get(1).getIndex());
                continue;
            }

            // 第一个笔是否成型
            Integer fromIndex = null;
            for (int j = 0; j < size; j++) {
                KLine KLine = biKLines.get(j);
                if (NullCheckUtils.isBlank(fromIndex)) {
                    fromIndex = KLine.getIndex();
                }
                if (KLine.getFromToSize() < 5) {
                    j += 3;
                    // 判断
                    if (j >= size - 1) {
                        addBiTopBottomsList(biTopBottoms, fromIndex);
                        addBiTopBottomsList(biTopBottoms, getLastPrice(biKLines).getIndex());
                        continue;
                        // 什么都不做了
                    } else {
                        Triple<KLine, KLine, Integer> confirmBiPrice = confirmBiPrice(biTopBottoms, biKLines, fromIndex, j);
                        j = confirmBiPrice.getThird();
                        fromIndex = null;
                    }
                } else {
                    // 第一笔大于5
                    Triple<KLine, KLine, Integer> confirmBiPrice = confirmBiPrice(biTopBottoms, biKLines, fromIndex, j);
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
            List<KLine> biKLines = segment.getBiKLines();
            int size = biKLines.size();
            if (size <= 2) {
                justTwo(confirmBis, biKLines);
                continue;
            }
            // 第一个笔是否成型
            List<Bi> unConfirmOneBis = getUnConfirmBis(biKLines, size, 0);
            List<Bi> unConfirmTwoBis = getUnConfirmBis(biKLines, size, 1);
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
                setBiFrom(bi, one.getFromKLine());
            }
            if (i <= unConfirmTwoBis.size() - 1) {
                Bi tow = unConfirmTwoBis.get(i);
                // 小于5说明，反向笔没有确定
                if (tow.getFromKLine().getFromToSize() < 5) {
                    continue;
                }
                // 大于5,反向笔确定
                // 判断是否为此时的i+1不是最后一个待定笔
                if (i + 1 < unConfirmOneBis.size() - 1) {
                    // 第一笔
                    setBiTo(bi, one.getToKLine());
                    // 添加确定笔
                    confirmBis.add(bi);
                    // 第二笔
                    bi = new Bi();
                    setBiFrom(bi, tow.getFromKLine());
                    setBiTo(bi, tow.getToKLine());
                    confirmBis.add(bi);
                    // 第三笔
                    bi = null;
                    continue;
                }
                // 判断是否为此时的i+1是最后一个待定笔且是最后一个待定笔时不足五根
                if (i + 1 == unConfirmOneBis.size() - 1 && unConfirmOneBis.get(i + 1).getFromKLine().getFromToSize() < 5) {
                    // 第一笔
                    setBiTo(bi, unConfirmOneBis.get(i + 1).getToKLine());
                    // 添加确定笔
                    confirmBis.add(bi);
                    break;
                }
                // 判断是否为此时的i+1是最后一个待定笔且是最后一个待定笔时足五根
                if (i + 1 == unConfirmOneBis.size() - 1 && unConfirmOneBis.get(i + 1).getFromKLine().getFromToSize() >= 5) {
                    // 第一笔
                    setBiTo(bi, one.getToKLine());
                    // 添加确定笔
                    confirmBis.add(bi);
                    // 第二笔
                    bi = new Bi();
                    setBiFrom(bi, one.getToKLine());
                    setBiTo(bi, tow.getToKLine());
                    confirmBis.add(bi);
                    // 第三笔
                    bi = new Bi();
                    setBiFrom(bi, tow.getToKLine());
                    setBiTo(bi, unConfirmOneBis.get(i + 1).getToKLine());
                    confirmBis.add(bi);
                    break;
                }


            } else {
                // 此时走到最后
                setBiTo(bi, one.getToKLine());
                // 添加确定笔
                confirmBis.add(bi);
                break;
            }

        }
        return confirmBis;
    }

    private static List<Bi> getUnConfirmBis(List<KLine> biKLines, int size, int i2) {
        List<Bi> unConfirmBis = new ArrayList<>(size / 2);
        for (int j = i2; j + 1 < size; j += 2) {
            Bi bi = new Bi();
            setBiFrom(bi, biKLines.get(j));
            setBiTo(bi, biKLines.get(j + 1));
            unConfirmBis.add(bi);
        }
        return unConfirmBis;
    }

    private static void setBiFrom(Bi bi, KLine KLine) {
        bi.setFromIndex(KLine.getIndex());
        bi.setFromKLine(KLine);
    }

    private static void justTwo(List<Bi> bis, List<KLine> biKLines) {
        Bi bi = new Bi();
        KLine KLine = biKLines.get(0);
        setBiFrom(bi, KLine);
        KLine KLine1 = biKLines.get(1);
        setBiTo(bi, KLine1);
        bis.add(bi);
    }

    private static void lastTwo(int index, List<Bi> bis, List<KLine> biKLines) {
        Bi bi = new Bi();
        KLine KLine = biKLines.get(index);
        setBiFrom(bi, KLine);
        KLine lastKLine = getLastPrice(biKLines);
        setBiTo(bi, lastKLine);
        bis.add(bi);
    }

    private static void lastTwo(Bi bi, List<Bi> bis, List<KLine> biKLines) {
        KLine lastKLine = getLastPrice(biKLines);
        setBiTo(bi, lastKLine);
        bis.add(bi);
    }

    private static void setBiTo(Bi bi, KLine lastKLine) {
        bi.setToIndex(lastKLine.getIndex());
        bi.setToKLine(lastKLine);
    }

    /**
     * @param biTopBottoms
     * @param biKLines
     * @param fromIndex
     * @param j
     * @return Triple<Price, Price, Integer> Integer 增量
     */
    private static Triple<KLine, KLine, Integer> confirmBiPrice(List<Integer> biTopBottoms, List<KLine> biKLines, Integer fromIndex, int j) {
        Triple<KLine, KLine, Integer> confirmBiPrice = getConfirmBiPrice(biKLines, j + 1, 0);
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
     * @param biKLines
     * @param j
     * @return Triple<Price, Price, Integer> Integer 增量
     */
    private static Triple<KLine, KLine, Integer> confirmBiPrice(List<Bi> bis, List<KLine> biKLines, Bi unConfirmBi, int j) {
        Triple<KLine, KLine, Integer> confirmBiPrice = getConfirmBi(biKLines, j, 0);
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
     * @param biKLines
     * @param index
     * @param incr
     * @return
     */
    private static Triple<KLine, KLine, Integer> getConfirmBi(List<KLine> biKLines, int index, int incr) {
        int size = biKLines.size();
        int lastIndex = size - 1;
        int nowIndex = incr + index;
        if (lastIndex <= nowIndex) {
            return new Triple<>(getLastPrice(biKLines), null, lastIndex);
        }
        KLine nowKLine = biKLines.get(nowIndex);
        // 判断当前是否是5个
        int fromToSize = nowKLine.getFromToSize();
        KLine nextKLine = getNextPrice(biKLines, nowIndex);
        int nextFromToSize = nextKLine.getFromToSize();
        // 都大于5，确定两笔
        if (fromToSize >= 5 && nextFromToSize >= 5) {
            int nextIndex = nowIndex + 1;
            return new Triple<>(nowKLine, nextKLine, nextIndex);
        }

        if (fromToSize >= 5 && nextFromToSize < 5) {
            if (getNextPrice(biKLines, nowIndex + 1) == null) {
                return new Triple<>(getLastPrice(biKLines), null, lastIndex);
            } else {
                return new Triple<>(null, null, nowIndex + 1);
            }
        }

        if (fromToSize < 5 && nowIndex + 2 <= lastIndex) {
            return getConfirmBiPrice(biKLines, nowIndex + 2, 0);
        }
        // 判断是否是最后一个
        if (nowIndex + 2 >= lastIndex) {
            return new Triple<>(biKLines.get(lastIndex), null, lastIndex);
        }
        return getConfirmBiPrice(biKLines, index, 2);

    }

    /**
     * 获取成笔price
     *
     * @param biKLines
     * @param index
     * @param incr
     * @return
     */
    private static Triple<KLine, KLine, Integer> getConfirmBiPrice(List<KLine> biKLines, int index, int incr) {

        int size = biKLines.size();
        int lastIndex = size - 1;
        int nowIndex = incr + index;
        if (lastIndex <= nowIndex) {
            return new Triple<>(getLastPrice(biKLines), null, lastIndex);
        }
        KLine nowKLine = biKLines.get(nowIndex);
        // 判断当前是否是5个
        int fromToSize = nowKLine.getFromToSize();
        KLine nextKLine = getNextPrice(biKLines, nowIndex);
        int nextFromToSize = nextKLine.getFromToSize();
        if (fromToSize >= 5 && nextFromToSize >= 5) {
            return new Triple<>(nowKLine, nextKLine, index + 1);
        }

        if (fromToSize >= 5 && nextFromToSize < 5) {
            if (getNextPrice(biKLines, nowIndex + 1) == null) {
                return new Triple<>(getLastPrice(biKLines), null, lastIndex);
            } else {
                return new Triple<>(null, null, nowIndex + 1);
            }
        }

        if (fromToSize < 5 && nowIndex + 2 <= lastIndex) {
            return getConfirmBiPrice(biKLines, nowIndex + 2, 0);
        }
        // 判断是否是最后一个
        if (nowIndex + 2 >= lastIndex) {
            return new Triple<>(biKLines.get(lastIndex), null, lastIndex);
        }
        return getConfirmBiPrice(biKLines, index, 2);

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
     * 分型 -：表示空点  "1-"+ MaxPrice：顶分型  "0-"+ MinPrice：低分型
     *
     * @param KLines
     * @return
     */
    public static List<String> priceType(List<KLine> KLines) {
        List<String> bi = new ArrayList<>();
        int size = 0;
        if (null == KLines || (size = KLines.size()) == 0) {
            return bi;
        }
        FengXingTypeEnum priceType;
        KLine KLine;
        for (int i = 0; i < size; i++) {
            KLine = KLines.get(i);
            priceType = KLine.getKType();
            switch (priceType) {
                case TOP:
                    bi.add("1-" + KLine.getMaxPrice());
                    break;
                case BOTTOM:
                    bi.add("0-" + KLine.getMinPrice());
                    break;
                default:
                    bi.add("-");
                    break;
            }
        }

        return bi;
    }

    /**
     * 分型 -：表示空点  "1-"+ MaxPrice：顶分型  "0-"+ MinPrice：低分型
     *
     * @param KLines
     * @return
     */
    public static void setKType(List<KLine> KLines, List<XinBi> xinBiList) {
        if (NullCheckUtils.isBlank(xinBiList) || NullCheckUtils.isBlank(KLines)) {
            return;
        }

        for (XinBi xinBi : xinBiList) {
            if(!xinBi.isZhuanZhe()){
                continue;
            }
            FenXing startFenXing = xinBi.getStartFenXing();
            FenXing endFenXing = xinBi.getEndFenXing();
            FengXingTypeEnum startFenXingTypeEnum = startFenXing.getFenXingTypeEnum();
            FengXingTypeEnum endFenXingTypeEnum = endFenXing.getFenXingTypeEnum();
            FengXingTypeEnum startKType = startFenXing.getFenXingTypeEnum();
            FengXingTypeEnum endKType = endFenXing.getFenXingTypeEnum();
            KLines.get(FengXingTypeEnum.TOP == startKType ? startFenXing.getMiddleLine().getMaxIndex() : startFenXing.getMiddleLine().getMinIndex()).setKType(startKType);
            KLines.get(FengXingTypeEnum.TOP == endKType ? endFenXing.getMiddleLine().getMaxIndex() : endFenXing.getMiddleLine().getMinIndex()).setKType(endKType);
        }
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
            RunTypeEnum priceRunType = price.getPriceRunType();
            // 和前两根进行比较
            int firstPre = index - 1;
            // 判断当前节点是否是包含K线或和上个方向一致
            BiSequence firstPrice = biSequences.get(firstPre);
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
            } else {
                firstPrice.setPriceType(BiPriceTypeEnum.BOTTOM);
            }
            types.add(firstPrice);// 添加新元素
        }
        return types;
    }


    /**
     * 获取最后一个item
     *
     * @param pairs
     * @return
     */
    public static Pair<Integer, KLine> getLastPair(List<Pair<Integer, KLine>> pairs) {
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
    private static void getAllContainPrice(Node<KLine> node, List<KLine> list) {
        if (null == node) {
            return;
        }
        KLine data = node.getData();
        KLine containKLine = data.getContainKLine();
        list.add(data);
        if (containKLine == null) {
            return;
        }
        getAllContainPrice(node.getPre(), list);
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
     * @param biPrices
     * @param i
     * @return
     */
    private static BiSequence getBiPre(List<BiSequence> biPrices, int i) {
        return i > 0 ? biPrices.get(i - 1) : null;
    }


    private static void biContain(double priceMaxPrice, double priceMinPrice, double prePriceMaxPrice, double prePriceMinPrice, BiSequence containPrice, boolean b, boolean b2, RunTypeEnum up) {
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
    private static int getBiContainSize(BiSequence preContainPrice) {
        return preContainPrice == null ? 2 : preContainPrice.getContainSize() + 1;
    }
}