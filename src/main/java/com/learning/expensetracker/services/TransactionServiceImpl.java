package com.learning.expensetracker.services;


import com.learning.expensetracker.exceptions.EtBadRequestException;
import com.learning.expensetracker.exceptions.EtResourceNotFoundException;
import com.learning.expensetracker.models.Transaction;
import com.learning.expensetracker.repositories.TransactionRepositoryOriginal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepositoryOriginal transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(String userId, String categoryid) throws EtResourceNotFoundException {
        return transactionRepository.findAll(userId,categoryid);
    }

    @Override
    public Transaction addTransaction(Transaction transaction) throws EtBadRequestException {
        return transactionRepository.create(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction) throws EtBadRequestException {
        transactionRepository.updateTransaction(transaction);
    }

    @Override
    public void removeTransaction(int transactionId, String userId, String categoryId) throws EtResourceNotFoundException {
        transactionRepository.remove(userId, categoryId, transactionId);
    }

    @Override
    public Transaction fetchTransactionById(String userId, String categoryid, int transactionId) throws EtResourceNotFoundException {
        return transactionRepository.findById(userId,categoryid,transactionId);
    }
}
