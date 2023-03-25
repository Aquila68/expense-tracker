package com.learning.expensetracker.models;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Component
@Entity
@Table(name="et_transactions")
public class Transaction {
    @Id
    private Integer transactionId;
    private String categoryId;
    private String userId;
    private Double amount;
    private String note;
    private long transactionDate;

    public Transaction(Integer transactionId, String categoryId, String userId, double amount, String note, long transactionDate) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.userId = userId;
        this.amount = amount;
        this.note = note;
        this.transactionDate = transactionDate;
    }

    public Transaction(){}

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", categoryId='" + categoryId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", note='" + note + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
