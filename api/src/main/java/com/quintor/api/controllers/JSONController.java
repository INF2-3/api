package com.quintor.api.controllers;

import com.quintor.api.postgresql.ConnectionPostgres;
import com.quintor.api.toJson.ToJSON;
import com.quintor.api.validators.JSONSchemaValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("api/json/")
public class JSONController {
    /**
     * This method can be called when you want all the bankStatements from the postgresql database in JSON format
     *
     * @return a JSON string with all the bankStatements
     */
    @GetMapping("getAllBankStatements")
    public ResponseEntity<String> allBankStatementsJSON() {
        try {
            String allBankStatements = ToJSON.bankStatementsToJson(ConnectionPostgres.getAllBankStatements()).toString();
            JSONSchemaValidator validator = new JSONSchemaValidator();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(validator.compareToSchema(allBankStatements, "bankStatementDbSchema"));
        } catch (SQLException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * This method can be called when you want all transaction from the postgresql database in JSON format
     *
     * @return a list with all the transactions
     */
    @GetMapping("getAllTransactions")
    public ResponseEntity<String> findAll() {
        try {
            String allTransactions = ToJSON.transactionsToJson(ConnectionPostgres.getAllTransactions()).toString();
            JSONSchemaValidator validator = new JSONSchemaValidator();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(validator.compareToSchema(allTransactions, "transactionsDbSchema"));
        } catch (SQLException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
