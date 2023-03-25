package com.learning.expensetracker.models;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Component
@Entity
@Table(name="et_categories")
public class Category {

    @Id
    private String categoryId;
    private String userId;
    private String title;
    private String description;
    @Transient
    private Double totalExpense;

    public Category(String categoryId, String userId, String title, String description, Double totalExpense) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.totalExpense = totalExpense;
    }

    public Category(){}

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
