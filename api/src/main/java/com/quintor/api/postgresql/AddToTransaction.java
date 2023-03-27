package com.quintor.api.postgresql;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.DebitOrCredit;
import com.quintor.api.dataobjects.Description;
import com.quintor.api.dataobjects.Transaction;

import java.time.LocalDate;

public class AddToTransaction {
    public static Transaction makeTransaction(int id, LocalDate valueDate, int entryDate, String debCred, double amount, String transactionCode, String referenceOwner, String institutionReference, String supplementaryDetails, Description originalDescription, String description, int fileId, Category category) {
        DebitOrCredit debitOrCredit;
        if (debCred.equals("c") || debCred.equals("C")) {
            debitOrCredit = DebitOrCredit.CREDIT;
        } else if (debCred.equals("d") || debCred.equals("D")) {
            debitOrCredit = DebitOrCredit.DEBIT;
        } else {
            debitOrCredit = null;
        }

        Transaction transaction = new Transaction(id, valueDate, entryDate, debitOrCredit, amount, transactionCode, originalDescription, fileId, category);
        transaction.setReferenceOwner(referenceOwner);
        transaction.setInstitutionReference(institutionReference);
        transaction.setSupplementaryDetails(supplementaryDetails);
        transaction.setDescription(description);
        return transaction;
    }

    public static Description makeDescription(int id, String returnReason, String clientReference, String endToEndReference,
                                              String paymentInformationId, String instructionId, String mandateReference,
                                              String creditorId, String counterpartyId, String remittanceInformation,
                                              String purposeCode, String ultimateCreditor, String ultimateDebitor,
                                              String exchangeRate, String charges) {
        Description description = new Description(id);
        description.setReturnReason(returnReason);
        description.setClientReference(clientReference);
        description.setEndToEndReference(endToEndReference);
        description.setPaymentInformationId(paymentInformationId);
        description.setInstructionId(instructionId);
        description.setMandateReference(mandateReference);
        description.setCreditorId(creditorId);
        description.setCounterpartyId(counterpartyId);
        description.setRemittanceInformation(remittanceInformation);
        description.setPurposeCode(purposeCode);
        description.setUltimateCreditor(ultimateCreditor);
        description.setUltimateDebitor(ultimateDebitor);
        description.setExchangeRate(exchangeRate);
        description.setCharges(charges);
        return description;
    }

    public static Category makeCategory(int categoryId, String categoryName){
        return new Category(categoryId, categoryName);

    }
}
