package com.learning.expensetracker.services;

import com.learning.expensetracker.exceptions.EtAuthException;
import com.learning.expensetracker.models.User;
import com.learning.expensetracker.repositories.UserRepositoryOriginal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepositoryOriginal userRepositoryImpl;


    @Override
    public User validateUser(String email,String password) throws EtAuthException {
        if(email!=null){
            email=email.toLowerCase();
        }
        return userRepositoryImpl.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(User user) throws EtAuthException {
        String email=null;
        Pattern pattern=Pattern.compile("^(.+)@(.+).(.+)$");
        if(user.getEmail()!=null){
            email=user.getEmail().toLowerCase();
        }
        if(!pattern.matcher(email).matches()){
            throw new EtAuthException("Invalid email id");
        }

        Integer count=userRepositoryImpl.getCountByEmail(email);
        if(count>0){
            throw new EtAuthException("Email id already exists");
        }
        String userId=userRepositoryImpl.create(user).getUserId();

        return userRepositoryImpl.findById(userId);
    }

}
