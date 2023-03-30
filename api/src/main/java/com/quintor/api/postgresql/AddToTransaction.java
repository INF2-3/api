package com.quintor.api.postgresql;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.DebitOrCredit;
import com.quintor.api.dataobjects.Description;
import com.quintor.api.dataobjects.Transaction;

import java.time.LocalDate;

public class AddToTransaction {
    /**
     * Make a transaction object and sets the fields that can be null.
     *
     * @param id                   the id of the transaction
     * @param valueDate            the value date of the transaction
     * @param entryDate            the entry date of the transaction
     * @param debCred              if the transaction is debit or credit
     * @param amount               the amount of the transaction
     * @param transactionCode      the transaction code
     * @param referenceOwner       the reference for the owner
     * @param institutionReference the institution reference
     * @param supplementaryDetails the supplementary details
     * @param originalDescription  the original description
     * @param description          the optional description
     * @param fileId               id of the file
     * @param category             the category of the transaction
     * @return a Transaction object
     */
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

    /**
     * Make a Description object and sets all the fields that can be null.
     *
     * @param id                    the id of the description
     * @param returnReason          the return reason of the description
     * @param clientReference       the client reference
     * @param endToEndReference     the end to end reference
     * @param paymentInformationId  the id of the payment information
     * @param instructionId         the id of the instruction
     * @param mandateReference      the mandate reference
     * @param creditorId            the id of the creditor
     * @param counterpartyId        the id of the counterparty
     * @param remittanceInformation the remittance information
     * @param purposeCode           the purpose code
     * @param ultimateCreditor      the ultimate creditor
     * @param ultimateDebitor       the ultimate debitor
     * @param exchangeRate          the exchange rate
     * @param charges               the charges
     * @return a Description object
     */
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

    /**
     * Make a Category object and returns this Category
     *
     * @param categoryId   the id of the category
     * @param categoryName the name of the category
     * @return A Category object
     */
    public static Category makeCategory(int categoryId, String categoryName) {
        return new Category(categoryId, categoryName);

    }
}
