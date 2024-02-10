//Main Activity

package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.ai.client.generativeai.GenerativeModel
import ui.GenerateTextWithInput

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val generativeModel = GenerativeModel("gemini-pro","AIzaSyA66bywioo4S92W02KYZU69QV1wHDayv1o")

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GenerateTextWithInput(generativeModel, this@MainActivity)
                }
            }
        }
    }
}

