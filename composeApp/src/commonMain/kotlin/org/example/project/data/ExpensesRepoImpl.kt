package org.example.project.data

import com.expenseApp.db.AppDatabase
import org.example.project.domain.ExpenseRepository
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory

class ExpensesRepoImpl(
    private val expenseManager: ExpenseManager,
    private val appDatabase: AppDatabase
) : ExpenseRepository {

    private val queries = appDatabase.expensesDbQueries


    override fun getAllExpenses(): List<Expense> {
        return queries.selectAll().executeAsList().map {
            Expense(
                id = it.id,
                amount = it.amount,
                category = ExpenseCategory.valueOf(it.categoryName),
                description = it.description)
        }
        //return expenseManager.fakeExpensesList
    }

    override fun addExpense(expense: Expense) {
        queries.transaction {
            queries.insert(
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description
            )
        }
        //expenseManager.addExpense(expense)
    }

    override fun editExpense(expense: Expense) {
        queries.transaction {
            queries.update(
                id = expense.id,
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description
            )
        }
       // expenseManager.editExpense(expense)
    }

    override fun getCategories(): List<ExpenseCategory> {
        return queries.categories().executeAsList().map {
            ExpenseCategory.valueOf(it)
        }

        //return expenseManager.getCategories()
    }

    override fun deleteExpense(expense: Expense): List<Expense> {
        TODO("Not yet implemented")
    }
}