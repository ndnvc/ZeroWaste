package com.example.zerowaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.zerowaste.ui.login.LoginScreen
import com.example.zerowaste.ui.theme.ZeroWasteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZeroWasteTheme {
                LoginScreen()
            }
        }
    }
}
