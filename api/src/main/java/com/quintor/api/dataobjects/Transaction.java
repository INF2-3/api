package com.quintor.api.dataobjects;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "valueDate")
    private LocalDate valueDate;
    @Column(name = "entryDate")
    private int entryDate;
    @Column(name = "debit/credit")
    private String debitOrCredit;
    @Column(name = "amount")
    private double amount;
    @Column(name = "transactionCode")
    private String transactionCode;
    @Column(name = "referenceOwner")
    private String referenceOwner;
    @Column(name = "institutionReference")
    private String institutionReference;
    @Column(name = "supplementaryDetails")
    private String supplementaryDetails;
    @Column(name = "originalDescriptionId")
    private int originalDescriptionId;
    private Description originalDescription;
    @Column(name = "description")
    private String description;
    @Column(name = "fileId")
    private int fileId;
    @Column(name = "categoryId")
    private int categoryId;
    private Category category;

    public Transaction(int id, LocalDate valueDate, int entryDate, String debitOrCredit, double amount, String transactionCode, Description originalDescription, int fileId, Category category) {
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

    public String getDebitOrCredit() {
        return this.debitOrCredit;
    }

    public void setDebitOrCredit(String debitOrCredit) {
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

    @Override
    public String toString(){
        return "Id" + getId();
    }
}
