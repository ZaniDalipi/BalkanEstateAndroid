package com.zanoapps.search.presentation.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme
import com.zanoapps.search.presentation.R

@Composable
fun SaveSearchDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, enableNotifications: Boolean) -> Unit,
    isSaving: Boolean = false
) {
    var searchName by remember { mutableStateOf("") }
    var enableNotifications by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_search),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = searchName,
                    onValueChange = { searchName = it },
                    label = { Text(stringResource(R.string.save_search_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = enableNotifications,
                        onCheckedChange = { enableNotifications = it }
                    )
                    Text(
                        text = stringResource(R.string.save_search_notifications),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.save_search_cancel))
                    }
                    
                    Button(
                        onClick = { onSave(searchName, enableNotifications) },
                        modifier = Modifier.weight(1f),
                        enabled = searchName.isNotBlank() && !isSaving,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.save_search_save))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveSearchDialogPreview() {
    BalkanEstateTheme {
        SaveSearchDialog(
            onDismiss = {},
            onSave = { _, _ -> },
            isSaving = false
        )
    }
}
