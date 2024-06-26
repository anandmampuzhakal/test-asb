package nz.co.test.transactions.repo

import javax.inject.Inject
import nz.co.test.transactions.services.TransactionsService
import nz.co.test.transactions.services.mapToBo

class TransactionRepositoryImpl @Inject constructor(
    private val transactionService: TransactionsService
) : TransactionRepository {
    override suspend fun getTransactions() =
        transactionService.retrieveTransactions().sortedByDescending { it.transactionDate }.map {
            it.mapToBo()
        }
}
