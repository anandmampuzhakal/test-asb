package nz.co.test.transactions.util

import org.junit.Assert.*

import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TransactionUtilTest {

    @Test
    fun testGetParsedDate_ValidDate() {
        val transactionDate = "2023-06-26T12:00:00"
        val expectedDate =
            LocalDateTime.parse(transactionDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atOffset(ZoneOffset.UTC)
        val actualDate = TransactionUtil.getParsedDate(transactionDate)
        assertEquals(expectedDate, actualDate)
    }

    @Test
    fun testGetParsedDate_InvalidDate() {
        val transactionDate = "invalid-date"
        val actualDate = TransactionUtil.getParsedDate(transactionDate)
        assertNull(actualDate)
    }

    @Test
    fun testGetAmountText_Debit() {
        val debit = BigDecimal("150.00")
        val credit = BigDecimal.ZERO
        assertEquals("-150.00", TransactionUtil.getAmountText(debit, credit))
    }

    @Test
    fun testGetAmountText_Credit() {
        val debit = BigDecimal.ZERO
        val credit = BigDecimal("200.00")
        assertEquals("+200.00", TransactionUtil.getAmountText(debit, credit))
    }

    @Test
    fun testCalculateGST() {
        val amount = BigDecimal("100.00")
        val expectedGST = "15.00"  // 15% of 100.00
        assertEquals(expectedGST, TransactionUtil.calculateGST(amount))
    }
}