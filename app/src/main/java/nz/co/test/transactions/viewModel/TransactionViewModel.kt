package nz.co.test.transactions.viewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nz.co.test.transactions.repo.TransactionRepository
import nz.co.test.transactions.services.prepareDisplayData
import nz.co.test.transactions.ui.model.TransactionDisplayData
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
}