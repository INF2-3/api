package com.quintor.api.controllers;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.dataobjects.Description;
import com.quintor.api.postgresql.ConnectionPostgres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
