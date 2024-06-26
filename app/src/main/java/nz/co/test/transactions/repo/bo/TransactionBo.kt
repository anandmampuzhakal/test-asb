package nz.co.test.transactions.repo.bo

import androidx.compose.ui.graphics.Color
import nz.co.test.transactions.services.mapToBo
import nz.co.test.transactions.ui.model.TransactionDisplayData
import nz.co.test.transactions.util.TransactionUtil
import java.math.BigDecimal

data class TransactionBo(
    val id: Int,
    val summary: String,
    val debit: BigDecimal,
    val credit: BigDecimal,
    val gst: String,
    val date: String,
    val isDebit: Boolean = false
)

fun TransactionBo.prepareDisplayData(): TransactionDisplayData {
    val amountColor = if (isDebit) Color.Red else Color.Green
    val amountText = TransactionUtil.getAmountText(this.debit, this.credit)
    return TransactionDisplayData(
        id = this.id,
        summary = this.summary,
        amountText = amountText,
        gst = gst,
        date = date,
        amountColor = amountColor
    )
}


