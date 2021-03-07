package com.farm.wheat.share.chan.dto;

import com.farm.common.utils.Linked;
import com.farm.common.utils.Node;
import com.farm.common.utils.NullCheckUtils;
import com.farm.common.utils.Pair;
import com.farm.wheat.share.chan.util.BiPriceTypeEnum;
import com.farm.wheat.share.chan.util.BiRunTypeEnum;
import com.farm.wheat.share.chan.util.BiSequence;
import com.farm.wheat.share.chan.util.Duan;
import com.farm.wheat.share.chan.util.FengXingTypeEnum;
import com.farm.wheat.share.chan.util.RunTypeEnum;
import com.farm.wheat.share.chan.util.Segment;

import java.util.ArrayList;
import java.util.Collections;
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
     * 由分型得到笔
     * 1.从第一个分型开始，到下一个分型之间是否具有5根K线
     *
     * @param fenXingList
     * @return
     */
    public static List<XinBi> biSimpleHandle(List<FenXing> fenXingList) {
        if (NullCheckUtils.isBlank(fenXingList)) {
            return null;
        }
        int fenXingSize = fenXingList.size();
        List<XinBi> xinBiList = new ArrayList<>();

        FenXing firstFenXing = fenXingList.get(0);
        FenXing secondFenXing;
        for (int index = 1; index < fenXingSize; index++) {
            XinBi xinBi = new XinBi();
            secondFenXing = fenXingList.get(index);
            xinBi.setStartFenXing(firstFenXing);
            xinBi.setEndFenXing(secondFenXing);
            xinBi.setKSize(kLineSize(firstFenXing, secondFenXing));
            xinBi.setBiRunTypeEnum(getBiRunTypeEnum(firstFenXing));
            xinBi.setMaxPrice(getMaxPrice(firstFenXing, secondFenXing));
            xinBi.setMinPrice(getMinPrice(firstFenXing, secondFenXing));
            xinBiList.add(xinBi);
            firstFenXing = secondFenXing;
        }
        return xinBiList;
    }

    /**
     * 由分型得到笔
     * 1.从第一个分型开始，到下一个分型之间是否具有5根K线
     *
     * @param fenXingList
     * @return
     */
    public static List<XinBi> biHandle(List<FenXing> fenXingList) {
        List<XinBi> xinBiSimpleHandle = biSimpleHandle(fenXingList);
        List<XinBi> sequence = sequence(xinBiSimpleHandle);
        List<Integer> zhuanZhe = zhuanZhe(sequence);
        if(NullCheckUtils.isNotBlank(zhuanZhe)){
            for (Integer zhuan : zhuanZhe) {
                sequence.get(zhuan).setZhuanZhe(true);
            }
        }
        return sequence;
    }

    /**
     * @param biList
     */
    private static void dd(Linked<XinBi> biList) {
        if (NullCheckUtils.isBlank(biList)) {
            return;
        }
        int size = biList.getSize();
        for (int index = 0; index < size; index++) {
            Node<XinBi> node = biList.getNode(index);
            XinBi xinBi = node.getData();
            double maxPrice = xinBi.getMaxPrice();
            double minPrice = xinBi.getMinPrice();
            int kSize = xinBi.getKSize();
            BiRunTypeEnum biRunTypeEnum = xinBi.getBiRunTypeEnum();
            FenXing startFenXing = xinBi.getStartFenXing();
            FenXing endFenXing = xinBi.getEndFenXing();
            if (kSize >= BI_MIN_SIZE) {
                Node<XinBi> next = node.getNext();
                XinBi nextXinBi = next.getData();
                int nextBiKSize = nextXinBi.getKSize();
                if (nextBiKSize < BI_MIN_SIZE) {

                }
            }
        }
    }

    /**
     * 获取偶数笔，偶数特征序列
     *
     * @param xinBiList biList
     * @return List<Bi>
     */
    private static List<XinBi> getOuShu(List<XinBi> xinBiList) {
        if (NullCheckUtils.isBlank(xinBiList)) {
            return Collections.emptyList();
        }
        List<XinBi> ouShu = new ArrayList<>();
        for (int i = 0; i < xinBiList.size(); i += 2) {
            ouShu.add(xinBiList.get(i));
        }
        return ouShu;
    }


    private static BiRunTypeEnum getBiRunTypeEnum(FenXing firstFenXing) {
        return FengXingTypeEnum.TOP == firstFenXing.getFenXingTypeEnum() ? BiRunTypeEnum.DOWN : BiRunTypeEnum.UP;
    }

    /**
     * 由分型得到笔
     * 1.从第一个分型开始，到下一个分型之间是否具有5根K线
     *
     * @param fenXingList
     * @param containedKLineList
     * @return
     */
    public static List<XinBi> xx(List<FenXing> fenXingList, List<ContainedKLine> containedKLineList) {
        if (NullCheckUtils.isBlank(fenXingList) || NullCheckUtils.isBlank(containedKLineList)) {
            return Collections.emptyList();
        }
        int fenXingSize = fenXingList.size();
        List<XinBi> xinBiList = new ArrayList<>();
        XinBi firstXinBi = new XinBi();
        XinBi secondXinBi = new XinBi();
        FenXing firstFenXing = fenXingList.get(0);
        firstXinBi.setStartFenXing(firstFenXing);
        FenXing secondFenXing;
        FenXing thirdFenXing;
        for (int index = 1; index < fenXingSize; ) {
            secondFenXing = fenXingList.get(index);
            int kLineSize = kLineSize(firstFenXing, secondFenXing);
            if (kLineSize >= BI_MIN_SIZE) {
                firstXinBi.setEndFenXing(secondFenXing);
                secondXinBi.setStartFenXing(secondFenXing);

            } else {
                if (index + 1 <= fenXingSize - 1) {
                    boolean result = compareFenXing(firstFenXing, fenXingList.get(index + 1));
                    if (result) {
                        index = index + 1;
                        continue;
                    } else {
                        firstFenXing = secondFenXing;
                        firstXinBi.setStartFenXing(firstFenXing);
                        index = index + 1;
                        continue;
                    }
                }

            }
        }
        return xinBiList;
    }


    private void xx(FenXing firstFenXing, FenXing secondFenXing, FenXing thirdFenXing, List<FenXing> fenXingList) {
        if (NullCheckUtils.isBlank(fenXingList)) {
            return;
        }
        // 第一个分型和第二个分型之间的K线数量，是否满5根
        boolean firstAndSecond = false;
        if (NullCheckUtils.isNotBlank(firstFenXing) && NullCheckUtils.isNotBlank(secondFenXing)) {
            // 判断是否大于5根
            int kLineSize = kLineSize(firstFenXing, secondFenXing);
            firstAndSecond = kLineSize >= BI_MIN_SIZE;
        }
        // 第二个分型和第三个分型之间的K线数量，是否满5根
        boolean secondAndThird = false;
        if (NullCheckUtils.isNotBlank(secondFenXing) && NullCheckUtils.isNotBlank(thirdFenXing)) {
            // 判断是否大于5根
            int kLineSize = kLineSize(secondFenXing, thirdFenXing);
            secondAndThird = kLineSize >= BI_MIN_SIZE;
        }


        int startIndex = 0;
        int fenXingSize = fenXingList.size();
        if (NullCheckUtils.isBlank(firstFenXing)) {
            firstFenXing = fenXingList.get(startIndex);
        }
        for (int index = ++startIndex; index < fenXingSize; ) {
            secondFenXing = fenXingList.get(index);
            int kLineSize = kLineSize(firstFenXing, secondFenXing);
            if (kLineSize >= BI_MIN_SIZE) {
                xx(firstFenXing, secondFenXing, null, fenXingList.subList(index, fenXingSize - 1));
            } else {
                if (index + 1 <= fenXingSize - 1) {
                    boolean result = compareFenXing(firstFenXing, fenXingList.get(index + 1));
                    if (result) {
                        index = index + 1;
                        continue;
                    } else {
                        firstFenXing = secondFenXing;
                        index = index + 1;
                        continue;
                    }
                }

            }
        }
        return;
    }


    private static Integer getNext(int startIndex, List<FenXing> fenXingList) {
        if (NullCheckUtils.isBlank(fenXingList)) {
            return null;
        }

        FenXing firstFenXing;
        FenXing secondFenXing;
        int fenXingSize = fenXingList.size();
        firstFenXing = fenXingList.get(startIndex);
        for (int index = ++startIndex; index < fenXingSize; ) {
            secondFenXing = fenXingList.get(index);
            int kLineSize = kLineSize(firstFenXing, secondFenXing);
            if (kLineSize >= BI_MIN_SIZE) {
                return index;
            } else {
                if (index + 1 <= fenXingSize - 1) {
                    boolean result = compareFenXing(firstFenXing, fenXingList.get(index + 1));
                    if (result) {
                        index = index + 1;
                        continue;
                    } else {
                        firstFenXing = secondFenXing;
                        index = index + 1;
                        continue;
                    }
                }

            }
        }
        return null;
    }


    /**
     * 比较两个分型的高低，
     * 顶分型比较最高点，前者高为true,后者高为false
     * 低分型比较低高点，前者低为true,后者低为false
     *
     * @param firstFenXing  firstFenXing
     * @param secondFenXing secondFenXing
     * @return boolean
     */
    private static boolean compareFenXing(FenXing firstFenXing, FenXing secondFenXing) {
        FengXingTypeEnum firstFengXingTypeEnum = firstFenXing.getFenXingTypeEnum();
        FengXingTypeEnum secondFengXingTypeEnum = secondFenXing.getFenXingTypeEnum();
        if (firstFengXingTypeEnum != secondFengXingTypeEnum) {
            throw new RuntimeException();
        }
        boolean result = false;
        switch (firstFengXingTypeEnum) {
            case TOP:
                result = firstFenXing.getMiddleLine().getMaxPrice() >= secondFenXing.getMiddleLine().getMaxPrice();
                break;
            case BOTTOM:
                result = firstFenXing.getMiddleLine().getMinIndex() <= secondFenXing.getMiddleLine().getMinIndex();
                break;
            case NONE:
                throw new RuntimeException();
        }

        return result;
    }

    /**
     * 两个分型之间K线的数量
     *
     * @param firstFenXing  firstFenXing
     * @param secondFenXing secondFenXing
     * @return int
     */
    private static int kLineSize(FenXing firstFenXing, FenXing secondFenXing) {
        ContainedKLine firstEndLine = firstFenXing.getEndLine();
        ContainedKLine secondStartLine = secondFenXing.getStartLine();
        int firstEndIndex = firstEndLine.getEndIndex();
        int secondStartIndex = secondStartLine.getStartIndex();
        return 3 + secondStartIndex - firstEndIndex;
    }

    /**
     * 获取最大值
     *
     * @param firstFenXing  firstFenXing
     * @param secondFenXing secondFenXing
     * @return int
     */
    private static double getMaxPrice(FenXing firstFenXing, FenXing secondFenXing) {
        return Math.max(firstFenXing.getMaxPrice(), secondFenXing.getMaxPrice());
    }

    /**
     * 获取最小值
     *
     * @param firstFenXing  firstFenXing
     * @param secondFenXing secondFenXing
     * @return int
     */
    private static double getMinPrice(FenXing firstFenXing, FenXing secondFenXing) {

        return Math.min(firstFenXing.getMinPrice(), secondFenXing.getMinPrice());
    }


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
            FengXingTypeEnum priceType = one.getKType();
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
        boolean isTop = KLine.getKType() == FengXingTypeEnum.TOP;
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

    private static List<XinBi> sequence(List<XinBi> xinBiList) {
        if (NullCheckUtils.isBlank(xinBiList)) {
            return Collections.emptyList();
        }
        XinBi first = null;
        XinBi second = null;
        // 处理完包含之后的数据
        List<XinBi> result = new ArrayList<>();
        for (int index = 0; index < xinBiList.size(); index += 2) {
            if (NullCheckUtils.isBlank(first)) {
                first = xinBiList.get(index);
                result.add(first);
                continue;
            }
            second = xinBiList.get(index);
            double minPrice = second.getMinPrice() - first.getMinPrice();
            double maxPrice = second.getMaxPrice() - second.getMaxPrice();
            if ((maxPrice > 0 && minPrice > 0) || (maxPrice > 0 && minPrice >= 0) || (maxPrice >= 0 && minPrice > 0)) {
                // 向上
                setBiRunType(second, result, BiRunTypeEnum.UP);
                first = second;
                second = null;
                continue;
            } else if ((maxPrice < 0 && minPrice < 0) || (maxPrice < 0 && minPrice <= 0) || (maxPrice <= 0 && minPrice < 0)) {
                // 向下
                setBiRunType(second, result, BiRunTypeEnum.DOWN);
                first = second;
                second = null;
                continue;
            } else {
                // 包含
                if (maxPrice >= 0) {
                    // 说明second高
                    setBiRunType(second, result, first.getBiRunTypeEnum());
                    first = second;
                    second = null;
                } else {
                    // 说明second低，忽略掉这个second
                    result.add(second);
                    second = null;
                }
            }
        }

        if (first != null) {
            result.add(first);
        }
        if (second != null) {
            result.add(second);
        }
        return result;
    }


    private static List<Integer> zhuanZhe(List<XinBi> xinBiList) {
        if (NullCheckUtils.isBlank(xinBiList)) {
            return Collections.emptyList();
        }
        List<Integer> zzd = new ArrayList<>();
        //  trend,lastTrend 值一样说明方向没变,true=向上，false=向下
        XinBi first = null;
        XinBi second = null;
        for (int index = 0; index < xinBiList.size(); index++) {
            if (NullCheckUtils.isBlank(first)) {
                first = xinBiList.get(index);
                continue;
            }
            second = xinBiList.get(index);
            BiRunTypeEnum firstBiRunTypeEnum = first.getBiRunTypeEnum();
            BiRunTypeEnum secondBiRunTypeEnum = second.getBiRunTypeEnum();
            if (firstBiRunTypeEnum != secondBiRunTypeEnum) {
                zzd.add(index - 1);
            }
            first = second;
        }
        return zzd;
    }

    private static void setBiRunType(XinBi second, List<XinBi> list, BiRunTypeEnum biRunTypeEnum) {
        second.setBiRunTypeEnum(biRunTypeEnum);
        XinBi lastXinBi = getLastBi(list);
        if (NullCheckUtils.isNotBlank(lastXinBi) && NullCheckUtils.isBlank(lastXinBi.getBiRunTypeEnum())) {
            lastXinBi.setBiRunTypeEnum(biRunTypeEnum);
        }
        list.add(second);
    }


    private static XinBi getLastBi(List<XinBi> list) {
        if (NullCheckUtils.isBlank(list)) {
            return null;
        }
        return list.get(list.size() - 1);
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
                FengXingTypeEnum priceType = KLine.getKType();
                is = priceType == FengXingTypeEnum.TOP;
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
            FengXingTypeEnum firstPriceType1 = firstKLine1.getKType();
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
                if (FengXingTypeEnum.TOP == firstPriceType1) {

                }
                // 降笔
                if (FengXingTypeEnum.BOTTOM == firstPriceType1) {

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
            FengXingTypeEnum priceType = KLine.getKType();
            if (priceType == FengXingTypeEnum.TOP) {
                Pair<Integer, KLine> two = getNextTopBottom(topBottoms, ++i);
            }
            if (priceType == FengXingTypeEnum.BOTTOM) {
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
    private static Pair<Integer, Pair<Integer, KLine>> getNextType(FengXingTypeEnum priceType1, List<Pair<Integer, KLine>> topBottoms, int index) {
        boolean isHaveNext = false;
        Pair<Integer, KLine> target = null;
        Integer topBottomIndex = null;
        for (int i = index; i < topBottoms.size(); i++) {
            Pair<Integer, KLine> pair = topBottoms.get(i);
            KLine second = pair.getSecond();
            FengXingTypeEnum priceType2 = second.getKType();
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