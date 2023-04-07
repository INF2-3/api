package com.quintor.api.controllers;

import com.quintor.api.postgresql.ConnectionPostgres;
import com.quintor.api.toXml.ToXML;
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
@RequestMapping("api/xml/")
public class XMLController {
    /**
     * This method can be called when you want all the bankStatements from the postgresql database in XML format
     *
     * @return a xml String with all the BankStatements.
     */
    @GetMapping("getAllBankStatements")
    public ResponseEntity<String> allBankStatementsXML() {
        try {
            String allBankStatements = ToXML.bankStatementsToXML(ConnectionPostgres.getAllBankStatements());
            XMLSchemaValidator validator = new XMLSchemaValidator();
            System.out.println(allBankStatements);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(validator.compareToSchema(allBankStatements, "bankStatementDbSchema"));
        } catch (SQLException | SAXException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * This method can be called when you want all transactions from the postgresql database in XML format
     *
     * @return a list with all the transactions
     */
    @GetMapping("getAllTransactions")
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
