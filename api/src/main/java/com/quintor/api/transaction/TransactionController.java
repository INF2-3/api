package com.quintor.api.transaction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/transaction/")
public class TransactionController {
//    private final TransactionService transactionService;
//
//    public TransactionController(TransactionService transactionService) {
//        this.transactionService = transactionService;
//    }
//
//    public TransactionService getTransactionService(){
//        return this.transactionService;
//    }


    @GetMapping("/getAllTransactions")
    public List<Transaction> listAll() {
        List<Transaction> test = new ArrayList<>();
        test.add(new Transaction(1, LocalDate.now(), 25, DebitOrCredit.CREDIT, 25, "test", 33, 54, 43));
        test.add(new Transaction(2, LocalDate.now(), 25, DebitOrCredit.CREDIT, 25, "test", 33, 54, 43));
        test.add(new Transaction(3, LocalDate.now(), 25, DebitOrCredit.CREDIT, 25, "test", 33, 54, 43));

        return test;
    }


}
