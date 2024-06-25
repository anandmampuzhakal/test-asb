package nz.co.test.transactions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.co.test.transactions.ui.model.TransactionDisplayData
import nz.co.test.transactions.viewModel.TransactionViewModel

@Composable
fun TransactionListScreen(viewModel: TransactionViewModel, navController: NavController) {
    val transactions = viewModel.transactions.collectAsState().value
    LazyColumn(
        modifier = Modifier.background(Color.White) // Ensure LazyColumn has a white background
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction) {
                navController.navigate("transactionDetail/${transaction.id}")
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionDisplayData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium // This applies rounded corners
    ) {
        Column(modifier = Modifier
            .background(Color.White) // Ensuring the background is white
            .padding(16.dp)) {
            Text(text = transaction.summary, style = MaterialTheme.typography.h6)
            Text(text = "Amount: ${transaction.amountText} (GST: $${transaction.gst})", color = transaction.amountColor)
            Text(text = "Date: ${transaction.date}")
        }
    }
}