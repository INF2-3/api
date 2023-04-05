package com.quintor.api.dataobjects;

import java.time.LocalDate;
import java.util.HashSet;

public class BankStatement {
    private int id;
    private String transactionReferenceNumber;
    private String accountNumber;
    private int statementNumber;
    private FileDescription fileDescription;
    private User lastUpdatedUser;
    private LocalDate uploadDate;
    private HashSet<Balance> forwardAvailableBalances;
    private Balance closingAvailableBalance;
    private Balance closingBalance;
    private Balance openingBalance;

    public BankStatement(int id, String transactionReferenceNumber, String accountNumber, int statementNumber, FileDescription fileDescription, User lastUpdatedUser, LocalDate uploadDate) {
        setId(id);
        setTransactionReferenceNumber(transactionReferenceNumber);
        setAccountNumber(accountNumber);
        setStatementNumber(statementNumber);
        setFileDescription(fileDescription);
        setLastUpdatedUser(lastUpdatedUser);
        setUploadDate(uploadDate);
        this.forwardAvailableBalances = new HashSet<>();
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

    public int getStatementNumber() {
        return this.statementNumber;
    }

    public void setStatementNumber(int statementNumber) {
        this.statementNumber = statementNumber;
    }

    public FileDescription getFileDescription() {
        return this.fileDescription;
    }

    public void setFileDescription(FileDescription fileDescription) {
        this.fileDescription = fileDescription;
    }

    public User getLastUpdatedUser() {
        return this.lastUpdatedUser;
    }

    public void setLastUpdatedUser(User lastUpdatedUser) {
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

    public void addForwardAvailableBalance(Balance forwardAvailableBalance) {
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
