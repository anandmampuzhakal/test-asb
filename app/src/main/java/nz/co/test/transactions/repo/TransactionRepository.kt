package nz.co.test.transactions.repo

import nz.co.test.transactions.repo.bo.TransactionBo

interface TransactionRepository {
    suspend fun getTransactions(): List<TransactionBo>
}
