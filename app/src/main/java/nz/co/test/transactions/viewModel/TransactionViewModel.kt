package nz.co.test.transactions.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nz.co.test.transactions.repo.TransactionRepository
import nz.co.test.transactions.repo.bo.prepareDisplayData
import nz.co.test.transactions.ui.model.TransactionDisplayData
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<TransactionDisplayData>>(emptyList())
    val transactions: StateFlow<List<TransactionDisplayData>> = _transactions.asStateFlow()

    private val _transactionDetailState = MutableStateFlow<TransactionDetailState>(TransactionDetailState.Empty)
    val transactionDetailState: StateFlow<TransactionDetailState> = _transactionDetailState.asStateFlow()

    //Todo modify later
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _transactionDetailState.value = TransactionDetailState.Empty
        println("Error occurred: ${exception.localizedMessage}")
    }

    init {
        loadTransactions()
    }

    fun loadTransactions() = viewModelScope.launch(exceptionHandler) {
        val rawTransactions = repository.getTransactions().map { it.prepareDisplayData() }
        _transactions.value = rawTransactions
    }

    fun selectTransaction(transactionId: Int) {
        val transaction = _transactions.value.find { it.id == transactionId }
        _transactionDetailState.value = transaction?.let {
            TransactionDetailState.Detail(it)
        } ?: TransactionDetailState.Empty
    }

    sealed class TransactionDetailState {
        object Empty : TransactionDetailState()
        data class Detail(val transaction: TransactionDisplayData) : TransactionDetailState()
    }
}
