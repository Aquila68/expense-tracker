package com.learning.expensetracker.services;

import com.learning.expensetracker.exceptions.EtBadRequestException;
import com.learning.expensetracker.exceptions.EtResourceNotFoundException;
import com.learning.expensetracker.models.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> fetchAllTransactions(String userId,String categoryid) throws EtResourceNotFoundException;

    Transaction addTransaction(Transaction transaction) throws EtBadRequestException;

    void updateTransaction(Transaction transaction) throws EtBadRequestException;

    void removeTransaction(int transactionId, String userId, String categoryId) throws EtResourceNotFoundException;

    Transaction fetchTransactionById(String userId, String categoryid, int transactionId) throws EtResourceNotFoundException;
}
