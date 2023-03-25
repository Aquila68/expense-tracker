package com.learning.expensetracker.services;

import com.learning.expensetracker.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> fetchAllCategories(String userId);

    Category fetchCategoryById(String categoryId,String userId);

    String addCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(String categoryId,String userId);
}
