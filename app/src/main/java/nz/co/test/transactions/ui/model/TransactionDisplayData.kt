package nz.co.test.transactions.ui.model

import androidx.compose.ui.graphics.Color

data class TransactionDisplayData(
    val id: Int,
    val summary: String,
    val amountText: String,
    val gst: String,
    val date: String,
    val amountColor: Color
)