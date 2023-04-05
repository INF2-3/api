package com.quintor.api.dataobjects;

public class FileDescription {
    private int id;
    private int numberOfDebitEntries;
    private int numberOfCreditEntries;
    private double amountOfDebitEntries;
    private double amountOfCreditEntries;

    public FileDescription(int id, int numberOfDebitEntries, int numberOfCreditEntries, double amountOfDebitEntries, double amountOfCreditEntries) {
        this.id = id;
        this.numberOfDebitEntries = numberOfDebitEntries;
        this.numberOfCreditEntries = numberOfCreditEntries;
        this.amountOfDebitEntries = amountOfDebitEntries;
        this.amountOfCreditEntries = amountOfCreditEntries;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfDebitEntries() {
        return this.numberOfDebitEntries;
    }

    public void setNumberOfDebitEntries(int numberOfDebitEntries) {
        this.numberOfDebitEntries = numberOfDebitEntries;
    }

    public int getNumberOfCreditEntries() {
        return this.numberOfCreditEntries;
    }

    public void setNumberOfCreditEntries(int numberOfCreditEntries) {
        this.numberOfCreditEntries = numberOfCreditEntries;
    }

    public double getAmountOfDebitEntries() {
        return this.amountOfDebitEntries;
    }

    public void setAmountOfDebitEntries(double amountOfDebitEntries) {
        this.amountOfDebitEntries = amountOfDebitEntries;
    }

    public double getAmountOfCreditEntries() {
        return this.amountOfCreditEntries;
    }

    public void setAmountOfCreditEntries(double amountOfCreditEntries) {
        this.amountOfCreditEntries = amountOfCreditEntries;
    }
}
