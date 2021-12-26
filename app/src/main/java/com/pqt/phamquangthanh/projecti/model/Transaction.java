package com.pqt.phamquangthanh.projecti.model;

import com.pqt.phamquangthanh.projecti.model.TransactionGroup;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private int id;
    private TransactionGroup transactionGroup;
    private long amountOfMoney;
    private String note;
    private Date date;
    private int isShow;


    public Transaction(TransactionGroup transactionGroup, long amountOfMoney, String note, Date date,int isShow) {
        this.transactionGroup = transactionGroup;
        this.amountOfMoney = amountOfMoney;
        this.note = note;
        this.date = date;
        this.isShow = isShow;
    }

    public Transaction(int id, TransactionGroup transactionGroup, long amountOfMoney, String note, Date date,int isShow) {
        this.id = id;
        this.transactionGroup = transactionGroup;
        this.amountOfMoney = amountOfMoney;
        this.note = note;
        this.date = date;
        this.isShow = isShow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(long amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}