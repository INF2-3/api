package com.quintor.api.toJson;

import com.quintor.api.dataobjects.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ToJSON {
    /**
     * Puts the list of transactions in a JSONArray and then returns this array.
     *
     * @param allTransactions the list of transactions you want to change to JSON
     * @return A JSONArray.
     */
    public static JSONArray transactionsToJson(List<Transaction> allTransactions) {
        return new JSONArray(allTransactions);
    }

    public static JSONObject transactionToJSON(Transaction transaction) {
        return new JSONObject(transaction);
    }
}
