package com.ryd.stockanalysis;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ryd.stockanalysis.bean.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.handle.StTradeThread;
import com.ryd.stockanalysis.handle.StockTradeThread;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis
 * 创建人：songby
 * 创建时间：2016/3/28 12:00
 */
public class StockAnalysis {

    private static Logger logger = Logger.getLogger(StockAnalysis.class);


    public static void main(String[] args) {
        logger.info("股票分析---------------开始--------------------");

        StockAnalysisServiceI serviceI = new StockAnalysisServiceImpl();

        //创建基础数据
        serviceI.createBaseData();
        initQuotePriceMap();

        //初始数据
        for(String key: Constant.stAccounts.keySet()){
            StAccount uu = Constant.stAccounts.get(key);
            printAccountInfo(uu,"初始");
        }

        // 创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);

        // 往线程池中放入用户报价（买入/卖出）信息------------StockTradeThread
        pool.execute(new StTradeThread());

        //买卖家报价
        pool.execute(new StockTradeThread(serviceI));


        try {
            //30秒后结算
            Thread.sleep(1000*50);
            //结算前用户信息
            for(String key: Constant.stAccounts.keySet()){
                StAccount uu = Constant.stAccounts.get(key);
                printAccountInfo(uu,"结算前");
            }
            //结算信息
            serviceI.settleResult();
        }catch (InterruptedException e){

        }


        //结算后用户信息
        for(String key: Constant.stAccounts.keySet()){
            StAccount uu = Constant.stAccounts.get(key);
            printAccountInfo(uu,"结算后");
        }


        for(StTradeRecord stt:Constant.recordList){
            logger.info("记录分析--交易买方->"+stt.getBuyerAccountId()+"--卖方->"+stt.getSellerAccountId()+"--交易价格->"+stt.getQuotePrice()+"--交易数量->"+stt.getAmount());
        }

        logger.info("股票分析---------------结束---------------------");
    }


    public static void printAccountInfo(StAccount uu,String pinfo){

        StringBuffer sb = new StringBuffer(pinfo);

        sb.append("帐户信息--帐号->" + uu.getAccountName());
        sb.append("--可使用金额->->"+uu.getUseMoney());
        sb.append("--帐户总金额->"+uu.getTotalMoney());


        if(CollectionUtils.isNotEmpty(uu.getStPositionList())) {
            StPosition sp = uu.getStPositionList().get(0);
            StStock st = sp.getStStock();
            sb.append("--持有股票编码->" + st.getStockCode());
            sb.append("--持有股票名称->" + st.getStockName());
            sb.append("--持仓数->" + sp.getAmount());
        }

        logger.info(sb.toString());
    }

    //初始化报价信息
    public static void initQuotePriceMap(){
        //交易股票
        StStock st = Constant.stockTable.get(Constant.TRADEING_STOCK_ID);

        //买家A
        StAccount aSt = Constant.stAccounts.get("A");
        for(int i=1; i<= Constant.STQUOTE_A_NUM;i++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",aSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_A_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_A_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_BUY);

            rtn.put("info","买家报价--报价次数-->"+i+"--买家->" + aSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--买家报价->" + Constant.STQUOTE_A_QUOTEPRICE+ "--购买数量->" + Constant.STQUOTE_A_AMOUNT);

            Constant.allQuoteTable.put("A"+i,rtn);
        }
        //买家B
        StAccount bSt = Constant.stAccounts.get("B");
        for(int bi=1; bi<= Constant.STQUOTE_B_NUM;bi++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",bSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_B_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_B_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_BUY);

            rtn.put("info","买家报价--报价次数-->"+bi+"--买家->" + bSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--买家报价->" + Constant.STQUOTE_B_QUOTEPRICE+ "--购买数量->" + Constant.STQUOTE_B_AMOUNT);

            Constant.allQuoteTable.put("B"+bi,rtn);
        }

        //卖家C
        StAccount cSt = Constant.stAccounts.get("C");
        for(int ci=1; ci<= Constant.STQUOTE_C_NUM;ci++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",cSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_C_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_C_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_SELL);

            rtn.put("info","卖家报价--报价次数-->"+ci+"--" + cSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--卖家报价->" +  Constant.STQUOTE_C_QUOTEPRICE+"--卖掉数量->" + Constant.STQUOTE_C_AMOUNT);

            Constant.allQuoteTable.put("C"+ci,rtn);
        }

        //卖家D
        StAccount dSt = Constant.stAccounts.get("D");
        for(int di=1; di<= Constant.STQUOTE_D_NUM;di++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",dSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_D_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_D_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_SELL);

            rtn.put("info","卖家报价--报价次数-->"+di+"--" + dSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--卖家报价->" +  Constant.STQUOTE_D_QUOTEPRICE+"--卖掉数量->" + Constant.STQUOTE_D_AMOUNT);

            Constant.allQuoteTable.put("D"+di,rtn);
        }

        //卖家E
        StAccount eSt = Constant.stAccounts.get("E");
        for(int ei=1; ei<= Constant.STQUOTE_E_NUM;ei++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",eSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_E_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_E_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_SELL);

            rtn.put("info","卖家报价--报价次数-->"+ei+"--" + eSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--卖家报价->" +  Constant.STQUOTE_E_QUOTEPRICE+"--卖掉数量->" + Constant.STQUOTE_E_AMOUNT);

            Constant.allQuoteTable.put("E"+ei,rtn);
        }
    }

}
