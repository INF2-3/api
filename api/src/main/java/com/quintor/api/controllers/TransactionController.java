package com.quintor.api.controllers;

import com.quintor.api.postgresql.ConnectionPostgres;
import com.quintor.api.toJson.ToJSON;
import com.quintor.api.toXml.ToXML;
import com.quintor.api.validators.JSONSchemaValidator;
import com.quintor.api.validators.XMLSchemaValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("api/transaction/")
public class TransactionController {

    /**
     * This method can be called when you want all transaction from the postgresql database in JSON format
     *
     * @return a list with all the transactions
     */
    @GetMapping("getAllTransactionsJSON")
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

    /**
     * This method can be called when you want all transactions from the postgresql database in XML format
     *
     * @return a list with all the transactions
     */
    @GetMapping("getAllTransactionsXML")
    public ResponseEntity<String> findAllTransactionsXML() {
        try {
            String allTransactions = ToXML.transactionsToXML(ConnectionPostgres.getAllTransactions());
            XMLSchemaValidator validator = new XMLSchemaValidator();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(validator.compareToSchema(allTransactions, "transactionsDbSchema"));
        } catch (SQLException | IOException | SAXException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
