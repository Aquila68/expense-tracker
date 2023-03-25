package com.learning.expensetracker.repositories;

import com.learning.expensetracker.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
