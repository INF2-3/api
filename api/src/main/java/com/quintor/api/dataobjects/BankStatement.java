package com.quintor.api.dataobjects;

import java.time.LocalDate;
import java.util.HashSet;

public class BankStatement {
    private int id;
    private String transactionReferenceNumber;
    private String accountNumber;
    private String statementNumber;
    private FileDescription fileDescription;
    private int lastUpdatedUser;
    private LocalDate uploadDate;
    private HashSet<Balance> forwardAvailableBalances;
    private Balance closingAvailableBalance;
    private Balance closingBalance;
    private Balance openingBalance;

    public BankStatement(int id, String transactionReferenceNumber, String accountNumber, String statementNumber, FileDescription fileDescription, int lastUpdatedUser, LocalDate uploadDate) {
        this.id = id;
        this.transactionReferenceNumber = transactionReferenceNumber;
        this.accountNumber = accountNumber;
        this.statementNumber = statementNumber;
        this.fileDescription = fileDescription;
        this.lastUpdatedUser = lastUpdatedUser;
        this.uploadDate = uploadDate;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionReferenceNumber() {
        return this.transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatementNumber() {
        return this.statementNumber;
    }

    public void setStatementNumber(String statementNumber) {
        this.statementNumber = statementNumber;
    }

    public FileDescription getFileDescription() {
        return this.fileDescription;
    }

    public void setFileDescription(FileDescription fileDescription) {
        this.fileDescription = fileDescription;
    }

    public int getLastUpdatedUser() {
        return this.lastUpdatedUser;
    }

    public void setLastUpdatedUser(int lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public LocalDate getUploadDate() {
        return this.uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public HashSet<Balance> getForwardAvailableBalances() {
        return this.forwardAvailableBalances;
    }

    public void addForwardAvailableBalance(Balance forwardAvailableBalance){
        this.forwardAvailableBalances.add(forwardAvailableBalance);
    }

    public Balance getClosingAvailableBalance() {
        return this.closingAvailableBalance;
    }

    public void setClosingAvailableBalance(Balance closingAvailableBalance) {
        this.closingAvailableBalance = closingAvailableBalance;
    }

    public Balance getClosingBalance() {
        return this.closingBalance;
    }

    public void setClosingBalance(Balance closingBalance) {
        this.closingBalance = closingBalance;
    }

    public Balance getOpeningBalance() {
        return this.openingBalance;
    }

    public void setOpeningBalance(Balance openingBalance) {
        this.openingBalance = openingBalance;
    }
}
