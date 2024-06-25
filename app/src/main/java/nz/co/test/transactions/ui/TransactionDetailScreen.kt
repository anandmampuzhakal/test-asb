package nz.co.test.transactions.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import nz.co.test.transactions.ui.model.TransactionDisplayData
import nz.co.test.transactions.ui.utils.DetailItem

@Composable
fun TransactionDetailScreen(transactionDisplayData: TransactionDisplayData) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Details") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colors.background
        ) {
            //Todo need to modify the logic
            if (transactionDisplayData != null) {
                DetailItemsColumn(transactionDisplayData)
            } else {
                EmptyTransactionView()
            }
        }
    }
}

@Composable
private fun DetailItemsColumn(transactionDisplayData: TransactionDisplayData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DetailItem(label = "Transaction ID:", value = transactionDisplayData.id.toString())
        DetailItem(label = "Date:", value = transactionDisplayData.date)
        DetailItem(label = "Summary:", value = transactionDisplayData.summary)
        DetailItem(
            label = "Amount:",
            value = transactionDisplayData.amountText,
            valueColor = transactionDisplayData.amountColor
        )
        DetailItem(
            label = "GST:",
            value = "$${transactionDisplayData.gst}"
        )
    }
}

@Composable
private fun EmptyTransactionView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No transaction details available", style = MaterialTheme.typography.h6, color = Color.Gray)
    }
}
