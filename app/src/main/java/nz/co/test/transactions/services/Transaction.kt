package nz.co.test.transactions.services

import androidx.compose.ui.graphics.Color
import nz.co.test.transactions.ui.model.TransactionDisplayData
import nz.co.test.transactions.util.TransactionUtil
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class Transaction(
    val id: Int,
    val transactionDate: String,
    val summary: String,
    val debit: BigDecimal,
    val credit: BigDecimal
)

fun Transaction.prepareDisplayData(): TransactionDisplayData {
    val isDebit = this.debit > BigDecimal.ZERO
    val amountColor = if (isDebit) Color.Red else Color.Green
    val amountText = TransactionUtil.getAmountText(this.debit, this.credit)
    val gst = TransactionUtil.calculateGST(if (isDebit) this.debit else this.credit)
    val parsedDate = TransactionUtil.getParsedDate(this.transactionDate)?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "Invalid date"

    return TransactionDisplayData(
        id = this.id,
        summary = this.summary,
        amountText = amountText,
        gst = gst,
        date = parsedDate,
        amountColor = amountColor
    )
}
