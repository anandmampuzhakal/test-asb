package nz.co.test.transactions.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.Dispatchers
import nz.co.test.transactions.repo.TransactionRepository
import nz.co.test.transactions.services.Transaction
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.After
import org.junit.Assert.assertEquals
import io.mockk.coEvery
import io.mockk.mockk
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class TransactionViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TransactionViewModel
    private val repository: TransactionRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = TransactionViewModel(repository)
        coEvery { repository.getTransactions() } returns listOf(
            Transaction(id = 1, transactionDate = "2022-02-27T19:30:17", credit = BigDecimal("5846.72"), debit = BigDecimal("0.00"), summary = "Deposit"),
            Transaction(id = 2, transactionDate = "2022-02-27T18:25:18", credit = BigDecimal("0"), debit = BigDecimal("5401.72"), summary = "Withdrawal")
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ensure transactions are loaded correctly`() = runTest {
        viewModel.loadTransactions()
        advanceUntilIdle()
        val transactions = viewModel.transactions.value
        assertEquals(2, transactions.size)
        assertEquals(1, transactions[0].id)
        assertEquals("2022-02-27", transactions[0].date)
        assertEquals("+5846.72", transactions[0].amountText)
        assertEquals(2, transactions[1].id)
        assertEquals("-5401.72", transactions[1].amountText)
    }

    @Test
    fun `verify no transactions are loaded when repository is empty`() = runTest {
        coEvery { repository.getTransactions() } returns emptyList()
        viewModel.loadTransactions()
        val transactions = viewModel.transactions.value
        assertEquals(0, transactions.size)
    }

    @Test
    fun `test transaction display data formatting`() = runTest {
        coEvery { repository.getTransactions() } returns listOf(
            Transaction(id = 3, transactionDate = "2024-06-26T15:47:10", credit = BigDecimal("1000000.00"), debit = BigDecimal("500000.00"), summary = "Large Deposit"),
            Transaction(id = 4, transactionDate = "2024-06-27T15:47:10", credit = BigDecimal("0.01"), debit = BigDecimal("0.01"), summary = "Minimal Transaction")
        )
        viewModel.loadTransactions()
        val transactions = viewModel.transactions.value
        assertEquals(2, transactions.size)
        assertEquals("75000.00", transactions[1].gst)
        assertEquals("0.00", transactions[0].gst)
    }

    @Test
    fun `test GST calculation for debit transaction`() = runTest {
        val transactionList = listOf(
            Transaction(id = 1, transactionDate = "2022-02-27", summary = "Withdrawal", debit = BigDecimal("1000"), credit = BigDecimal.ZERO)
        )
        coEvery { repository.getTransactions() } returns transactionList

        viewModel.loadTransactions()

        val transactions = viewModel.transactions.value
        assertEquals("150.00", transactions.first().gst)
    }

    @Test
    fun `test GST calculation for credit transaction`() = runTest {
        val transactionList = listOf(
            Transaction(id = 2, transactionDate = "2022-02-28", summary = "Deposit", debit = BigDecimal.ZERO, credit = BigDecimal("2000"))
        )
        coEvery { repository.getTransactions() } returns transactionList

        viewModel.loadTransactions()

        val transactions = viewModel.transactions.value
        assertEquals("300.00", transactions.first().gst)
    }

    @Test
    fun `test valid date formatting`() = runTest {
        val transactionList = listOf(
            Transaction(id = 1, transactionDate = "2022-02-27T19:30:17", summary = "Valid Date", debit = BigDecimal("500"), credit = BigDecimal.ZERO)
        )
        coEvery { repository.getTransactions() } returns transactionList

        viewModel.loadTransactions()

        val transactions = viewModel.transactions.value
        assertEquals("2022-02-27", transactions.first().date)
    }

    @Test
    fun `test invalid date formatting`() = runTest {
        val transactionList = listOf(
            Transaction(id = 2, transactionDate = "invalid-date-format", summary = "Invalid Date", debit = BigDecimal.ZERO, credit = BigDecimal("1000"))
        )
        coEvery { repository.getTransactions() } returns transactionList

        viewModel.loadTransactions()

        val transactions = viewModel.transactions.value
        assertEquals("Invalid date", transactions.first().date)
    }
    @Test
    fun `test leap year date handling`() = runTest {
        val transactionList = listOf(
            Transaction(id = 3, transactionDate = "2020-02-29T00:00:00", summary = "Leap Year", debit = BigDecimal.ZERO, credit = BigDecimal("200"))
        )
        coEvery { repository.getTransactions() } returns transactionList

        viewModel.loadTransactions()

        val transactions = viewModel.transactions.value
        assertEquals("2020-02-29", transactions.first().date)
    }

    @Test
    fun `test concurrent loadTransactions calls handle data correctly`() = runTest {
        val largeTransactionList = List(1000) { index ->
            Transaction(id = index, transactionDate = "2024-06-26T15:47:10", credit = BigDecimal("100"), debit = BigDecimal("50"), summary = "Transaction $index")
        }
        coEvery { repository.getTransactions() } returns largeTransactionList

        repeat(10) {
            launch {
                viewModel.loadTransactions()
            }
        }
        advanceUntilIdle()

        val transactions = viewModel.transactions.value
        assertEquals(1000, transactions.size)
        assertEquals("Transaction 999", transactions.last().summary)
    }
}
