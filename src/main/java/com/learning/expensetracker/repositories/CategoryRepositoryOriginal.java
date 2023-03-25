package com.learning.expensetracker.repositories;

import com.learning.expensetracker.exceptions.EtBadRequestException;
import com.learning.expensetracker.exceptions.EtResourceNotFoundException;
import com.learning.expensetracker.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryRepositoryOriginal {

    private static final String SQL_FIND_BY_ID="SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, "
            + "COALESCE(SUM(T.AMOUNT),0) TOTAL_EXPENSE "
            + "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID "
            + "WHERE C.USER_ID = ? AND C.CATEGORY_ID= ? GROUP BY C.CATEGORY_ID;";

    private static final String SQL_FIND_ALL_CATEGORIES_FOR_USER="SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, "
            + "COALESCE(SUM(T.AMOUNT),0) TOTAL_EXPENSE "
            + "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID "
            + "WHERE C.USER_ID = ? GROUP BY C.CATEGORY_ID;";

    private static final String SQL_UPDATE_CATEGORY="UPDATE ET_CATEGORIES SET TITLE=? ,DESCRIPTION=? "
        + "WHERE USER_ID=? AND CATEGORY_ID=?;";

    private static final String SQL_DELETE="DELETE FROM ET_CATEGORIES WHERE USER_ID=? AND "+
            "CATEGORY_ID=?";
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private TransactionRepositoryOriginal transactionRepo;

    public List<Category> findAllCategories(String userId) throws EtResourceNotFoundException {
        return jdbcOperations.query(SQL_FIND_ALL_CATEGORIES_FOR_USER,categoryRowMapper,userId);
    }

    public Category findByCategoryId(String categoryId,String userId) throws EtResourceNotFoundException {
        try{
            return jdbcOperations.queryForObject(SQL_FIND_BY_ID,categoryRowMapper,userId,categoryId);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new EtResourceNotFoundException("Category does not exists");
        }
    }

    public String create(Category category) throws EtBadRequestException {

        return categoryRepository.save(category).getCategoryId();
    }

    public void updateCategory(Category category) throws EtBadRequestException{
        try{
            int rows=jdbcOperations.update(SQL_UPDATE_CATEGORY,category.getTitle(),category.getDescription(),
                    category.getUserId(),category.getCategoryId());
            if (rows==0){
                throw new EtBadRequestException("Specified category does not exist");
            }
        }
        catch (DataAccessException e){
            throw new EtBadRequestException("Specified category does not exist");
        }
    }

    public void removeCategory(String userId,String categoryId){
        transactionRepo.removeTransactionsForCategory(userId, categoryId);
        jdbcOperations.update(SQL_DELETE,userId,categoryId);
    }

    private RowMapper<Category> categoryRowMapper=new RowMapper<Category>() {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Category(rs.getString("CATEGORY_ID"),
                    rs.getString("USER_ID"),
                    rs.getString("TITLE"),
                    rs.getString("DESCRIPTION"),
                    rs.getDouble("TOTAL_EXPENSE"));
        }
    };
}
