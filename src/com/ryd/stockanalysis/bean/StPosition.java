package com.ryd.stockanalysis.bean;

import java.io.Serializable;

/**
 * <p>标题:持仓</p>
 * <p>描述:持仓</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:55
 */
public class StPosition implements Serializable{

    private static final long serialVersionUID = 8371501383577455847L;

    private String positionId;
    private String accountId;
    private String stockId;
    private Integer amount; // 数量
    private Integer canSellAmount; // 可卖数量
    private Integer status;

    private StStock stStock;

    public StPosition() {
    }

    public StPosition(String accountId, String stockId, Integer amount, Integer status) {
        this.accountId = accountId;
        this.stockId = stockId;
        this.amount = amount;
        this.status = status;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public StStock getStStock() {
        return stStock;
    }

    public void setStStock(StStock stStock) {
        this.stStock = stStock;
    }
}
