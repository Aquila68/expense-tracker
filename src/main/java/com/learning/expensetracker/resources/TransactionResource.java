package com.learning.expensetracker.resources;

import com.learning.expensetracker.models.Transaction;
import com.learning.expensetracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
public class TransactionResource {
    private static final String USERID="USERID";

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path="")
    public ResponseEntity<Transaction> addTransaction(HttpServletRequest request, @RequestBody Map<String,Object> transactionMap,
                                         @PathVariable String categoryId){
        Transaction transaction=new Transaction();
        long date=Long.parseLong(transactionMap.get("transactionDate").toString());

        transaction.setUserId((String)request.getAttribute(USERID));
        transaction.setCategoryId(categoryId);
        transaction.setNote((String)transactionMap.get("note"));
        transaction.setAmount((int)transactionMap.get("amount"));
        transaction.setTransactionDate(date);

        return new ResponseEntity<>(transactionService.addTransaction(transaction), HttpStatus.CREATED);
    }


    @GetMapping(path="/{transactionId}")
    public ResponseEntity<Transaction> fetchTransaction(HttpServletRequest request,@PathVariable int transactionId,
                                                        @PathVariable String categoryId){
        String userId=(String)request.getAttribute(USERID);
        Transaction transaction=transactionService.fetchTransactionById(userId,categoryId,transactionId);

        return new ResponseEntity<>(transaction,HttpStatus.FOUND);
    }


    @PutMapping(path="/{transactionId}")
    public ResponseEntity<String> updateTransaction(HttpServletRequest request,@PathVariable int transactionId,
                                                    @PathVariable String categoryId,
                                                    @RequestBody Map<String,Object> transactionMap) {
        String userId = (String) request.getAttribute(USERID);
        Transaction transaction=new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setUserId(userId);
        transaction.setCategoryId(categoryId);
        transaction.setNote((String) transactionMap.get("note"));
        transaction.setAmount((double)transactionMap.get("amount"));
        transaction.setTransactionDate(Long.parseLong(transactionMap.get("transactionDate").toString()));

        transactionService.updateTransaction(transaction);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    @GetMapping(path="")
    public List<Transaction> fetchAllTransaction(HttpServletRequest request,@PathVariable String categoryId){
        String userId=(String) request.getAttribute(USERID);
        return transactionService.fetchAllTransactions(userId,categoryId);
    }

    @DeleteMapping(path="/{transactionId}")
    public void deleteTransaction(HttpServletRequest request,@PathVariable String categoryId,
                                  @PathVariable int transactionId){
        String userId=(String) request.getAttribute(USERID);
        transactionService.removeTransaction(transactionId,userId,categoryId);
    }
}
