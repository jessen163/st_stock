package com.ryd.stockanalysis.bean;

import java.io.Serializable;

/**
 * <p>标题:报价/下单</p>
 * <p>描述:报价/下单</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:55
 */
public class StStock implements Serializable{


    private static final long serialVersionUID = 7549760581444025071L;


    private String stockId;
    private String stockName;
    private String stockCode;
    private String stockPinyin;
    private String stockShortPinyin;

    public StStock() {
    }

    public StStock(String stockId, String stockName, String stockCode) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.stockCode = stockCode;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockPinyin() {
        return stockPinyin;
    }

    public void setStockPinyin(String stockPinyin) {
        this.stockPinyin = stockPinyin;
    }

    public String getStockShortPinyin() {
        return stockShortPinyin;
    }

    public void setStockShortPinyin(String stockShortPinyin) {
        this.stockShortPinyin = stockShortPinyin;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
