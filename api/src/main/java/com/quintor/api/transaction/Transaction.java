package com.quintor.api.transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Transaction {
    @Id
    @Column
    private int id;
    private LocalDate valueDate;
    private int entryDate;
    private DebitOrCredit debitOrCredit;
    private double amount;
    private String transactionCode;
    private String referenceOwner;
    private String institutionReference;
    private String supplementaryDetails;
    private int originalDescriptionId;
    private String description;
    private int fileId;
    private int categoryId;

    public Transaction(int id, LocalDate valueDate, int entryDate, DebitOrCredit debitOrCredit, double amount, String transactionCode, int originalDescriptionId, int fileId, int categoryId) {
        setId(id);
        setValueDate(valueDate);
        setEntryDate(entryDate);
        setDebitOrCredit(debitOrCredit);
        setAmount(amount);
        setTransactionCode(transactionCode);
        setOriginalDescriptionId(originalDescriptionId);
        setFileId(fileId);
        setCategoryId(categoryId);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getValueDate() {
        return this.valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public int getEntryDate() {
        return this.entryDate;
    }

    public void setEntryDate(int entryDate) {
        this.entryDate = entryDate;
    }

    public DebitOrCredit getDebitOrCredit() {
        return this.debitOrCredit;
    }

    public void setDebitOrCredit(DebitOrCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionCode() {
        return this.transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        if (transactionCode == null || transactionCode.isEmpty()) {
            return;
        }
        this.transactionCode = transactionCode;
    }

    public String getReferenceOwner() {
        return this.referenceOwner;
    }

    public void setReferenceOwner(String referenceOwner) {
        if (referenceOwner == null || referenceOwner.isEmpty()) {
            return;
        }
        this.referenceOwner = referenceOwner;
    }

    public String getInstitutionReference() {
        return this.institutionReference;
    }

    public void setInstitutionReference(String institutionReference) {
        if (institutionReference == null || institutionReference.isEmpty()) {
            return;
        }
        this.institutionReference = institutionReference;
    }

    public String getSupplementaryDetails() {
        return this.supplementaryDetails;
    }

    public void setSupplementaryDetails(String supplementaryDetails) {
        if (supplementaryDetails == null || supplementaryDetails.isEmpty()) {
            return;
        }
        this.supplementaryDetails = supplementaryDetails;
    }

    public int getOriginalDescriptionId() {
        return this.originalDescriptionId;
    }

    public void setOriginalDescriptionId(int originalDescriptionId) {
        this.originalDescriptionId = originalDescriptionId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            return;
        }
        this.description = description;
    }

    public int getFileId() {
        return this.fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
