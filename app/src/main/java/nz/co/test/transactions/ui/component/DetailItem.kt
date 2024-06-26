package nz.co.test.transactions.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DetailItem(label: String, value: String, valueColor: Color = MaterialTheme.colors.onSurface) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.width(8.dp))  // Adding a small space for visual separation
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = valueColor
        )
    }
}

@Preview(showBackground = true, name = "Detail Item Preview")
@Composable
fun PreviewDetailItem() {
    MaterialTheme {
        DetailItem(label = "Example Label:", value = "Example Value", valueColor = MaterialTheme.colors.error)
    }
}