package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 
 * <p>标题:用户买入/卖出股票线程</p> 
 * <p>描述:传入账户、股票信息，调用买入/卖出股票的报价动作--------------实现</p>
 * 包名：com.ryd.stockanalysis.handle
 *
 * 创   建 人：yl
 * 创建时间：2016-3-29 下午1:25:41
 */
public class StockTradeThread implements Runnable {

	private static Logger logger = Logger.getLogger(StockTradeThread.class);

	StockAnalysisServiceI stockAnalysisServiceI;

	public StockTradeThread(StockAnalysisServiceI stockAnalysisServiceI) {
		this.stockAnalysisServiceI = stockAnalysisServiceI;
	}

	@Override
	public void run() {
			try {
				if(!Constant.allQuoteTable.isEmpty()) {
					//处理报价
					for (String key : Constant.allQuoteTable.keySet()) {
						Map uu = Constant.allQuoteTable.get(key);
						if (uu == null) {
							Thread.sleep(1000);
							continue;
						}
						StQuote stQuote = stockAnalysisServiceI.quotePrice(uu.get("accountId").toString(), uu.get("stockId").toString(), (double) uu.get("quotePrice"), (int) uu.get("amount"), (int) uu.get("type"));

						if (stQuote != null) {
							logger.info(uu.get("info").toString());
						}

						Thread.sleep(1000);
					}
				}else{
					Thread.sleep(2000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

}