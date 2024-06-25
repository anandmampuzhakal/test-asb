package nz.co.test.transactions.viewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nz.co.test.transactions.repo.TransactionRepository
import nz.co.test.transactions.services.Transaction
import nz.co.test.transactions.ui.model.TransactionDisplayData
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<TransactionDisplayData>>(emptyList())
    val transactions = _transactions.asStateFlow()

    init {
        loadTransactions()
    }

    fun loadTransactions() = viewModelScope.launch {
        val rawTransactions = repository.getTransactions().sortedByDescending { it.transactionDate }
        _transactions.value = rawTransactions.map { it.prepareDisplayData() }
    }


    private fun Transaction.prepareDisplayData(): TransactionDisplayData {
        // Determine if the transaction is primarily a credit or a debit.
        val isDebit = this.debit > BigDecimal.ZERO
        val amountColor = if (isDebit) Color.Red else Color.Green
        // Ensure the text reflects the dominant transaction type.
        val amountText = if (isDebit) "-${this.debit}" else "+${this.credit}"
        val gst = (if (isDebit) this.debit else this.credit) * BigDecimal("0.15")
        val parsedDate = this.getParsedDate()?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "Invalid date"

        return TransactionDisplayData(
            id = this.id,
            summary = this.summary,
            amountText = amountText,
            gst = gst.setScale(2, RoundingMode.HALF_UP).toPlainString(),
            date = parsedDate,
            amountColor = amountColor
        )
    }

}