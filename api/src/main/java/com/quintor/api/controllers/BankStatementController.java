package com.quintor.api.controllers;

import com.quintor.api.toJson.ToJSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/bankStatement/")
public class BankStatementController {
    @GetMapping("allBankStatements")
    public String allBankStatements(){
        try ToJSON.bankStatementsToJson();
    }
}
