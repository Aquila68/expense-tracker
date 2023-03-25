package com.learning.expensetracker.services;

import com.learning.expensetracker.exceptions.EtAuthException;
import com.learning.expensetracker.models.User;

public interface UserService {

    User validateUser(String userName, String password) throws EtAuthException;

    User registerUser(User user) throws EtAuthException;
}
