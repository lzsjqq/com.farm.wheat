package com.farm.wheat.share.chan.util;

import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;

import java.util.List;

/**
 * @program: wheat
 * @description: 用于构造处理链表
 * @author: xyc
 * @create: 2020-03-13 21:49
 */
public class LinkedUtil {
    private LinkedUtil() {
    }

    /**
     * 构建链表
     *
     * @return
     */
    public static Linked<Price> buildLined(List<SharePriceDto> list) throws Exception {
        Linked<Price> linked = new Linked<>();
        if (NullCheckUtils.isNotBlank(list)) {
            for (SharePriceDto sharePrice : list) {
                Price price = new Price();
                price.setTodayMinPrice(sharePrice.getTodayMinPrice().doubleValue());
                price.setTodayMaxPrice(sharePrice.getTodayMaxPrice().doubleValue());
                price.setTodayEndPrice(sharePrice.getTodayEndPrice().doubleValue());
                price.setTodayOpenPrice(sharePrice.getTodayOpenPrice().doubleValue());
                price.setTradingDate(DateUtils.dateToString(sharePrice.getTradingDate(), DateUtils.YYYY_MM_DD));
                linked.add(price);
            }
        }
        // 处理包含关系包含关系
        handleContain(linked);
        // 处理分型
        topBottomType(linked);
        // 处理顶底分型
        return linked;
    }

    /**
     * 得到顶底分型
     *
     * @param linked
     * @return
     */
    public static void topBottomType(Linked<Price> linked) {
        if (linked == null || linked.getSize() == 0) {
            return;
        }
        int size = linked.getSize();
        for (int i = 0; i < size; i++) {
            Node<Price> node = linked.getNode(i);
            Pair<Node<Price>, Node<Price>> nodePair = twoPrePrice(node);
            // 和前两根进行比较
            Node<Price> firstPre = nodePair.getFirst();
            Node<Price> secondPre = nodePair.getSecond();
            if (null == secondPre || null == firstPre) {
                continue;
            }
            Price price = node.data;
            PriceRunTypeEnum priceType = price.getPriceRunType();
            Price firstPrePrice = firstPre.data;
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
                firstPrePrice.setPriceType(PriceTypeEnum.TOP);
            } else {
                firstPrePrice.setPriceType(PriceTypeEnum.BOTTOM);
            }
        }
    }


    /**
     * 获取处理完包含关系的前两根K线
     *
     * @param node
     * @return
     */
    private static Pair<Node<Price>, Node<Price>> twoPrePrice(Node<Price> node) {
        Pair<Node<Price>, Node<Price>> pair = new Pair<>(null, null);
        // 判断前一个是否是包含节点
        Price nodeData = node.getData();
        int pre = 1;
        if (PriceRunTypeEnum.CONTAIN == nodeData.getPriceRunType()) {
            pre = nodeData.getContainSize();
        }
        // 获取第二个节点
        Node<Price> firstNode = Linked.getPreNode(pre, node);
        if (null == firstNode) {
            return pair;
        }
        int secondPre = 1;
        Price firstNodeData = firstNode.getData();
        if (PriceRunTypeEnum.CONTAIN == firstNodeData.getPriceRunType()) {
            secondPre = firstNodeData.getContainSize();
        }
        Node<Price> secondNode = Linked.getPreNode(secondPre, firstNode);
        pair.setFirst(firstNode);
        pair.setSecond(secondNode);
        return pair;
    }

    /**
     * 处理包含关系包含关系
     *
     * @param linked
     * @return false 不包含 true 包含
     */
    private static void handleContain(Linked<Price> linked) {
        int size = linked.getSize();
        for (int i = 0; i < size; i++) {
            Node<Price> node = linked.getNode(i);
            /**
             * 前一个
             */
            Price price = node.data;
            double priceMaxPrice = price.getTodayMaxPrice();
            double priceMinPrice = price.getTodayMinPrice();
            Node<Price> pre = node.getPre();
            if (pre == null) {
                price.setPriceRunType(PriceRunTypeEnum.NONE);
                continue;
            }

            Price prePrice = pre.data;
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
                containPrice.setContainSize(getContainSize(node, 2));
                PriceRunTypeEnum priceType = prePrice.getPriceRunType();
                // 向上 包含
                if (priceType == PriceRunTypeEnum.UP) {
                    containPrice.setTodayMaxPrice(max > 0 ? priceMaxPrice : prePriceMaxPrice);
                    containPrice.setTodayMinPrice(min > 0 ? priceMinPrice : prePriceMinPrice);
                }
                // 向下 包含
                if (priceType == PriceRunTypeEnum.DOWN) {
                    containPrice.setTodayMaxPrice(max < 0 ? priceMaxPrice : prePriceMaxPrice);
                    containPrice.setTodayMinPrice(min < 0 ? priceMinPrice : prePriceMinPrice);
                }
                price.setContainPrice(containPrice);
            }
        }
    }

    /**
     * 从当前确定和前面包含的K线算确定当前一共包含了几根K线
     *
     * @param node
     * @return
     */
    private static int getContainSize(Node<Price> node, int size) {
        Node<Price> pre = node.pre;
        if (null == pre || PriceRunTypeEnum.CONTAIN != pre.getData().getPriceRunType()) {
            return 0 + size;
        }
        return getContainSize(pre.pre, size + 1);
    }

}