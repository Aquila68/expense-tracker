package com.learning.expensetracker.repositories;

import com.learning.expensetracker.exceptions.EtBadRequestException;
import com.learning.expensetracker.exceptions.EtResourceNotFoundException;
import com.learning.expensetracker.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepositoryOriginal {

    private static final String EXCEPTION_NO_TRANSACTION_ID="Specific transaction id does not exists";
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_BY_ID="SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE," +
            " TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE TRANSACTION_ID=? AND CATEGORY_ID=? AND USER_ID=?;";

    private static final String SQL_INSERT="INSERT INTO ET_TRANSACTIONS (TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT," +
            " NOTE, TRANSACTION_DATE ) VALUES (NEXTVAL('ET_TRANSACTIONS_SEQ'),?,?,?,?,?) ;";

    private static final String SQL_UPDATE = "UPDATE ET_TRANSACTIONS SET AMOUNT=?, NOTE=?, TRANSACTION_DATE=? WHERE "+
            " TRANSACTION_ID=? AND USER_ID=? AND CATEGORY_ID=?;";

   private static final String SQL_FIND_ALL = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE," +
           " TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE CATEGORY_ID=? AND USER_ID=?;";

   private static final String SQL_DELETE = "DELETE FROM ET_TRANSACTIONS WHERE "+
           " TRANSACTION_ID=? AND USER_ID=? AND CATEGORY_ID=?;";

   private static final String SQL_DELETE_ALL_FOR_CATEGORY="DELETE FROM ET_TRANSACTIONS WHERE "+
           " USER_ID=? AND CATEGORY_ID=?";

    public void updateTransaction(Transaction transaction) throws EtBadRequestException{
        try {
            int rows = jdbcOperations.update(SQL_UPDATE, transaction.getAmount(),
                    transaction.getNote(),
                    transaction.getTransactionDate(),
                    transaction.getTransactionId(),
                    transaction.getUserId(),
                    transaction.getCategoryId());
            if(rows==0){
                throw new EtBadRequestException(EXCEPTION_NO_TRANSACTION_ID);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new EtBadRequestException(EXCEPTION_NO_TRANSACTION_ID);
        }
    }

    public List<Transaction> findAll(String userId,String categoryId){
        return jdbcOperations.query(SQL_FIND_ALL,transactionRowMapper,categoryId,userId);
    }

    public Transaction findById(String userId,String categoryId,int transactionId) throws EtResourceNotFoundException{
        try{
            return jdbcOperations.queryForObject(SQL_FIND_BY_ID,transactionRowMapper,transactionId,categoryId,userId);
        }
        catch (Exception e){
            throw new EtResourceNotFoundException("No such transaction exists");
        }
    }

    public Transaction create(Transaction transaction) throws EtBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps=con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1,transaction.getCategoryId());
                    ps.setString(2,transaction.getUserId());
                    ps.setDouble(3,transaction.getAmount());
                    ps.setString(4,transaction.getNote());
                    ps.setLong(5,transaction.getTransactionDate());
                    return ps;
                },
            keyHolder);
            Map<String,Object> keys=keyHolder.getKeys();
            if(keys!=null){
                transaction.setTransactionId((Integer)keys.get("TRANSACTION_ID"));
                return transaction;
            }
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            throw new EtBadRequestException("Bad SQL query");
        }
    }

    public void remove(String userId,String categoryId,int transactionId) throws EtResourceNotFoundException{
        try {
            int rows = jdbcOperations.update(SQL_DELETE, transactionId,
                    userId,
                    categoryId);
            if(rows==0){
                throw new EtBadRequestException(EXCEPTION_NO_TRANSACTION_ID);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new EtBadRequestException(EXCEPTION_NO_TRANSACTION_ID);
        }
    }

    public void removeTransactionsForCategory(String userId,String categoryId) throws EtBadRequestException{
        try {
            jdbcOperations.update(SQL_DELETE_ALL_FOR_CATEGORY, userId,
                    categoryId);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new EtBadRequestException("Bad Request");
        }
    }


    private final RowMapper<Transaction> transactionRowMapper=new RowMapper<Transaction>() {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Transaction(rs.getInt("transaction_id"),
                    rs.getString("category_id"),
                    rs.getString("user_id"),
                    rs.getInt("amount"),
                    rs.getString("note"),
                    rs.getLong("transaction_date"));
        }
    };

}
