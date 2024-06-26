package nz.co.test.transactions.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nz.co.test.transactions.R
import nz.co.test.transactions.ui.model.TransactionDisplayData
import nz.co.test.transactions.ui.component.DetailItem

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
        DetailItem(label = stringResource(R.string.transaction_id), value = transactionDisplayData.id.toString())
        DetailItem(label = stringResource(R.string.date), value = transactionDisplayData.date)
        DetailItem(label = stringResource(R.string.summary), value = transactionDisplayData.summary)
        DetailItem(
            label = stringResource(R.string.amount),
            value = transactionDisplayData.amountText,
            valueColor = transactionDisplayData.amountColor
        )
        DetailItem(
            label = stringResource(R.string.gst),
            value = "$${transactionDisplayData.gst}"
        )
    }
}

@Composable
private fun EmptyTransactionView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.no_transaction_details_available), style = MaterialTheme.typography.h6, color = Color.Gray)
    }
}
