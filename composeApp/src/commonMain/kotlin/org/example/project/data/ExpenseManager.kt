package org.example.project.data

import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

object ExpenseManager {

    private var currentId = 1L

    val fakeExpensesList = mutableListOf(
        Expense(
            id = currentId++,
            amount = 70.0,
            category = ExpenseCategory.GROCERIES,
            description = "Weekly buy"
    ),
        Expense(
            id = currentId++,
            amount = 10.2,
            category = ExpenseCategory.SNACKS,
            description = "Hommies"
        ),
        Expense(
            id = currentId++,
            amount = 21000.2,
            category = ExpenseCategory.CAR,
            description = "Audi A1"
        ),
        Expense(
            id = currentId++,
            amount = 210.0,
            category = ExpenseCategory.PARTY,
            description = "Weekend party"
        ),
        Expense(
            id = currentId++,
            amount = 21.0,
            category = ExpenseCategory.HOUSE,
            description = "Cleaning"
        ),
        Expense(
            id = currentId++,
            amount = 121.0,
            category = ExpenseCategory.OTHER,
            description = "Services"
        )
    )

    fun addExpense(expense: Expense) {
        fakeExpensesList.add(expense.copy(id = currentId++))
    }
    fun editExpense(expense: Expense) {
        val index = fakeExpensesList.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            fakeExpensesList[index] = fakeExpensesList[index].copy(
                amount = expense.amount,
                category = expense.category,
                description = expense.description
            )

        }
    }
    // TODO: Delete expense
    fun deleteExpense(expense: Expense) {
        val index = fakeExpensesList.indexOfFirst { it.id == expense.id }
        fakeExpensesList.removeAt(index)
    }


    fun getCategories(): List<ExpenseCategory> {
        return listOf(
            ExpenseCategory.GROCERIES,
            ExpenseCategory.PARTY,
            ExpenseCategory.SNACKS,
            ExpenseCategory.COFFEE,
            ExpenseCategory.CAR,
            ExpenseCategory.HOUSE,
            ExpenseCategory.OTHER
        )

    }
}