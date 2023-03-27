package com.quintor.api.toJson;

import com.quintor.api.dataobjects.Transaction;
import org.json.JSONArray;

import java.util.List;

public class ToJSON {
    public static JSONArray transactionsToJson(List<Transaction> allTransactions) {
        return new JSONArray(allTransactions);
    }
}
