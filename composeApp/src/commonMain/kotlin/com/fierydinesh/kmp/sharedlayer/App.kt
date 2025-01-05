package com.fierydinesh.kmp.sharedlayer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import onnxkmpsample.composeapp.generated.resources.Res
import onnxkmpsample.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var inputYear by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }
        var showContent by remember { mutableStateOf(false) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "KMP + ONNX App to predict the INR to USD exchange rate!",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = inputYear,
                onValueChange = {
                    inputYear = it
                    showError = false // Reset error when input changes
                },
                label = { Text("Enter Year") },
                placeholder = { Text("e.g. 2024") },
                modifier = Modifier.fillMaxWidth()
            )
            if (showError) {
                Text(
                    text = "Invalid year! Please enter a valid year.",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            val onnxInterface = remember { OnnxInference() }
            var result by remember { mutableStateOf(-1F) }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val validYears =
                        listOf(2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024)
                    val year = inputYear.toIntOrNull()

                    if (year != null &&
                        year in validYears
                    ) {
                        result = onnxInterface.predict(input = inputYear.toFloat())
                        showContent = true
                        showError = false
                    } else {
                        showError = true
                        showContent = false
                    }
                }) {
                Text("Check Exchange Rate")
            }

            AnimatedVisibility(visible = showContent) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Predicted INR to USD Rate for $inputYear: $result")
                }
            }
        }

    }
}