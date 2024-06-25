package nz.co.test.transactions.services

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
){
    fun getParsedDate(): OffsetDateTime? {
        return try {
            // First parse as LocalDateTime since there's no offset in your string
            val localDateTime = LocalDateTime.parse(transactionDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            // Convert LocalDateTime to OffsetDateTime, assuming UTC (+00:00) as there's no offset specified
            OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
        } catch (e: Exception) {
            null  // Handle exception possibly by logging
        }
    }
}