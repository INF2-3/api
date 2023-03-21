package com.quintor.api.description;

public class Description {
    private int descriptionId;
    private String returnReason;
    private String clientReference;
    private String endToEndReference;
    private String paymentInformationId;
    private String instructionId;
    private String mandateReference;
    private String creditorId;
    private String counterpartyId;
    private String remittanceInformation;
    private String purposeCode;
    private String ultimateCreditor;
    private String ultimateDebitor;
    private String exchangeRate;
    private String charges;

    public Description(int descriptionId) {
        setDescriptionId(descriptionId);
    }

    public int getDescriptionId() {
        return this.descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getReturnReason() {
        return this.returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getClientReference() {
        return this.clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public String getEndToEndReference() {
        return this.endToEndReference;
    }

    public void setEndToEndReference(String endToEndReference) {
        this.endToEndReference = endToEndReference;
    }

    public String getPaymentInformationId() {
        return this.paymentInformationId;
    }

    public void setPaymentInformationId(String paymentInformationId) {
        this.paymentInformationId = paymentInformationId;
    }

    public String getInstructionId() {
        return this.instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getMandateReference() {
        return this.mandateReference;
    }

    public void setMandateReference(String mandateReference) {
        this.mandateReference = mandateReference;
    }

    public String getCreditorId() {
        return this.creditorId;
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public String getCounterpartyId() {
        return this.counterpartyId;
    }

    public void setCounterpartyId(String counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public String getRemittanceInformation() {
        return this.remittanceInformation;
    }

    public void setRemittanceInformation(String remittanceInformation) {
        this.remittanceInformation = remittanceInformation;
    }

    public String getPurposeCode() {
        return this.purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getUltimateCreditor() {
        return this.ultimateCreditor;
    }

    public void setUltimateCreditor(String ultimateCreditor) {
        this.ultimateCreditor = ultimateCreditor;
    }

    public String getUltimateDebitor() {
        return this.ultimateDebitor;
    }

    public void setUltimateDebitor(String ultimateDebitor) {
        this.ultimateDebitor = ultimateDebitor;
    }

    public String getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCharges() {
        return this.charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }
}
