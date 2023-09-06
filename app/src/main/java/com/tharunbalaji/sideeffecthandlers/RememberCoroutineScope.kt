package com.tharunbalaji.sideeffecthandlers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RememberCoroutineScope: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HasSideEffect()
        }
    }
}

@Composable
fun HasSideEffect() {
    val counter = remember { mutableStateOf(0) }
    var coroutineScope = rememberCoroutineScope()
    
    var text = "Counter is running ${counter.value}"
    if (counter.value == 10) text = "Counter stopped"
    
    Column {
        Text(text = text)
        Button(onClick = {
            coroutineScope.launch {
                Log.d("DEBUG", "Started....")
            
                try {
                    for (i in 1..10){
                        counter.value++
                        delay(1000)
                    } 
                } catch(e: Exception) {
                    Log.d("DEBUG",e.message.toString())
                }
            }
        }) {
            Text(text = "START")
        }
    }
}