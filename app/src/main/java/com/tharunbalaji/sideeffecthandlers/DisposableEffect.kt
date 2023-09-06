package com.tharunbalaji.sideeffecthandlers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class DisposableEffect: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayData()
        }
    }
}

@Preview
@Composable
fun DisplayData() {
    val nameList = remember { mutableStateOf(listOf<String>()) }
    var counter = remember { mutableStateOf(1) }

    DisposableEffect(key1 = Unit, effect = {
        nameList.value = fetchDataList()
        Log.d("REFRESH","Refresh occurred")
        onDispose {
            nameList.value = emptyList()
        }
    })


    Column(
        modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            counter.value++
            Log.d("COUNTER","Current value: ${counter.value}")}
        ) {
            Text(text = "Refresh")
        }

        Spacer(modifier = Modifier.size(20.dp))

        LazyColumn {
            items(nameList.value) { item ->
                Card(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 5.dp)) {
                    Text(text = item, modifier = Modifier.padding(10.dp))
                }
            }
        }
    }
}

fun fetchDataList(): List<String> {
    return listOf("Sam", "Joseph", "Ben")
}