package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.data.TitleTopBarTypes
import org.example.project.getColorTheme
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailsScreen(
    expenseToEdit: Expense? = null,
    categoryList: List<ExpenseCategory> = emptyList(),
    addExpenseAndNavigateBack: (expense: Expense) -> Unit
) {

    val colors = getColorTheme()
    var price by remember { mutableStateOf(expenseToEdit?.amount ?: 0.0) }
    var description by remember { mutableStateOf(expenseToEdit?.description ?: "") }
    var expenseCategory by remember { mutableStateOf(expenseToEdit?.category?.name ?: "") }
    var categorySelected by remember { mutableStateOf(expenseToEdit?.category?.name ?: "") }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    var showModal by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(imeVisible) {
        if (imeVisible && sheetState.isVisible) {
            sheetState.hide()
        }
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            content = {
                CategoryBottomSheetContent(categoryList) {
                    expenseCategory = it.name
                    categorySelected = it.name
                    scope.launch {
                        sheetState.hide()
                    }
                }
            },
            onDismissRequest = {
                keyboardController?.hide()
                showModal = false
            }
        )
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Spacer(modifier = Modifier.height(90.dp))
        ExpenseAmount(
            priceContent = price,
            onPriceChange = { price = it },
            keyboardController = keyboardController
        )
        Spacer(modifier = Modifier.height(30.dp))
        ExpenseTypeSelector(categorySelected = categorySelected, openBottomSheet = {

            scope.launch {
                showModal = true
                sheetState.show()
            }
        })
        Spacer(modifier = Modifier.height(30.dp))
        ExpenseDescription(
            descriptionContent = description,
            onDescriptionChange = {
                description = it
            },
            keyboardController = keyboardController
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.fillMaxWidth().padding(bottom = 25.dp).clip(RoundedCornerShape(45)),
            onClick = {
                val expense = Expense(
                    amount = price,
                    category = ExpenseCategory.valueOf(expenseCategory),
                    description = description
                )
                val expenseFromEdit = expenseToEdit?.id?.let { expense.copy(id = it) }
                addExpenseAndNavigateBack(expenseFromEdit ?: expense)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.purple,
                contentColor = Color.White
            ),
            enabled = price != 0.0 && description.isNotBlank() && expenseCategory.isNotBlank()
        ) {
            expenseToEdit?.let {
                Text(text = TitleTopBarTypes.EDIT_EXPENSES.values)
                return@Button
            }
            Text(text = TitleTopBarTypes.ADD_EXPENSES.values)
        }
    }
}

@Composable
private fun ExpenseAmount(
    priceContent: Double,
    onPriceChange: (price: Double) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    val colors = getColorTheme()
    var text by remember { mutableStateOf("$priceContent") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Amount",
            fontSize = 20.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                fontSize = 25.sp,
                color = colors.textColor,
                fontWeight = FontWeight.ExtraBold

            )
            TextField(
                modifier = Modifier.weight(1f),
                value = text,
                onValueChange = { newText ->
                    val numericText = newText.filter { it.isDigit() || it == '.' }
                    text = if (numericText.isNotEmpty() && numericText.count { it == '.' } <= 1) {
                        try {
                            val newValue = numericText.toDouble()
                            onPriceChange(newValue)
                            numericText
                        } catch (e: NumberFormatException) {
                            ""
                        }
                    } else {
                        onPriceChange(0.0)
                        ""
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.None
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colors.textColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                textStyle = TextStyle(fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)

            )
            Text(
                text = "USD",
                fontSize = 20.sp,
                color = Color.Gray,
                fontWeight = FontWeight.ExtraBold
            )

        }
        Divider(color = Color.Gray, thickness = 2.dp)
    }
}

@Composable
private fun ExpenseTypeSelector(
    categorySelected: String,
    openBottomSheet: () -> Unit
) {
    val colors = getColorTheme()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Expenses made for",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Text(
                text = categorySelected,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.textColor
            )
        }
        IconButton(
            modifier = Modifier.clip(RoundedCornerShape(35)).background(colors.colorArrowRound),
            onClick = {
                openBottomSheet.invoke()
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Button Expense Type",
                tint = colors.textColor
            )
        }
    }
}

@Composable
fun ExpenseDescription(
    descriptionContent: String,
    onDescriptionChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    var text by remember { mutableStateOf(descriptionContent) }
    val colors = getColorTheme()

    Column {
        Text(
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { newText ->
                if (newText.length <= 200) {
                    text = newText
                    onDescriptionChange(newText)
                }
            },
            value = text,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = colors.textColor,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Divider(color = Color.Black, thickness = 2.dp)
    }
}

@Composable
private fun CategoryBottomSheetContent(
    categories: List<ExpenseCategory>,
    onCategorySelected: (category: ExpenseCategory) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onCategorySelected = onCategorySelected)
        }
    }
}

@Composable
private fun CategoryItem(
    category: ExpenseCategory,
    onCategorySelected: (category: ExpenseCategory) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
            onCategorySelected(category)
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(40.dp).clip(CircleShape),
            imageVector = category.icon,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            text = category.name
        )
    }
}