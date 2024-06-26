package nz.co.test.transactions.services

import nz.co.test.transactions.repo.bo.TransactionBo
import nz.co.test.transactions.util.TransactionUtil
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

data class TransactionDto(
    val id: Int,
    val transactionDate: String,
    val summary: String,
    val debit: BigDecimal,
    val credit: BigDecimal
)

fun TransactionDto.mapToBo(): TransactionBo {
    val isDebit = this.debit > BigDecimal.ZERO
    val gst = TransactionUtil.calculateGST(if (isDebit) this.debit else this.credit)
    val parsedDate = TransactionUtil.getParsedDate(this.transactionDate)
        ?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "Invalid date"

    return TransactionBo(
        id = this.id,
        summary = this.summary,
        debit,
        credit,
        gst = gst,
        date = parsedDate,
        isDebit
    )
}

