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

//Todo We can move this logics in another units class later.
fun Transaction.getParsedDate(): OffsetDateTime? {
    return try {
        // First parse as LocalDateTime since there's no offset in your string
        val localDateTime = LocalDateTime.parse(transactionDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        // Convert LocalDateTime to OffsetDateTime, assuming UTC (+00:00) as there's no offset specified
        OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
    } catch (e: Exception) {
        null  // Handle exception possibly by logging
    }
}
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
