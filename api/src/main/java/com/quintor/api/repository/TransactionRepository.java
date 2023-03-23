package com.quintor.api.repository;

import com.quintor.api.dataobjects.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
//    List<Transaction> findAllById();
}
