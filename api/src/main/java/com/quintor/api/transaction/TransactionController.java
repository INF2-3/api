package com.quintor.api.transaction;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/transaction/")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public TransactionService getTransactionService(){
        return this.transactionService;
    }


    @GetMapping("/getAllTransactions")
    public String listAll() {
        return "test";
    }


}
