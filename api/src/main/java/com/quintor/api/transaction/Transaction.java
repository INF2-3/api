package com.quintor.api.transaction;

import com.quintor.api.category.Category;
import com.quintor.api.description.Description;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
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
    private Description originalDescription;
    private String description;
    private int fileId;
    private Category category;

    public Transaction(int id, LocalDate valueDate, int entryDate, DebitOrCredit debitOrCredit, double amount, String transactionCode, Description originalDescription, int fileId, Category category) {
        setId(id);
        setValueDate(valueDate);
        setEntryDate(entryDate);
        setDebitOrCredit(debitOrCredit);
        setAmount(amount);
        setTransactionCode(transactionCode);
        setOriginalDescription(originalDescription);
        setFileId(fileId);
        setCategory(category);
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

    public Description getOriginalDescription() {
        return this.originalDescription;
    }

    public void setOriginalDescription(Description originalDescription) {
        if (originalDescription == null) {
            return;
        }
        this.originalDescription = originalDescription;
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

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
