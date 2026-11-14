package com.ehan.htmlrunner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
