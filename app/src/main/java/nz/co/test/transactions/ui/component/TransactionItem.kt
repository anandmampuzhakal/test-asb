package nz.co.test.transactions.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nz.co.test.transactions.R
import nz.co.test.transactions.ui.model.TransactionDisplayData

@Composable
fun TransactionItem(transaction: TransactionDisplayData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 4.dp, vertical = 2.dp
            ) // Reduced and optimized padding
            .fillMaxWidth()
            .clickable(onClick = onClick), elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Text(text = transaction.summary, style = MaterialTheme.typography.h6)
            Text(
                text = stringResource(
                    R.string.amount_gst_format, transaction.amountText, transaction.gst
                ), color = transaction.amountColor
            )
            Text(
                text = stringResource(R.string.label_date_format, transaction.date)
            )
        }
    }
}

// Preview of TransactionItem
@Preview(showBackground = true)
@Composable
fun PreviewTransactionItem() {
    // Sample data for the preview
    val sampleTransaction = TransactionDisplayData(
        id = 1,
        summary = "Coffee Purchase",
        amountText = "-$4.50",
        gst = "0.68",
        date = "2023-06-26",
        amountColor = Color.Red
    )

    // Theme wrapper to apply correct Material theme
    MaterialTheme {
        TransactionItem(transaction = sampleTransaction, onClick = {})
    }
}
