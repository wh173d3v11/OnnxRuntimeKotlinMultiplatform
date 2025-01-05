package com.fierydinesh.kmp.sharedlayer

import onnxkmpsample.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

fun getResPath(input: String): String = ("composeResources/onnxkmpsample.composeapp.generated.resources/" + input)

expect class OnnxInference() {
    @OptIn(ExperimentalResourceApi::class)
    fun predict(input: Float, model: String = getResPath("files/linear_model.onnx")): Float
}