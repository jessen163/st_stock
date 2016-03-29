package com.ryd.stockanalysis.bean;

import java.io.Serializable;

/**
 * <p>标题:股票</p>
 * <p>描述:股票</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:55
 */
public class StQuote implements Serializable, Comparable<StQuote> {


    private static final long serialVersionUID = -8913199818978793382L;

    private String quoteId;
    private String accountId;
    private String stockId;
    private Integer amount; //数量
    private Double quotePrice;//报价
    private Integer type; //1、买，2、卖
    private Integer status; //1、托管 2、成交 -1、过期

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Double getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(Double quotePrice) {
        this.quotePrice = quotePrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int compareTo(StQuote o) {
        if(this.quotePrice < o.quotePrice){
            return 1;
        } else if(this.quotePrice==o.quotePrice){
            return 0;
        } else{
            return -1;
        }
    }
}
