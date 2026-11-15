package com.ehan.htmlrunner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import com.ehan.htmlrunner.ui.AppNav
import com.ehan.htmlrunner.ui.theme.HtmlRunnerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HtmlRunnerTheme { AppNav() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val app_name: String = "HTML Runner"
    HtmlRunnerTheme {
        Text(
            text = app_name
            textAlign = TextAlign.Center,
        )
    }
}
