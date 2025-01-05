package com.fierydinesh.kmp.sharedlayer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ONNX KMP Sample",
    ) {
        App()
    }
}