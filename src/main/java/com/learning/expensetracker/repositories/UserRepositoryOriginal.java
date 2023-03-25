package com.learning.expensetracker.repositories;

import com.learning.expensetracker.exceptions.EtAuthException;
import com.learning.expensetracker.models.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public class UserRepositoryOriginal {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcOperations jdbcOperations;

    private static final String SQL_COUNT_BY_EMAIL= "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL=?";

    private static final String SQL_FIND_BY_EMAIL="SELECT * FROM ET_USERS WHERE EMAIL=?";

    public User create(User user) {
        try{
            user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(5)));
            return userRepository.save(user);
        }
        catch (IllegalArgumentException e){
            System.err.println("Entered user details is null");
            e.printStackTrace();
        }
        catch (Exception e){
            System.err.println("Exception caught while inserting user");
            e.printStackTrace();
        }
        return null;
    }

    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        User user;
        try{


            user=jdbcOperations.queryForObject(SQL_FIND_BY_EMAIL,userRowMapper,email);
            if(user != null) {
                if (BCrypt.checkpw(password, user.getPassword())) {
                    return user;
                }else {
                    throw new EtAuthException("Wrong password");
                }
            }
            else{
                throw new EtAuthException("No such user exists");
            }
        }
        catch (DataAccessException e){
            throw new EtAuthException("Invalid email id");
        }
        catch (Exception e){
            throw new EtAuthException("Error while fetching data");
        }


    }


    public Integer getCountByEmail(String email) {
        return jdbcOperations.queryForObject(SQL_COUNT_BY_EMAIL,Integer.class,email);
    }

    public User findById(String userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        User user=new User();
        user.setUserId(rs.getString("USER_ID"));
        user.setFirstName(rs.getString("FIRST_NAME"));
        user.setLastName(rs.getString("LAST_NAME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setPassword(rs.getString("PASSWORD"));
        return user;
    });
}
