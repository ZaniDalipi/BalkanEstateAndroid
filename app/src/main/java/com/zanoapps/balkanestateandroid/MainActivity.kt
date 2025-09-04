package com.zanoapps.balkanestateandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zanoapps.balkanestateandroid.ui.theme.BalkanEstateAndroidTheme
import com.zanoapps.core.presentation.designsystem.AddedToFavIcon
import com.zanoapps.core.presentation.designsystem.BalkanEstateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BalkanEstateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun Button() {

        Button(
            onClick = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary),
            enabled = true
        ) {
            Icon(
                imageVector = AddedToFavIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
            )
            Text(
                text = "Test for the guys",
                color = MaterialTheme.colorScheme.onPrimary


            )
        }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BalkanEstateAndroidTheme {
        Button()
    }
}