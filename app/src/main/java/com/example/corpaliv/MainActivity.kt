package com.example.corpaliv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.corpaliv.ui.screens.HomeScreen
import com.example.corpaliv.ui.screens.HomeScreen2
import com.example.corpaliv.ui.theme.CorpalivTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorpalivTheme {
                HomeScreen2()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Prevista() {
    CorpalivTheme{ HomeScreen2()}
    
}