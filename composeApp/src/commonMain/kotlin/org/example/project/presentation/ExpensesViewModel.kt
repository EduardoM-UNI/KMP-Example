package org.example.project.presentation

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.example.project.domain.ExpenseRepository
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory


sealed class ExpensesUiState {
    object Loading : ExpensesUiState()
    data class Success(val expenses: List<Expense>, val total: Double) : ExpensesUiState()
    data class Error(val message: String) : ExpensesUiState()
}

class ExpensesViewModel(private val repo: ExpenseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesUiState>(ExpensesUiState.Loading)
    val uiState = _uiState.asStateFlow()


    init {
        getExpensesList()
    }

    private fun getExpensesList(){
        viewModelScope.launch {
            try {
                delay(2000)
                val expenses = repo.getAllExpenses()
                _uiState.value = ExpensesUiState.Success(expenses, expenses.sumOf { it.amount })
            } catch (e: Exception) {
                _uiState.value = ExpensesUiState.Error(e.message ?: "Ha ocurrido un error")
            }
        }
    }

    private suspend fun updatExpensesList() {
        try {
            val expenses = repo.getAllExpenses()
            _uiState.value = ExpensesUiState.Success(expenses, expenses.sumOf { it.amount })
        } catch (e: Exception) {
            _uiState.value = ExpensesUiState.Error(e.message ?: "Ha ocurrido un error")
        }
    }


    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repo.addExpense(expense)
                updatExpensesList()
            }catch (e: Exception){
                _uiState.value = ExpensesUiState.Error(e.message ?: "Ha ocurrido un error")
            }
        }
    }

    fun editExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repo.editExpense(expense)
                updatExpensesList()
            }catch (e: Exception){
                _uiState.value = ExpensesUiState.Error(e.message ?: "Ha ocurrido un error")
            }
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteExpense(id)
                updatExpensesList()
            }catch (e: Exception){
                _uiState.value = ExpensesUiState.Error(e.message ?: "Ha ocurrido un error")
            }
        }
    }

    fun getExpenseWithID(id: Long): Expense? {
        return (_uiState.value as? ExpensesUiState.Success)?.expenses?.find { it.id == id }
    }

    fun getCategories():List<ExpenseCategory>{
        return repo.getCategories()
    }
}