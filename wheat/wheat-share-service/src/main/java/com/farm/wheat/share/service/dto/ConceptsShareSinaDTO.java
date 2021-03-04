package com.farm.wheat.share.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 新浪
 * @author: xyc
 * @create: 2019-10-27 16:14
 */
@Data
public class ConceptsShareSinaDTO {
    @JSONField(name = "symbol")
    private String symbol;
    @JSONField(name = "code")
    private String code;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "trade")
    private String trade;
    @JSONField(name = "pricechange")
    private String priceChange;
    @JSONField(name = "changepercent")
    private String changePercent;
    @JSONField(name = "buy")
    private String buy;
    @JSONField(name = "sell")
    private String sell;
    @JSONField(name = "settlement")
    private String settlement;
    @JSONField(name = "open")
    private String open;
    @JSONField(name = "high")
    private String high;
    @JSONField(name = "low")
    private String low;
    @JSONField(name = "volume")
    private BigDecimal volume;
    @JSONField(name = "amount")
    private BigDecimal amount;
    @JSONField(name = "ticktime")
    private String tickTime;
    @JSONField(name = "per")
    private BigDecimal per;
    @JSONField(name = "pb")
    private BigDecimal pb;
    @JSONField(name = "mktcap")
    private BigDecimal mktCap;
    @JSONField(name = "nmc")
    private BigDecimal nmc;
    @JSONField(name = "turnoverratio")
    private BigDecimal turnoverRatio;

}
