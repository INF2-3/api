package com.quintor.api.controllers;

import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.postgresql.ConnectionPostgres;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/transaction/")
public class TransactionController {

    @GetMapping("getAllTransactions")
    public List<Transaction> findAll() {
        try {
            return ConnectionPostgres.getAllTransactions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
