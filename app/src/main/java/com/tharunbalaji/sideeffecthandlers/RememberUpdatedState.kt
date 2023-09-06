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
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RememberUpdatedState: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    var counter = remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = Unit, block = {
        delay(1000)
        counter.value = 10
    })

    Counter(counter.value)
}

@Composable
fun Counter(value: Int) {
    val state = rememberUpdatedState(newValue = value)
    Log.d("DEBUG", "Counter Composition occurred")
    LaunchedEffect(key1 = Unit, block = {
        delay(3000)
        Log.d("DEBUG", state.value.toString())
    })
    Text(text = value.toString())
}