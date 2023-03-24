package com.quintor.api.postgresql;

import com.quintor.api.dataobjects.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPostgres {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "root";
    private static final String password = "changeme";


    private static ResultSet createConnection(String sql) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> allTransactions = new ArrayList<>();

        String sql = "SELECT * FROM transaction INNER JOIN category ON transaction.category_id = category.id INNER JOIN description ON transaction.original_description_id = description.id";
        ResultSet result = ConnectionPostgres.createConnection(sql);
        while (result.next()) {
            int id = result.getInt("id");
            LocalDate valueDate = result.getDate("value_date").toLocalDate();
            int entryDate = result.getInt("entry_date");
            String debCred = result.getString("debit/credit");
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


            System.out.println(categoryName);

            Transaction transaction = AddToTransaction.makeTransaction(id, valueDate, entryDate, debCred, amount,
                    transactionCode, referenceOwner, institutionReference, supplementaryDetails, originalDescriptionId,
                    description, fileId, categoryId);


            allTransactions.add(transaction);
        }

        return allTransactions;

    }

}
