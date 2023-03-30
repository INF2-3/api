package com.quintor.api.postgresql;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.Description;
import com.quintor.api.dataobjects.Transaction;
import org.json.JSONArray;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPostgres {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "root";
    private static final String password = "changeme";

    /**
     * Creates the connection with a sql query.
     *
     * @param sql the query that has to be executed.
     * @return a ResultSet with the results of the query
     */
    private static ResultSet createConnection(String sql) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Get all the transaction from the database with a view.
     * Loops over the results and makes the transaction, description and category.
     * Then adds the transaction to the List with all the transactions
     *
     * @return A list with all the transactions
     * @throws SQLException can throw SQLException.
     */
    public static List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> allTransactions = new ArrayList<>();

        String sql = "SELECT * FROM transactionsview";
        ResultSet result = ConnectionPostgres.createConnection(sql);

        while (result.next()) {
            int id = result.getInt("c_id");
            LocalDate valueDate = result.getDate("value_date").toLocalDate();
            int entryDate = result.getInt("entry_date");
            String debCred = result.getString("debit_credit");
            double amount = result.getDouble("amount");
            String transactionCode = result.getString("transaction_code");
            String referenceOwner = result.getString("reference_owner");
            String institutionReference = result.getString("institution_reference");
            String supplementaryDetails = result.getString("supplementary_details");
            int originalDescriptionId = result.getInt("original_description_id");
            String description = result.getString("description");
            int fileId = result.getInt("file_id");
            int categoryId = result.getInt("category_id");

            String categoryName = result.getString("name");
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

            Description originalDescription = AddToTransaction.makeDescription(originalDescriptionId, returnReason,
                    clientReference, endToEndReference, paymentInformationId, instructionId, mandateReference, creditorId,
                    counterpartyId, remittanceInformation, purposeCode, ultimateCreditor, ultimateDebtor, exchangeRate,
                    charges);
            Category category = AddToTransaction.makeCategory(categoryId, categoryName);

            Transaction transaction = AddToTransaction.makeTransaction(id, valueDate, entryDate, debCred, amount,
                    transactionCode, referenceOwner, institutionReference, supplementaryDetails, originalDescription,
                    description, fileId, category);


            allTransactions.add(transaction);
        }
        JSONArray jsonArray = new JSONArray(allTransactions);
        return allTransactions;
    }
}
