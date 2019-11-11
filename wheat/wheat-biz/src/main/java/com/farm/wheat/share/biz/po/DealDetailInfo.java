package main.java.com.farm.wheat.share.biz.po;

import java.math.BigDecimal;
import java.util.Date;

public class DealDetailInfo {
    private Integer idDealDetailInfo;

    private Date tradingDate;

    private String shareCode;

    private String shareName;

    private BigDecimal dealPrice;

    private Integer volume;

    private String target;

    private BigDecimal stopLossPrice;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private BigDecimal profit;

    private BigDecimal fiveLowPrice;

    private BigDecimal fiveHighPrice;

    private BigDecimal fiveProfit;

    private BigDecimal tenLowPrice;

    private BigDecimal tenHighPrice;

    private BigDecimal tenProfit;

    private BigDecimal rRate;

    private Date createTime;

    private Date updateTime;

    private String reason;

    public Integer getIdDealDetailInfo() {
        return idDealDetailInfo;
    }

    public void setIdDealDetailInfo(Integer idDealDetailInfo) {
        this.idDealDetailInfo = idDealDetailInfo;
    }

    public Date getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(Date tradingDate) {
        this.tradingDate = tradingDate;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode == null ? null : shareCode.trim();
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName == null ? null : shareName.trim();
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    public BigDecimal getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(BigDecimal stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getFiveLowPrice() {
        return fiveLowPrice;
    }

    public void setFiveLowPrice(BigDecimal fiveLowPrice) {
        this.fiveLowPrice = fiveLowPrice;
    }

    public BigDecimal getFiveHighPrice() {
        return fiveHighPrice;
    }

    public void setFiveHighPrice(BigDecimal fiveHighPrice) {
        this.fiveHighPrice = fiveHighPrice;
    }

    public BigDecimal getFiveProfit() {
        return fiveProfit;
    }

    public void setFiveProfit(BigDecimal fiveProfit) {
        this.fiveProfit = fiveProfit;
    }

    public BigDecimal getTenLowPrice() {
        return tenLowPrice;
    }

    public void setTenLowPrice(BigDecimal tenLowPrice) {
        this.tenLowPrice = tenLowPrice;
    }

    public BigDecimal getTenHighPrice() {
        return tenHighPrice;
    }

    public void setTenHighPrice(BigDecimal tenHighPrice) {
        this.tenHighPrice = tenHighPrice;
    }

    public BigDecimal getTenProfit() {
        return tenProfit;
    }

    public void setTenProfit(BigDecimal tenProfit) {
        this.tenProfit = tenProfit;
    }

    public BigDecimal getrRate() {
        return rRate;
    }

    public void setrRate(BigDecimal rRate) {
        this.rRate = rRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }
}