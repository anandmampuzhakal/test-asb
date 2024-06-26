package nz.co.test.transactions.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TransactionUtil {
    private const val GST_PER = "0.15"
    fun getParsedDate(transactionDate: String): OffsetDateTime? {
        return try {
            val localDateTime =
                LocalDateTime.parse(transactionDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
        } catch (e: Exception) {
            null  // Handle exception possibly by logging
        }
    }

    fun getAmountText(debit: BigDecimal, credit: BigDecimal): String {
        val isDebit = debit > BigDecimal.ZERO
        return if (isDebit) "-${debit.toPlainString()}" else "+${credit.toPlainString()}"
    }

    fun calculateGST(amount: BigDecimal): String {
        val gst = amount * BigDecimal(GST_PER)
        return gst.setScale(2, RoundingMode.HALF_UP).toPlainString()
    }
}
