package nz.co.test.transactions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nz.co.test.transactions.ui.TransactionListScreen
import nz.co.test.transactions.viewModel.TransactionViewModel

@Composable
fun NavGraph(viewModel: TransactionViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "transactionList") {
        composable("transactionList") {
            TransactionListScreen(viewModel, navController)
        }
        composable("transactionDetail/{transactionId}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")?.toIntOrNull()
            transactionId?.let { id ->
                // Use collectAsState() to observe changes and allow recomposition
                val transactions = viewModel.transactions.collectAsState().value
                val transaction = transactions.find { it.id == id }
                transaction?.let {

                }
            }
        }
    }
}
