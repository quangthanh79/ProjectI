package com.pqt.phamquangthanh.projecti.model;

public class TransactionStatistic {
    public long beginMoneyAmount;
    public long endMoneyAmount;

    public TransactionStatistic(long beginMoneyAmount, long endMoneyAmount) {
        this.beginMoneyAmount = beginMoneyAmount;
        this.endMoneyAmount = endMoneyAmount;
    }

    public long getEndMoneyAmount() {
        return endMoneyAmount;
    }

    public void setEndMoneyAmount(long endMoneyAmount) {
        this.endMoneyAmount = endMoneyAmount;
    }

    public long getBeginMoneyAmount() {
        return beginMoneyAmount;
    }

    public void setBeginMoneyAmount(long beginMoneyAmount) {
        this.beginMoneyAmount = beginMoneyAmount;
    }
}
