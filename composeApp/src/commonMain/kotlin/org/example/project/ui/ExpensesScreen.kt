package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.ExpenseManager
import org.example.project.getColorTheme
import org.example.project.model.Expense
import org.example.project.presentation.ExpensesUiState

@Composable
fun ExpensesScreen(uiState: ExpensesUiState, onExpenseClick: (expense: Expense) -> Unit) {

    val colors = getColorTheme()

    when(uiState){
        is ExpensesUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ExpensesUiState.Success -> {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                stickyHeader {
                    Column(
                        modifier = Modifier.background(colors.backgroundColor)
                    ) {
                        Spacer(modifier = Modifier.height(90.dp))
                        ExpenseeTotalHeader(uiState.total)
                        AllExpensesHeader()
                    }
                }
                items(uiState.expenses) {expense ->
                    ExpenseItem(expense, onExpenseClick = onExpenseClick)
                }
            }
        }
        is ExpensesUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${uiState.message}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }




}

@Composable
fun ExpenseeTotalHeader(total: Double) {
    Card(
        shape = RoundedCornerShape(30),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    )
    {
        Box(
            modifier = Modifier.fillMaxWidth().height(130.dp).padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Total: $total",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                modifier = Modifier.align(Alignment.CenterEnd), text = "USD", color = Color.Gray
            )
        }
    }
}

@Composable
fun AllExpensesHeader() {
    val colors = getColorTheme()

    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "All Expenses",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = colors.textColor

        )
        Button(
            shape = RoundedCornerShape(50),
            onClick = { //TODO
            },
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            enabled = false
        ) {
            Text(text = "View All")
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onExpenseClick: (expense: Expense) -> Unit) {
    val colors = getColorTheme()

    Card(
        shape = RoundedCornerShape(30),
        colors = CardDefaults.cardColors(containerColor = colors.colorExpenseItem),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp).clickable {
            onExpenseClick(expense)
        }
    ) {
        Row (
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Surface (
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(35),
                color = colors.purple
            ){
                Image(
                    imageVector = expense.icon,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Column(
                modifier = Modifier.padding(start = 8.dp).weight(1f)
            ) {
                Text(
                    text = expense.category.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = colors.textColor)

                Text(
                    text = expense.description,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${expense.amount}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colors.textColor)
        }
    }

}