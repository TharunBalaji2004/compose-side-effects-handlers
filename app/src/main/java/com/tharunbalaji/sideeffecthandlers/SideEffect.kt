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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class SideEffect: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayList()
        }
    }
}

@Preview
@Composable
fun DisplayList() {
    val nameList = remember { mutableStateOf(listOf<String>()) }
    var counter = remember { mutableStateOf(1) }

    SideEffect {
        nameList.value = fetchData()
        Log.d("REFRESH","Refresh is occured")
    }

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

fun fetchList(): List<String> {
    return listOf("Sam", "Joseph", "Ben")
}