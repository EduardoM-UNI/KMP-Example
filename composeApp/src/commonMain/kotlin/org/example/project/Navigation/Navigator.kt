package org.example.project.Navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.viewmodel.viewModel
import org.example.project.data.ExpenseManager
import org.example.project.data.ExpensesRepoImpl
import org.example.project.getColorTheme
import org.example.project.presentation.ExpensesViewModel
import org.example.project.ui.ExpenseDetailsScreen
import org.example.project.ui.ExpensesScreen
import org.koin.core.parameter.parametersOf

@Composable
fun Navigation(navigator: Navigator){

    val colors = getColorTheme()
    /*
    val viewModel = viewModel(modelClass = ExpensesViewModel::class) {
        ExpensesViewModel(ExpensesRepoImpl(ExpenseManager))
    }
     */
    val viewModel = koinViewModel(ExpensesViewModel::class){parametersOf()}

    NavHost(
        modifier = Modifier.background(colors.backgroundColor),
        navigator = navigator,
        initialRoute = "/home"
    ){
        scene("/home"){
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ExpensesScreen(uiState = uiState, onExpenseClick = {expense ->
                navigator.navigate("/addExpenses/${expense.id}")
            }, onExpenseDelete = {expenseToDelete ->
                viewModel.deleteExpense(expenseToDelete.id)
            })
        }
        scene("/addExpenses/{id}?"){
           val idFromPath = it.path<Long>("id")
            val expenseToEditOrAdd = idFromPath?.let { id -> viewModel.getExpenseWithID(id) }

            ExpenseDetailsScreen(
                expenseToEdit = expenseToEditOrAdd,
                categoryList = viewModel.getCategories(),
                addExpenseAndNavigateBack = { expense ->
                    if (expenseToEditOrAdd == null) {
                        viewModel.addExpense(expense)
                    } else {
                        viewModel.editExpense(expense)
                    }
                    navigator.popBackStack()
                })
        }
    }
}
