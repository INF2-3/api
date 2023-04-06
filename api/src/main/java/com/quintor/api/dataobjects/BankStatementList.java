package com.quintor.api.dataobjects;

import java.util.ArrayList;
import java.util.List;

public class BankStatementList {
    private List<BankStatement> list;

    public BankStatementList() {
        this.list = new ArrayList<>();
    }

    public void add(BankStatement bankStatement) {
        list.add(bankStatement);
    }
}
