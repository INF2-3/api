package com.quintor.api.transaction;

import com.quintor.api.dataobjects.Category;
import com.quintor.api.dataobjects.DebitOrCredit;
import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.dataobjects.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/transaction/")
public class TransactionController {

    @GetMapping("/getAllTransactions")
    public List<Transaction> listAll() {
        List<Transaction> test = new ArrayList<>();
        Description description = new Description(1);
        Category leden  = new Category(1, "Leden");
        Category test1 = new Category(3, "Test");
        Transaction transaction1 = new Transaction(1, LocalDate.now(), 25, DebitOrCredit.CREDIT, 25, "test", description, 54, leden);
        description.setCharges("test");

        transaction1.setOriginalDescription(description);
        test.add(transaction1);
        test.add(new Transaction(2, LocalDate.now(), 25, DebitOrCredit.CREDIT, 25, "test", description, 54, test1));
        test.add(new Transaction(3, LocalDate.now(), 25, DebitOrCredit.CREDIT, 25, "test", description, 54, leden));

        return test;
    }

    @GetMapping("all")
    public void findAll() throws Exception {



    }



}
