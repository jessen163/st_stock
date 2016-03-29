package com.ryd.stockanalysis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>标题:账户</p>
 * <p>描述:账户</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:54
 */
public class StAccount implements Serializable {

    private static final long serialVersionUID = -529781398975293968L;

    private String accountId;
    private String accountName;
    private String accountNumber;
    private Double useMoney; //可以花的钱
    private Double totalMoney; //包含因报价/下单冻结的钱和持仓的钱
    private List<StPosition> stPositionList = new ArrayList<StPosition>();//持仓
    private List<StQuote> stQuotesList = new ArrayList<StQuote>();//报价
    public StAccount() {
    }

    public StAccount(String accountId,String accountName, String accountNumber, Double useMoney, Double totalMoney) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.useMoney = useMoney;
        this.totalMoney = totalMoney;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(Double useMoney) {
        this.useMoney = useMoney;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<StPosition> getStPositionList() {
        return stPositionList;
    }

    public List<StQuote> getStQuotesList() {
        return stQuotesList;
    }
}
