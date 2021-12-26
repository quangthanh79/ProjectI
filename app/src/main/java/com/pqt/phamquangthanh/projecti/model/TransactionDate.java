package com.pqt.phamquangthanh.projecti.model;

import java.util.Date;

public class TransactionDate {
    public Date date;
    public long amountOfMoney;

    public TransactionDate(Date date, long amountOfMoney) {
        this.date = date;
        this.amountOfMoney = amountOfMoney;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(long amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }
}
