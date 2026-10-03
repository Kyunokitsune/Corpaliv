package com.example.corpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.corpaliv.ui.screens.HomeScreen
import com.example.corpaliv.ui.theme.CorpalivTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivTheme {
                HomeScreen()
            }
        }
    }
}