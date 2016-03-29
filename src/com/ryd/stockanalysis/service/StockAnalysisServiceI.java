package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StQuote;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service
 * 创建人：songby
 * 创建时间：2016/3/29 10:02
 */
public interface StockAnalysisServiceI {
    /**
     * 创建基础数据
     * @return
     */
    public boolean createBaseData();

    /**
     * 结算结果
     * @return
     */
    public boolean settleResult();
    /**
     * 报价
     * @param stockId
     * @param amount
     * @param type
     * @return
     */
    public StQuote quotePrice(String accountId,String stockId, double quotePrice, int amount, int type);
}
