package com.quintor.api.postgresql;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.DebitOrCredit;
import com.quintor.api.dataobjects.Description;
import com.quintor.api.dataobjects.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        String sql = "SELECT * FROM transaction";
        ResultSet result = ConnectionPostgres.createConnection(sql);
        while (result.next()) {
            int id = result.getInt("id");
            LocalDate valueDate = result.getDate("valueDate").toLocalDate();
            int entryDate = result.getInt("entryDate");
            String debCred = result.getString("debit/credit");
            double amount = result.getDouble("amount");
            String transactionCode = result.getString("transactionCode");
            String referenceOwner = result.getString("referenceOwner");
            String institutionReference = result.getString("institutionReference");
            String supplementaryDetails = result.getString("supplementaryDetails");
            int originalDescriptionId = result.getInt("originalDescriptionId");
            String description = result.getString("description");
            int fileId = result.getInt("fileId");
            int categoryId = result.getInt("categoryId");

            Transaction transaction = AddToTransaction.makeTransaction(id, valueDate, entryDate, debCred, amount,
                    transactionCode, referenceOwner, institutionReference, supplementaryDetails, originalDescriptionId,
                    description, fileId, categoryId);


            allTransactions.add(transaction);
        }

        return allTransactions;

    }

}
