package com.quintor.api.controllers;

import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.postgresql.ConnectionPostgres;
import com.quintor.api.toJson.ToJSON;
import com.quintor.api.toXml.ToXML;
import com.quintor.api.validators.JSONSchemaValidator;
import com.quintor.api.validators.XMLSchemaValidator;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/transaction/")
public class TransactionController {

    /**
     * This method can be called when you want all transaction from the postgresql database in JSON format
     *
     * @return a list with all the transactions
     */
    @GetMapping("getAllTransactionsJSON")
    public String findAll() {
        try {
            String allTransactions = ToJSON.transactionsToJson(ConnectionPostgres.getAllTransactions()).toString();
            JSONSchemaValidator validator = new JSONSchemaValidator();
            return validator.compareToSchema(allTransactions, "transactionsDbSchema");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method can be called when you want all transactions from the postgresql database in XML format
     *
     * @return a list with all the transactions
     */
    @GetMapping("getAllTransactionsXML")
    public String findAllTransactionsXML() {
        try {
            String allTransactions = ToXML.transactionsToXML(ConnectionPostgres.getAllTransactions());
            XMLSchemaValidator validator = new XMLSchemaValidator();
            return validator.compareToSchema(allTransactions, "transactionsDbSchema");
        } catch (SQLException | IOException | SAXException e) {
            return e.getMessage();
        }
    }
}
