package com.learning.expensetracker.repositories;

import com.learning.expensetracker.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category,String> {
}
