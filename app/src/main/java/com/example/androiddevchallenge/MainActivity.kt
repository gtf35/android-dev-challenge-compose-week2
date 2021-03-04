/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.purple500
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val second = mutableStateOf(0)
        val inputEnable = mutableStateOf(true)
        setContent {
            MyTheme {
                CreateMainScreen(second, inputEnable)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (!isDestroyed) {
                delay(1000)
                if (!inputEnable.value)
                    if (second.value > 0)
                        second.value = second.value - 1
                    else
                        inputEnable.value = true
            }
        }
    }
}

@Composable
private fun CreateMainScreen(second: MutableState<Int>, inputEnable: MutableState<Boolean>) {
    Column(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.height(64.dp).fillMaxWidth().background(purple500)) {
            Text(
                "Countdown timer", modifier = Modifier.align(Alignment.CenterStart).padding(start = 20.dp),
                style = TextStyle(color = Color.White, fontSize = 20.sp)
            )
        }

        val textState = remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            value = textState.value,
            label = { Text("Timer: s") },
            onValueChange = {
                if (inputEnable.value) textState.value = it
                second.value = try {
                    it.toInt()
                } catch (e: Exception) { 0 }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(20.dp))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { inputEnable.value = !inputEnable.value }
        ) {
            Text(
                text = if (inputEnable.value) "Start" else "Stop"
            )
        }

        Box(Modifier.fillMaxSize()) {
            val m = second.value / 60
            val s = second.value % 60
            val mStr = if (m < 10) "0$m" else "$m"
            val sStr = if (s < 10) "0$s" else "$s"
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "$mStr:$sStr",
                style = TextStyle(fontSize = 100.sp)
            )
        }
    }
}
