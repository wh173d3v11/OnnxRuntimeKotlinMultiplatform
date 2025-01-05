package com.fierydinesh.kmp.sharedlayer

import com.fierydinesh.onnx.OnnxManager
import kotlinx.cinterop.ExperimentalForeignApi

actual class OnnxInference {

    @OptIn(ExperimentalForeignApi::class)
    actual fun predict(input: Float, model: String): Float {
        return OnnxManager.runWithModelWithYear(input, model).floatValue
    }
}
