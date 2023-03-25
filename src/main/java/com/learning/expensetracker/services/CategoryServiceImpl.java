package com.learning.expensetracker.services;

import com.learning.expensetracker.models.Category;
import com.learning.expensetracker.repositories.CategoryRepositoryOriginal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepositoryOriginal categoryRepo;

    @Override
    public List<Category> fetchAllCategories(String userId) {
        return categoryRepo.findAllCategories(userId);
    }

    @Override
    public Category fetchCategoryById(String categoryId,String userId) {
        return categoryRepo.findByCategoryId(categoryId,userId);
    }

    @Override
    public String addCategory(Category category) {
        return categoryRepo.create(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepo.updateCategory(category);
    }

    @Override
    public void deleteCategory(String categoryId, String userId) {
        categoryRepo.removeCategory(userId,categoryId);
    }


}
