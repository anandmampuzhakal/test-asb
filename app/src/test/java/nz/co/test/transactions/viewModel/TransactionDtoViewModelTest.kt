package nz.co.test.transactions.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import nz.co.test.transactions.repo.TransactionRepository
import nz.co.test.transactions.repo.bo.TransactionBo
import nz.co.test.transactions.repo.bo.prepareDisplayData
import nz.co.test.transactions.ui.model.TransactionDisplayData
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.Assert.*
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
@ExperimentalCoroutinesApi
class TransactionViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: TransactionViewModel
    private val repository: TransactionRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)  // Set main dispatcher to test dispatcher
        viewModel = TransactionViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()  // Reset to the original main dispatcher after tests
    }

    @Test
    fun `loadTransactions loads and updates transactions correctly`() = testScope.runTest {
        // Prepare your data for `TransactionBo`
        val transactionBos = listOf(
            TransactionBo(
                id = 1,
                summary = "Deposit",
                debit = BigDecimal("100"),
                credit = BigDecimal("0"),
                gst = "15",
                date = "2021-06-01",
                isDebit = true
            ),
            TransactionBo(
                id = 2,
                summary = "Withdrawal",
                debit = BigDecimal("0"),
                credit = BigDecimal("50"),
                gst = "7.5",
                date = "2021-06-02",
                isDebit = false
            )
        )

        // Mock repository to return these `TransactionBo` instances
        coEvery { repository.getTransactions() } returns transactionBos

        // Load transactions in the view model
        viewModel.loadTransactions()

        // Create expected `TransactionDisplayData` based on the `TransactionBo` data above
        val expectedTransactions = transactionBos.map { it.prepareDisplayData() }

        // Verify that the ViewModel's transactions are updated correctly
        assertEquals(expectedTransactions, viewModel.transactions.value)
    }

    @Test
    fun `loadTransactions handles empty data correctly`() = testScope.runTest {
        // Set up the repository to return an empty list
        coEvery { repository.getTransactions() } returns emptyList()

        // Load transactions in the view model
        viewModel.loadTransactions()

        // Verify that the ViewModel's transactions are an empty list
        assertTrue(viewModel.transactions.value.isEmpty())
    }

    // Assuming there's a `isLoading` StateFlow or similar in your ViewModel
    @Test
    fun `transaction data is transformed correctly`() = testScope.runTest {
        // Set up the TransactionBo with debit greater than zero to reflect a debit transaction
        val transactionBo = TransactionBo(
            id = 1,
            summary = "Test",
            debit = BigDecimal("100"),
            credit = BigDecimal.ZERO,
            gst = "15",
            date = "2022-01-01",
            isDebit = true
        )
        coEvery { repository.getTransactions() } returns listOf(transactionBo)

        viewModel.loadTransactions()

        // Adjusted expectation to reflect a debit transaction
        val expectedData = TransactionDisplayData(
            id = transactionBo.id,
            summary = transactionBo.summary,
            amountText = "-100", // Correct expectation for debit
            gst = transactionBo.gst,
            date = transactionBo.date,
            amountColor = Color.Red // Assuming red is for debits
        )

        assertEquals(listOf(expectedData), viewModel.transactions.value)
    }

    @Test
    fun `transactions are ordered correctly`() = testScope.runTest {
        // Setup transactions in unordered manner
        val transactions = listOf(
            TransactionBo(
                id = 2,
                summary = "Later Transaction",
                debit = BigDecimal("200"),
                credit = BigDecimal.ZERO,
                gst = "30",
                date = "2022-02-01",
                isDebit = true
            ),
            TransactionBo(
                id = 1,
                summary = "Earlier Transaction",
                debit = BigDecimal("100"),
                credit = BigDecimal.ZERO,
                gst = "15",
                date = "2022-01-01",
                isDebit = true
            )
        )
        coEvery { repository.getTransactions() } returns transactions

        // Trigger ViewModel to load transactions
        viewModel.loadTransactions()

        // Expected ordering by date or ID
        val expected = transactions.sortedByDescending { it.date }.map { it.prepareDisplayData() }
        assertEquals(expected, viewModel.transactions.value)
    }

    @Test
    fun `data remains consistent after transformation`() = testScope.runTest {
        val transactionBo = TransactionBo(
            id = 3,
            summary = "Consistency Check",
            debit = BigDecimal("300"),
            credit = BigDecimal.ZERO,
            gst = "45",
            date = "2022-03-01",
            isDebit = true
        )
        coEvery { repository.getTransactions() } returns listOf(transactionBo)

        viewModel.loadTransactions()

        val expectedData = transactionBo.prepareDisplayData()
        assertEquals(listOf(expectedData), viewModel.transactions.value)
    }

    @Test
    fun `mixed transaction types are handled correctly`() = testScope.runTest {
        val transactions = listOf(
            TransactionBo(
                id = 4,
                summary = "Credit Transaction",
                debit = BigDecimal.ZERO,
                credit = BigDecimal("400"),
                gst = "60",
                date = "2022-04-01",
                isDebit = false
            ),
            TransactionBo(
                id = 5,
                summary = "Debit Transaction",
                debit = BigDecimal("500"),
                credit = BigDecimal.ZERO,
                gst = "75",
                date = "2022-05-01",
                isDebit = true
            )
        )
        coEvery { repository.getTransactions() } returns transactions

        viewModel.loadTransactions()

        val expectedData = transactions.map { it.prepareDisplayData() }
        assertEquals(expectedData, viewModel.transactions.value)
    }

    @Test
    fun `handles extreme values correctly`() = testScope.runTest {
        val extremeTransaction = TransactionBo(
            id = 8,
            summary = "Extreme Transaction",
            debit = BigDecimal("99999999"),
            credit = BigDecimal.ZERO,
            gst = "15000000",
            date = "2022-08-01",
            isDebit = true
        )
        coEvery { repository.getTransactions() } returns listOf(extremeTransaction)

        viewModel.loadTransactions()

        val expectedData = extremeTransaction.prepareDisplayData()
        assertEquals(listOf(expectedData), viewModel.transactions.value)
    }
}
