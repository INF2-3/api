//package com.quintor.api.service;
//
//import com.quintor.api.dataobjects.Transaction;
//import com.quintor.api.repository.TransactionRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TransactionService {
//    private final TransactionRepository transactionRepository;
//
//    public TransactionService(TransactionRepository transactionRepository) {
//        this.transactionRepository = transactionRepository;
//    }
//
//    public void findAllTransactions() {
//        for (Transaction transaction : transactionRepository.findAll()) {
//            System.out.println(transaction.toString());
//
//        }
////        return (List<Transaction>) transactionRepository.findAll();
//    }
//}
