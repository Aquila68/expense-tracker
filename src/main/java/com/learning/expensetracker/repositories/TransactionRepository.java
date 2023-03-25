package com.learning.expensetracker.repositories;

import com.learning.expensetracker.models.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
}
