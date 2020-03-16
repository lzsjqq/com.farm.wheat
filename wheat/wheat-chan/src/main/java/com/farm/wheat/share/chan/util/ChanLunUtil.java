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
        topBottomType(prices);
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
     * 得到笔
     */
    public static void bi() {

    }

    /**
     * 得到顶底分型
     *
     * @param prices
     * @return
     */
    public static void topBottomType(List<Price> prices) {
        if (NullCheckUtils.isBlank(prices)) {
            return;
        }
        int size = prices.size();
        for (int i = 0; i < size; i++) {
            Price price = prices.get(i);
            Pair<Integer, Integer> nodePair = twoPrePrice(prices, i);
            // 和前两根进行比较
            Integer firstPre = nodePair.getFirst();
            Integer secondPre = nodePair.getSecond();
            if (null == secondPre || null == firstPre) {
                // - 表示空点
                continue;
            }
            PriceRunTypeEnum priceType = price.getPriceRunType();
            Price firstPrePrice = prices.get(firstPre);
            // 判断当前节点是否是包含K线或和上个方向一致
            if (PriceRunTypeEnum.CONTAIN == priceType || priceType == firstPrePrice.getPriceRunType()) {
                continue;
            }
            // 至此方向不在一致，确认分型
            Price firstPreContainPrice = firstPrePrice.getContainPrice();
            Price priceContainPrice = price.getContainPrice();
            double firstPrePriceTodayMaxPrice = firstPreContainPrice != null ? firstPreContainPrice.getTodayMaxPrice() : firstPrePrice.getTodayMaxPrice();
            double priceTodayMaxPrice = priceContainPrice != null ? priceContainPrice.getTodayMaxPrice() : price.getTodayMaxPrice();
            if (firstPrePriceTodayMaxPrice > priceTodayMaxPrice) {
                topPrice(prices, firstPre, i).setPriceType(PriceTypeEnum.TOP);
            } else {
                bottomPrice(prices, firstPre, i).setPriceType(PriceTypeEnum.BOTTOM);
            }
        }
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


    /**
     * 获取处理完包含关系的前两根K线
     *
     * @param prices
     * @param index
     * @return
     */
    private static Pair<Integer, Integer> twoPrePrice(List<Price> prices, int index) {
        Pair<Integer, Integer> pair = new Pair<>(null, null);
        if (0 == index) {
            return pair;
        }
        // 判断前一个是否是包含节点,如果是包含节点获取到包含节点的第一根K线
        Price price = prices.get(index);
        int pre;
        if (PriceRunTypeEnum.CONTAIN == price.getPriceRunType()) {
            pre = index - price.getContainSize();
        } else {
            pre = index - 1;
        }
        // 获取第二个节点
        int secondPre = 1;
        Price firstNodeData = prices.get(pre);
        if (PriceRunTypeEnum.CONTAIN == firstNodeData.getPriceRunType()) {
            secondPre = pre - firstNodeData.getContainSize();
        } else {
            secondPre = pre - 1;
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
        for (int i = 0; i < size; i++) {
            Price price = prices.get(i);
            /**
             * 前一个
             */
            double priceMaxPrice = price.getTodayMaxPrice();
            double priceMinPrice = price.getTodayMinPrice();
            Price prePrice = getPre(prices, i);
            if (prePrice == null) {
                price.setPriceRunType(PriceRunTypeEnum.NONE);
                continue;
            }
            // 判断是否是包含K线
            Price preContainPrice = prePrice.getContainPrice();
            double prePriceMaxPrice = preContainPrice != null ? preContainPrice.getTodayMaxPrice() : prePrice.getTodayMaxPrice();
            double prePriceMinPrice = preContainPrice != null ? preContainPrice.getTodayMinPrice() : prePrice.getTodayMinPrice();
            double max = priceMaxPrice - prePriceMaxPrice;
            double min = priceMinPrice - prePriceMinPrice;
            if (max >= 0 && min >= 0) {
                // 高高 向上
                price.setPriceRunType(PriceRunTypeEnum.UP);
//                price.setBiSize(price.getBiSize() + 1);
            } else if (max <= 0 && min <= 0) {
                // 低低 向下
                price.setPriceRunType(PriceRunTypeEnum.DOWN);
//                price.setBiSize(price.getBiSize() + 1);
            } else {
                // 类型为包含
                price.setPriceRunType(PriceRunTypeEnum.CONTAIN);
                // 包含类
                Price containPrice = new Price();
                // 计算k线包含数量
                containPrice.setContainSize(getContainSize(prices, 2));
                PriceRunTypeEnum priceType = prePrice.getPriceRunType();
                if (priceType == PriceRunTypeEnum.UP) {
                    // 向上 包含
                    contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max > 0, min > 0, PriceRunTypeEnum.UP);
                } else if (priceType == PriceRunTypeEnum.DOWN) {
                    // 向下 包含
                    contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max < 0, min < 0, PriceRunTypeEnum.DOWN);
                } else if (priceType == PriceRunTypeEnum.CONTAIN) {
                    // 包含 同包含方向处理
                    PriceRunTypeEnum priceRunType = preContainPrice.getPriceRunType();
                    if (priceRunType == PriceRunTypeEnum.UP) {
                        // 向上 包含
                        contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max > 0, min > 0, PriceRunTypeEnum.UP);
                    } else if (priceRunType == PriceRunTypeEnum.DOWN) {
                        // 向下 包含
                        contain(priceMaxPrice, priceMinPrice, prePriceMaxPrice, prePriceMinPrice, containPrice, max < 0, min < 0, PriceRunTypeEnum.DOWN);
                    }
                    containPrice.setPriceRunType(priceRunType);
                }
                price.setContainPrice(containPrice);
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
     * @param prices
     * @param index
     * @return
     */
    private static int getContainSize(List<Price> prices, int index) {
        int size = 2;
        Price price;
        for (int i = index - 1; i >= 0; i--) {
            price = prices.get(i);
            if (PriceRunTypeEnum.CONTAIN != price.getPriceRunType()) {
                break;
            }
            size += 1;
        }
        return size;
    }
}