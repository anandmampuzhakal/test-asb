package nz.co.test.transactions.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import dagger.hilt.android.AndroidEntryPoint
import nz.co.test.transactions.viewModel.TransactionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import nz.co.test.transactions.NavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val viewModel: TransactionViewModel = viewModel()
                    NavGraph(viewModel)
                }
            }
        }
    }
}
