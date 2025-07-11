import org.example.project.data.ExpenseManager
import org.example.project.data.ExpensesRepoImpl
import org.example.project.model.Expense
import org.example.project.model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ExpenseRepoTest {

    private val expenseManager = ExpenseManager
    private val repo = ExpensesRepoImpl(expenseManager)

    @Test
    fun expense_list_is_no_empty() {
        // Given
        val expenseList = mutableListOf<Expense>()
        // When
        expenseList.addAll(repo.getAllExpenses())
        // Then
        assertTrue { expenseList.isNotEmpty() }
    }

    @Test
    fun add_new_expense() {
        // GIVEN
        val expenseList = repo.getAllExpenses()

        // WHEN
        repo.addExpense(
            Expense(
                amount = 4.5,
                category = ExpenseCategory.CAR,
                description = "Combustible"
            )
        )

        // THEN
        assertContains(expenseList, expenseList.find { it.id == 7L })
    }

    @Test
    fun edit_expense() {
        // GIVEN
        val expenseListBefore = repo.getAllExpenses()

        // WHEN
        val newExpenseId = 7L
        repo.addExpense(
            Expense(
                amount = 4.5,
                category = ExpenseCategory.CAR,
                description = "Combustible"
            )
        )

        assertNotNull(expenseListBefore.find { it.id == newExpenseId })

        val updatedExpense = Expense(
            id = newExpenseId,
            amount = 8.0,
            category = ExpenseCategory.OTHER,
            description = "Ropa"
        )
        repo.editExpense(updatedExpense)

        // THEN
        val expenseListAfter = repo.getAllExpenses()
        assertEquals(updatedExpense, expenseListAfter.find { it.id == newExpenseId })
    }

    @Test
    fun get_all_categories() {
        // GIVEN
        val categoryList = mutableListOf<ExpenseCategory>()

        // WHEN
        categoryList.addAll(repo.getCategories())

        // THEN
        assertTrue(categoryList.isNotEmpty())

    }

    @Test
    fun check_all_categories() {
        // GIVEN
        val allCategoties = listOf(
            ExpenseCategory.GROCERIES,
            ExpenseCategory.PARTY,
            ExpenseCategory.SNACKS,
            ExpenseCategory.COFFEE,
            ExpenseCategory.CAR,
            ExpenseCategory.HOUSE,
            ExpenseCategory.OTHER,
        )

        val repoCategories = repo.getCategories()

        // THEN
        assertEquals(allCategoties, repoCategories)
    }
}