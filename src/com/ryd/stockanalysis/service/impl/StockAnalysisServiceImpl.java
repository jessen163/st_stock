package com.ryd.stockanalysis.service.impl;

import java.util.*;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import org.apache.log4j.Logger;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/29 10:00
 */
public class StockAnalysisServiceImpl implements StockAnalysisServiceI {

    private static Logger logger = Logger.getLogger(StockAnalysisServiceImpl.class);

    @Override
    public boolean createBaseData() {
        Hashtable<String,StAccount> userTable = Constant.stAccounts;
        //中国平安-股票
        StStock stStock = new StStock("1","中国平安","633256");
        Constant.stockTable.put(stStock.getStockId(),stStock);

        //初始数据用户A、B为卖家拥有持仓，用户C、D、E为买家，持仓为空

        //创建卖家A
        StAccount ataA = new StAccount("A","A","1",60000d,100000d);
        //用户A持仓
        StPosition ata1Pos = new StPosition();
        ata1Pos.setPositionId(UUID.randomUUID().toString());
        ata1Pos.setAccountId(ataA.getAccountId());
        ata1Pos.setStockId(stStock.getStockId());
        ata1Pos.setStStock(stStock);
        ata1Pos.setAmount(1000);
        ata1Pos.setStatus(1);

        ataA.getStPositionList().add(ata1Pos);

        //创建卖家B
        StAccount ataB = new StAccount("B","B","2",60000d,100000d);
        //用户B持仓
        StPosition ata2Pos = new StPosition();
        ata2Pos.setPositionId(UUID.randomUUID().toString());
        ata2Pos.setAccountId(ataB.getAccountId());
        ata2Pos.setStockId(stStock.getStockId());
        ata2Pos.setStStock(stStock);
        ata2Pos.setAmount(0);
        ata2Pos.setStatus(1);

        ataB.getStPositionList().add(ata2Pos);

        StAccount ataC = new StAccount("C","C","3",40000d,100000d);
        //用户C持仓
        StPosition ata3Pos = new StPosition();
        ata3Pos.setPositionId(UUID.randomUUID().toString());
        ata3Pos.setAccountId(ataC.getAccountId());
        ata3Pos.setStockId(stStock.getStockId());
        ata3Pos.setStStock(stStock);
        ata3Pos.setAmount(10000);
        ata3Pos.setStatus(1);

        ataC.getStPositionList().add(ata3Pos);

        StAccount ataD = new StAccount("D","D","4",40000d,100000d);
        //用户D持仓
        StPosition ata4Pos = new StPosition();
        ata4Pos.setPositionId(UUID.randomUUID().toString());
        ata4Pos.setAccountId(ataD.getAccountId());
        ata4Pos.setStockId(stStock.getStockId());
        ata4Pos.setStStock(stStock);
        ata4Pos.setAmount(10000);
        ata4Pos.setStatus(1);

        ataD.getStPositionList().add(ata4Pos);

        StAccount ataE = new StAccount("E","E","5",40000d,100000d);
        //用户E持仓
        StPosition ata5Pos = new StPosition();
        ata5Pos.setPositionId(UUID.randomUUID().toString());
        ata5Pos.setAccountId(ataE.getAccountId());
        ata5Pos.setStockId(stStock.getStockId());
        ata5Pos.setStStock(stStock);
        ata5Pos.setAmount(10000);
        ata5Pos.setStatus(1);

        ataE.getStPositionList().add(ata5Pos);

        userTable.put(ataA.getAccountId(),ataA);
        userTable.put(ataB.getAccountId(),ataB);
        userTable.put(ataC.getAccountId(),ataC);
        userTable.put(ataD.getAccountId(),ataD);
        userTable.put(ataE.getAccountId(),ataE);

        return true;
    }

    @Override
    public boolean settleResult(){
        //卖家结算
        LinkedList<StQuote> sellLinkList = Constant.sellList.getList();
        Iterator iterator = sellLinkList.iterator();
        while (iterator.hasNext())
        {
            StQuote stq = (StQuote)iterator.next();
            StAccount sta = Constant.stAccounts.get(stq.getAccountId());
            List<StPosition> stplist = sta.getStPositionList();
            StPosition stp = null;
            for(StPosition p : stplist){
                if(p.getAccountId().equals(sta.getAccountId())){
                    stp = p;
                    break;
                }
            }
            //持仓
            int camount = stp.getAmount();
            //卖家报价量
            int qamount = stq.getAmount();
            //退还托管股票
            stp.setAmount(camount+qamount);

            logger.info("卖家结算--卖家->"+sta.getAccountName()+"--股票名称->"+stp.getStStock().getStockName()+"--股票编码->"+stp.getStStock().getStockCode()+"--退还股票数--"+qamount);
       }
        //清除卖家队列
        Constant.sellList.clear();


        //买家结算
        LinkedList<StQuote> buyLinkList =  Constant.buyList.getList();
        Iterator iteratorb = buyLinkList.iterator();
        while (iteratorb.hasNext())
        {
            StQuote stqb = (StQuote)iteratorb.next();
            StAccount stab = Constant.stAccounts.get(stqb.getAccountId());

            //当前使用余额
            double cusemoney = stab.getUseMoney();
            //卖家报价开销
            double qmoney = stqb.getQuotePrice()*stqb.getAmount();

            //退还托管股票
            stab.setUseMoney(cusemoney + qmoney);
            stab.setTotalMoney(stab.getTotalMoney() + qmoney);

            StStock sto = Constant.stockTable.get(stqb.getStockId());
            logger.info("买家结算--买家->"+stab.getAccountName()+"--交易股票->"+sto.getStockName()+"--股票编码->"+sto.getStockCode()+"--退还金额->"+qmoney);
        }
        //清除买家队列
        Constant.buyList.clear();

        return true;
    }


    @Override
    public synchronized StQuote quotePrice(String accountId,String stockId, double quotePrice, int amount, int type) {

        StQuote stQuote = null;

        if(changeUserMoney(accountId,stockId,quotePrice,amount,type)) {
            stQuote = new StQuote();
            stQuote.setQuoteId(UUID.randomUUID().toString());
            stQuote.setStockId(stockId);
            stQuote.setAccountId(accountId);
            stQuote.setQuotePrice(quotePrice);
            stQuote.setAmount(amount);
            stQuote.setType(type);
            stQuote.setStatus(Constant.STOCK_STQUOTE_STATUS_TRUSTEE);
            if (type == Constant.STOCK_STQUOTE_TYPE_BUY) {
            	Constant.buyList.add(stQuote);
            } else {
            	Constant.sellList.add(stQuote);
            }
        }
        return stQuote;
    }

    //判断是否正常报价
    private boolean changeUserMoney(String accountId,String stockId, double quotePrice, int amount, int type) {
        //报价处理用户信息
        StAccount account = Constant.stAccounts.get(accountId);
        if (account !=null ) {
            //买股票时
            if (type == Constant.STOCK_STQUOTE_TYPE_BUY) {
                double spendMoney = quotePrice * amount;
                double useMoney = account.getUseMoney();
                if (useMoney > spendMoney) {
                    account.setUseMoney(useMoney - spendMoney);
                    return true;
                } else {
                    return false;
                }
            } else { //卖股票时

                List<StPosition> positions = account.getStPositionList();
                for (StPosition stp : positions) {
                    if (stp.getStockId().equals(stockId)) {
                        //报价前持仓数
                        int stamount = stp.getAmount();
                        if (stamount > amount) {
                            stp.setAmount(stamount - amount);
                            return true;
                        } else {
                            return false;
                        }
                    }

                }

            }
        }
        return false;
    }
}
