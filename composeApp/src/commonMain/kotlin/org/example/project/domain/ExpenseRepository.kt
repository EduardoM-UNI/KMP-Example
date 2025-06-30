package org.example.project.domain

import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

interface ExpenseRepository {

    suspend fun getAllExpenses(): List<Expense>
    suspend fun addExpense(expense: Expense)
    suspend fun editExpense(expense: Expense)
    fun getCategories(): List<ExpenseCategory>
    suspend fun deleteExpense(expense: Expense): List<Expense>


}