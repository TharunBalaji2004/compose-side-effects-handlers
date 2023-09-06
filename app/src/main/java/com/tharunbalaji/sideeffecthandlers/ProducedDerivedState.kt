package com.tharunbalaji.sideeffecthandlers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class ProducedDerivedState: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProducedState()
        }
    }
}

@Composable
fun ProducedState() {

    val state = produceState(initialValue = 0, producer = {
        for (i in 1..10){
            delay(1000)
            value++
        }
    })

    Text(text = state.value.toString(), style = MaterialTheme.typography.headlineLarge)
}

@Composable
fun DerivedState() {
    var count by remember { mutableStateOf(0) }
    var doubleCount by remember { mutableStateOf(0) }

    // Calculate a derived state: double the count
    val derivedDoubleCount by derivedStateOf {
        doubleCount = count * 2
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Count: $count", fontSize = 24.sp)
        Text(text = "Double Count: $derivedDoubleCount", fontSize = 24.sp)

        Button(
            onClick = {
                // Update the count
                count++
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Increment Count")
        }
    }
}