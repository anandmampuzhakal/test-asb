package nz.co.test.transactions.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.co.test.transactions.ui.utils.TransactionItem
import nz.co.test.transactions.viewModel.TransactionViewModel

@Composable
fun TransactionListScreen(viewModel: TransactionViewModel, navController: NavController) {
    val transactions = viewModel.transactions.collectAsState().value
    LazyColumn(
        modifier = Modifier.fillMaxSize(), // Use fillMaxSize for better utilisation of available space
        contentPadding = PaddingValues(all = 8.dp) // Apply padding around all items
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction) {
                navController.navigate("transactionDetail/${transaction.id}")
            }
        }
    }
}


