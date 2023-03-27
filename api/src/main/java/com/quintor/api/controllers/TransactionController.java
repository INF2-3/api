package com.quintor.api.controllers;

import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.postgresql.ConnectionPostgres;
import com.quintor.api.toXml.ToXML;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/transaction/")
public class TransactionController {

    @GetMapping("getAllTransactionsJSON")
    public List<Transaction> findAll() {
        try {
            return ConnectionPostgres.getAllTransactions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("getAllTransactionsXML")
    public String findAllTransactionsXML() {
        try {
            List<Transaction> allTransactions = ConnectionPostgres.getAllTransactions();
            return ToXML.transactionsToXML(allTransactions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
