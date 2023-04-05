package com.quintor.api.dataobjects;

import java.time.LocalDate;

public class Balance {
    private int id;
    private DebitOrCredit debitOrCredit;
    private LocalDate date;
    private String currency;
    private double amount;
    private BalanceType type;

    public Balance(int id, DebitOrCredit debitOrCredit, LocalDate date, String currency, double amount, BalanceType type) {
        this.id = id;
        this.debitOrCredit = debitOrCredit;
        this.date = date;
        this.currency = currency;
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DebitOrCredit getDebitOrCredit() {
        return this.debitOrCredit;
    }

    public void setDebitOrCredit(DebitOrCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BalanceType getType() {
        return this.type;
    }

    public void setType(BalanceType type) {
        this.type = type;
    }
}
