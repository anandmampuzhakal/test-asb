package nz.co.test.transactions.repo

import nz.co.test.transactions.services.TransactionsService
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transactionService: TransactionsService) {
    suspend fun getTransactions() = transactionService.retrieveTransactions()
}
