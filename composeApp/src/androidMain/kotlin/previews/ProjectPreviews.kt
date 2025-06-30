package previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.App
import org.example.project.data.ExpenseManager
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory
import org.example.project.presentation.ExpensesUiState
import org.example.project.ui.AllExpensesHeader
import org.example.project.ui.ExpenseItem
import org.example.project.ui.ExpenseeTotalHeader
import org.example.project.ui.ExpensesScreen

@Preview(showBackground = true)
@Composable
fun ExpensesScreenPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ExpenseeTotalHeader(total = 1000.0)
    }
}

@Preview(showBackground = true)
@Composable
fun AllExpensesHeaderPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        AllExpensesHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun ExpeseItemPreview() {
    Box(modifier = Modifier.padding(8.dp)) {
        ExpenseItem(
            expense = ExpenseManager.fakeExpensesList[0],
            onExpenseClick = {}

        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpeseScreenPreview() {
    ExpensesScreen(
        uiState = ExpensesUiState.Success(
            expenses = ExpenseManager.fakeExpensesList,
            total = 1000.0
        ), onExpenseClick = {}, onExpenseDelete = {})
}