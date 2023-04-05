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
    private static final String url = System.getenv("POSTGRESQL_URL");
    private static final String user = System.getenv("POSTGRESQL_USER");
    private static final String password = System.getenv("POSTGRESQL_PASSWORD");

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
            int id = result.getInt("t_id");
            LocalDate valueDate = result.getDate("t_value_date").toLocalDate();
            String entryDate = result.getString("t_entry_date");
            String debCred = result.getString("t_debit_credit");
            double amount = result.getDouble("t_amount");
            String transactionCode = result.getString("t_transaction_code");
            String referenceOwner = result.getString("t_reference_owner");
            String institutionReference = result.getString("t_institution_reference");
            String supplementaryDetails = result.getString("t_supplementary_details");
            int originalDescriptionId = result.getInt("t_original_description_id");
            String description = result.getString("t_description");
            int fileId = result.getInt("t_file_id");
            int categoryId = result.getInt("t_category_id");

            String categoryName = result.getString("c_name");
            String returnReason = result.getString("d_return_reason");
            String clientReference = result.getString("d_client_reference");
            String endToEndReference = result.getString("d_end_to_end_reference");
            String paymentInformationId = result.getString("d_payment_information_id");
            String instructionId = result.getString("d_instruction_id");
            String mandateReference = result.getString("d_mandate_reference");
            String creditorId = result.getString("d_creditor_id");
            String counterpartyId = result.getString("d_counterparty_id");
            String remittanceInformation = result.getString("d_remittance_information");
            String purposeCode = result.getString("d_purpose_code");
            String ultimateCreditor = result.getString("d_ultimate_creditor");
            String ultimateDebtor = result.getString("d_ultimate_debtor");
            String exchangeRate = result.getString("d_exchange_rate");
            String charges = result.getString("d_charges");

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
        return allTransactions;
    }
}
