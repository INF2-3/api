package com.quintor.api.postgresql;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.Description;
import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.result.GetDataResultSet;
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

    private static ResultSet preparedStatementWithInt(String sql, int id) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (PreparedStatement getTransaction = connection.prepareStatement(sql)) {
                getTransaction.setInt(1, id);
//                connection.commit();
                return getTransaction.executeQuery();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            Description originalDescription = GetDataResultSet.getDescriptionFromResultSet(result);
            Category category = GetDataResultSet.getCategoryFromResultSet(result);

            Transaction transaction = GetDataResultSet.getTransactionFromResultSet(result, originalDescription, category);

            allTransactions.add(transaction);
        }
        return allTransactions;
    }

    public static Transaction getTransactionById(int id) throws SQLException {
        String sql = "SELECT * FROM transactionsview WHERE t_id=?";
        ResultSet result = ConnectionPostgres.preparedStatementWithInt(sql, id);

        if (result.next()) {
            Description description = GetDataResultSet.getDescriptionFromResultSet(result);
            Category category = GetDataResultSet.getCategoryFromResultSet(result);
            return GetDataResultSet.getTransactionFromResultSet(result, description, category);
        }
        return null;
    }
}
