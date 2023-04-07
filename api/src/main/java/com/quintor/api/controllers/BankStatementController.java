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
@RequestMapping("api/bankStatement/")
public class BankStatementController {
    /**
     * This method can be called when you want all the bankStatements from the postgresql database in JSON format
     *
     * @return a JSON string with all the bankStatements
     */
    @GetMapping("allBankStatementsJSON")
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
     * This method can be called when you want all the bankStatements from the postgresql database in XML format
     *
     * @return a xml String with all the BankStatements.
     */
    @GetMapping("allBankStatementsXML")
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
}
