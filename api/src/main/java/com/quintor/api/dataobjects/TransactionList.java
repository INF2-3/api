package com.quintor.api.dataobjects;

import java.util.ArrayList;
import java.util.List;

public class TransactionList {
    private List<Transaction> list;

    public TransactionList() {
        this.list = new ArrayList<>();
    }

    public void add(Transaction transaction) {
        list.add(transaction);
    }

}
