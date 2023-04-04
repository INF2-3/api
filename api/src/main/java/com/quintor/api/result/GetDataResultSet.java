package com.quintor.api.result;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.Description;
import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.postgresql.AddToTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GetDataResultSet {
    public static Transaction getTransactionFromResultSet(ResultSet result, Description originalDescription, Category category) throws SQLException {
        int id = result.getInt("c_id");
        LocalDate valueDate = result.getDate("value_date").toLocalDate();
        int entryDate = result.getInt("entry_date");
        String debCred = result.getString("debit_credit");
        double amount = result.getDouble("amount");
        String transactionCode = result.getString("transaction_code");
        String referenceOwner = result.getString("reference_owner");
        String institutionReference = result.getString("institution_reference");
        String supplementaryDetails = result.getString("supplementary_details");
        String description = result.getString("description");
        int fileId = result.getInt("file_id");

        return AddToTransaction.makeTransaction(id, valueDate, entryDate, debCred, amount, transactionCode, referenceOwner,
                institutionReference, supplementaryDetails, originalDescription, description, fileId, category);
    }

    public static Description getDescriptionFromResultSet(ResultSet result) throws SQLException {
        int originalDescriptionId = result.getInt("original_description_id");
        String returnReason = result.getString("return_reason");
        String clientReference = result.getString("client_reference");
        String endToEndReference = result.getString("end_to_end_reference");
        String paymentInformationId = result.getString("payment_information_id");
        String instructionId = result.getString("instruction_id");
        String mandateReference = result.getString("mandate_reference");
        String creditorId = result.getString("creditor_id");
        String counterpartyId = result.getString("counterparty_id");
        String remittanceInformation = result.getString("remittance_information");
        String purposeCode = result.getString("purpose_code");
        String ultimateCreditor = result.getString("ultimate_creditor");
        String ultimateDebtor = result.getString("ultimate_debtor");
        String exchangeRate = result.getString("exchange_rate");
        String charges = result.getString("charges");

        return AddToTransaction.makeDescription(originalDescriptionId, returnReason, clientReference, endToEndReference,
                paymentInformationId, instructionId, mandateReference, creditorId, counterpartyId, remittanceInformation,
                purposeCode, ultimateCreditor, ultimateDebtor, exchangeRate, charges);
    }

    public static Category getCategoryFromResultSet(ResultSet result) throws SQLException {
        int categoryId = result.getInt("category_id");
        String categoryName = result.getString("name");

        return AddToTransaction.makeCategory(categoryId, categoryName);
    }
}
