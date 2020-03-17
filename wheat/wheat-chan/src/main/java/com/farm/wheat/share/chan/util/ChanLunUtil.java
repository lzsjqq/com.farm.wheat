package com.farm.wheat.share.chan.util;

import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;

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
    public static List<Price> buildLined(List<Price> prices) throws Exception {
        // 处理包含关系包含关系
        handleContain(prices);
        // 处理顶底分型
        List<Pair<Integer, Price>> pairs = topBottomType(prices);
        // 画笔     -：表示空点  "1-"+ MaxPrice：顶分型    "0-"+ MinPrice：低分型
//        List<String>
        return prices;
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
     * 得到笔
     */
    public static void bi(List<Pair<Integer, Price>> topBottoms, List<Price> prices) {
        if (NullCheckUtils.isBlank(prices) || NullCheckUtils.isBlank(topBottoms)) {
            return;
        }
        Price price;
        BiRunTypeEnum biRunType = null;
        // 第一个笔
        int firstOneIndex = 0;
        int firstTwoIndex = 0;

        PriceTypeEnum firstOnePriceType = null;
        PriceTypeEnum firstTwoPriceType = null;

        // 第二个笔
        Price secondOnePrice = null;
        Price secondTwoPrice = null;
        PriceTypeEnum secondOnePriceType = null;
        PriceTypeEnum secondTwoPriceType = null;

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
            Pair<Integer, Pair<Integer, Price>> nextType = getNextType(firstPriceType1, topBottoms, index + 1);
            if (nextType == null) {
                break;
            }
            Pair<Integer, Price> pair2 = nextType.getSecond();
            Integer firstIndex2 = pair2.getFirst();
            // 判断是否存在5根K线,确定两个坐标之间的非包含K线的数量
            int size = nonContainSize(prices, firstIndex1, firstIndex2);
            if (size >= BI_MIN_SIZE) {

            }
            // 下次循环开始 index
            index = index + nextType.getFirst();


        }


        for (int i = 0; i < prices.size(); i++) {
            price = prices.get(i);
            PriceTypeEnum priceType = price.getPriceType();
            // 确定开始笔方向
            if (PriceTypeEnum.NONE == priceType) {
                continue;
            }
            // 为第一个笔起点赋值
            if (firstOnePriceType == null) {
                firstOnePriceType = priceType;
                firstOneIndex = i;
                continue;
            }
            // 为第一个笔结束点赋值,赋值条件为：第二个点不能和第一个点的类型相同
            if (firstTwoPriceType == null && priceType != firstOnePriceType) {
                firstTwoPriceType = priceType;
                firstTwoIndex = i;
                continue;
            }


            // 判断 第一个笔的

            // 为第二个笔起点赋值
            if (secondOnePriceType == null) {
                secondOnePriceType = priceType;
                secondOnePrice = price;
                continue;
            }
            // 为第二个笔结束点赋值
            if (secondTwoPriceType == null) {
                secondTwoPriceType = priceType;
                secondTwoPrice = price;
                continue;
            }


        }
    }

    /**
     * 确定两个坐标之间的非包含K线的数量
     *
     * @param prices
     * @param firstIndex1
     * @param firstIndex2
     * @return
     */
    public static int nonContainSize(List<Price> prices, Integer firstIndex1, Integer firstIndex2) {
        int size = 0;
        boolean isContain = false;
        for (int i = firstIndex1; i <= firstIndex2; i++) {
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
     * 得到顶底分型
     *
     * @param prices
     * @return
     */
    public static List<Pair<Integer, Price>> topBottomType(List<Price> prices) {
        List<Pair<Integer, Price>> pairs = new ArrayList<>();
        int size;
        if (NullCheckUtils.isBlank(prices) || (size = prices.size()) < 3) {
            return pairs;
        }
        for (int index = 2; index < size; index++) {
            Price price = prices.get(index);
            String tradingDate = price.getTradingDate();
            if ("2019-03-06".equals(tradingDate)) {
                System.out.println();
            }
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
                Pair<Integer, Price> pair = topPrice(prices, firstPre, index);
                // 判断上个分型是否是顶分型，如果是去掉，只保留最高的，
                Pair<Integer, Price> last = getLastPair(pairs);
                if (last != null && PriceTypeEnum.TOP == last.getSecond().getPriceType()) {
                    last.getSecond().setPriceType(PriceTypeEnum.NONE);
                    pairs.remove( pairs.size() - 1);
                }
                pair.getSecond().setPriceType(PriceTypeEnum.TOP);
                pairs.add(pair);// 添加新元素
            } else {
                Pair<Integer, Price> pair = bottomPrice(prices, firstPre, index);
                // 判断上个分型是否是底分型，如果是去掉，只保留最底的，
                Pair<Integer, Price> last = getLastPair(pairs);
                if (last != null && PriceTypeEnum.BOTTOM == last.getSecond().getPriceType()) {
                    last.getSecond().setPriceType(PriceTypeEnum.NONE);
                    pairs.remove(pairs.size() - 1);
                }
                pair.getSecond().setPriceType(PriceTypeEnum.BOTTOM);
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

    private static Pair<Integer, Price> topPrice(List<Price> prices, int firstPre, int index) {
        Pair<Integer, Price> pair = new Pair<>();
        Price max = prices.get(firstPre);
        Price price;
        Integer maxIndex = firstPre;
        for (int i = firstPre + 1; i < index; i++) {
            price = prices.get(i);
            if (price.getTodayMaxPrice() > max.getTodayMaxPrice()) {
                max = price;
                maxIndex = i;
            }
        }
        pair.setFirst(maxIndex);
        pair.setSecond(max);
        return pair;
    }

    private static Pair<Integer, Price> bottomPrice(List<Price> prices, int firstPre, int index) {
        Pair<Integer, Price> pair = new Pair<>();
        Price min = prices.get(firstPre);
        Integer minIndex = firstPre;
        Price price;
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

    /**
     * 从当前确定和前面包含的K线算确定当前一共包含了几根K线
     *
     * @param preContainPrice
     * @return
     */
    private static int getContainSize(Price preContainPrice) {
        return preContainPrice == null ? 2 : preContainPrice.getContainSize() + 1;
    }
}