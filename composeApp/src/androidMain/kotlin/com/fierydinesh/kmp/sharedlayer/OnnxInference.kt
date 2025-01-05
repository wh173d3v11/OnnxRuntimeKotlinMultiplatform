package com.fierydinesh.kmp.sharedlayer

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import kotlinx.coroutines.runBlocking
import onnxkmpsample.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes
import java.nio.FloatBuffer

actual class OnnxInference actual constructor() {

    private val context: Context = MainApp.appInstance.applicationContext

    actual fun predict(
        input: Float, // input eg: 2025f,
        model: String
    ): Float {
        val ortEnvironment: OrtEnvironment = getEnvironment()
        val ortSession: OrtSession = getOrtSession(model, ortEnvironment)
        val result: Float = run(input, ortSession, ortEnvironment)
        return result
    }

    private fun getEnvironment() = OrtEnvironment.getEnvironment()

    @OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
    private fun getOrtSession(
        model: String,
        ortEnvironment: OrtEnvironment
    ): OrtSession {
        // Reads the model bytes from the resources.
        val modelBytes: ByteArray = runBlocking {
            readResourceBytes(model)
            //context.resources.openRawResource(scikitModel).readBytes()
        }
        // Creates a session using the model bytes.
        return ortEnvironment.createSession(modelBytes)
    }

    // Make predictions with given inputs
    private fun run(
        input: Float, ortSession: OrtSession, ortEnvironment: OrtEnvironment
    ): Float {
        // Get the name of the input and output nodes
        val inputName: String? = ortSession.inputNames?.iterator()?.next() //"input"
        val outputName: Set<String> = HashSet(ortSession.outputNames) //"output"
        // Make a FloatBuffer of the inputs
        val floatBufferInputs = FloatBuffer.wrap(floatArrayOf(input))
        // Create input tensor
        val inputTensor: OnnxTensor = OnnxTensor.createTensor(
            ortEnvironment,
            floatBufferInputs,
            longArrayOf(1, 1) //shape of input tensor
        )
        // Run the model
        val results: OrtSession.Result = ortSession.run(mapOf(inputName to inputTensor), outputName)
        // Fetch and return the results
        val output: Array<FloatArray> = results[0].value as Array<FloatArray>
        return output[0][0]
    }
}