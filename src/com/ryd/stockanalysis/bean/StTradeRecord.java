package com.ryd.stockanalysis.bean;

import java.io.Serializable;

/**
 * <p>标题:交易</p>
 * <p>描述:交易</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:56
 */
public class StTradeRecord implements Serializable{


    private static final long serialVersionUID = 1077912350836350022L;

    private String id;
    private String sellerAccountId;
    private String buyerAccountId;
    private String sellerAccountNumber; //冗余开户号
    private String buyerAccountNumber; //冗余开户号
    private String stockId;
    private String stockCode;
    private Integer amount; //数量
    private Double quotePrice;//交易价格

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerAccountId() {
        return sellerAccountId;
    }

    public void setSellerAccountId(String sellerAccountId) {
        this.sellerAccountId = sellerAccountId;
    }

    public String getBuyerAccountId() {
        return buyerAccountId;
    }

    public void setBuyerAccountId(String buyerAccountId) {
        this.buyerAccountId = buyerAccountId;
    }

    public String getBuyerAccountNumber() {
        return buyerAccountNumber;
    }

    public void setBuyerAccountNumber(String buyerAccountNumber) {
        this.buyerAccountNumber = buyerAccountNumber;
    }

    public String getSellerAccountNumber() {
        return sellerAccountNumber;
    }

    public void setSellerAccountNumber(String sellerAccountNumber) {
        this.sellerAccountNumber = sellerAccountNumber;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(Double quotePrice) {
        this.quotePrice = quotePrice;
    }
}
