package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.*;
import org.apache.log4j.Logger;

import com.ryd.stockanalysis.common.Constant;

import java.util.*;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis
 * 创建人：songby
 * 创建时间：2016/3/28 17:57
 */
public class StTradeThread implements Runnable {

    private static Logger logger = Logger.getLogger(StTradeThread.class);

	@Override
    public void run(){
    	while (true) {
    		try {
    			if (!Constant.sellList.isEmpty()&&!Constant.sellList.isEmpty()) {

        			StQuote sellQuote = Constant.sellList.getLast();
        			StQuote buyQuote = Constant.buyList.getFrist();

					if (sellQuote==null||buyQuote==null) {
        				Thread.sleep(5000);
        				continue;
        			}

					if (sellQuote.getStockId().equals(buyQuote.getStockId()) && sellQuote.getAmount()==buyQuote.getAmount()&&Double.doubleToLongBits(sellQuote.getQuotePrice())<=Double.doubleToLongBits(buyQuote.getQuotePrice())) {

        				//添加买入/卖出交易成功后的逻辑
						//撮合成功，买家增加股票数量，卖家减钱
						//买家
						StAccount buySta = Constant.stAccounts.get(buyQuote.getAccountId());
						List<StPosition> stplist = buySta.getStPositionList();
						StPosition stp = null;
						for (StPosition p : stplist) {
							if (p.getAccountId().equals(buySta.getAccountId())) {
								stp = p;
								break;
							}
						}
						//原有持仓
						int camount = stp.getAmount();
						//买家报价量，新买股票
						int qamount = buyQuote.getAmount();
						//交易买家持仓
						stp.setAmount(camount + qamount);

						//卖家
						StAccount sellSta = Constant.stAccounts.get(sellQuote.getAccountId());
						//原有使用余额
						double cusemoney = sellSta.getUseMoney();
						//卖家报价收入
						double qmoney = buyQuote.getQuotePrice() * sellQuote.getAmount();

						//卖股票新增收入
						sellSta.setUseMoney(cusemoney + qmoney);
						sellSta.setTotalMoney(sellSta.getTotalMoney() + qmoney);


						//添加交易记录
						StTradeRecord str = new StTradeRecord();
						str.setId(UUID.randomUUID().toString());
						str.setBuyerAccountId(buyQuote.getAccountId());
						str.setSellerAccountId(sellQuote.getAccountId());
						str.setAmount(sellQuote.getAmount());
						str.setStockId(sellQuote.getStockId());
						str.setQuotePrice(buyQuote.getQuotePrice());
						//股票列表，设置股票编码
						StStock sts = Constant.stockTable.get(sellQuote.getStockId());

						if (sts != null) {
							str.setStockCode(sts.getStockCode());
						}

						//交易记录列表
						Constant.recordList.add(str);

						logger.info("交易--买家->" + buyQuote.getAccountId() + "-和-卖家->" + sellQuote.getAccountId() + "-交易成功-交易价格:" + buyQuote.getQuotePrice()+ "-交易数量:" + buyQuote.getAmount()+ "-交易总额:" + buyQuote.getQuotePrice()*buyQuote.getAmount());

						//移除记录
						Constant.sellList.removeElement(sellQuote);
						Constant.buyList.removeElement(buyQuote);
					}
        			Thread.sleep(1000);
        		} else {
        			Thread.sleep(5000);
        		}
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	}

    }

}
