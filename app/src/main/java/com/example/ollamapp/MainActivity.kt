package com.example.ollamapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.verticalScroller
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import androidx.navigation.NavController
import androidx.navigation.rememberNavController
import androidx.navigation.compose.NavHostHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navHost
import com.example.ollamapp.ui.MainScreens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OllamaAppTheme {
                MaterialTheme
                val navController = rememberNavController()
                NavHost(navController, startDestination = "chat", fullScreen = true) {
                    composable("chat") { navController ->
                        MainScreens.ChatScreen(navController)
                    }
                    composable("models") { navController ->
                        MainScreens.ModelManagerScreen(navController)
                    }
                    composable("agent") { navController ->
                        MainScreens.CodingAgentScreen(navController)
                    }
                }
            }
        }
    }
}