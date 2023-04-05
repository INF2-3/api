package com.quintor.api.controllers;

import com.quintor.api.dataobjects.BankStatement;
import com.quintor.api.postgresql.ConnectionPostgres;
import com.quintor.api.toJson.ToJSON;
import com.quintor.api.toXml.ToXML;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/bankStatement/")
public class BankStatementController {
    @GetMapping("allBankStatementsJSON")
    public String allBankStatementsJSON() {
        try {
            return ToJSON.bankStatementsToJson(ConnectionPostgres.getAllBankStatements()).toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("allBankStatementsXML")
    public String allBankStatementsXML() {
        try {
            List<BankStatement> allBankStatements = ConnectionPostgres.getAllBankStatements();
            return ToXML.bankStatementsToXML(allBankStatements);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
