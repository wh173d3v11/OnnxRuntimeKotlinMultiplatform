//
//  OnnxInterferenceManager.swift
//  iosApp
//
//  Created by Dinesh S on 02/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import OnnxRuntimeBindings

public class OnnxInferenceManager {
    
    public static func run(year:Float, model: String) throws -> Float? {
        var ortEnvironment: ORTEnv? // Variable to hold the ONNX Runtime Environment
        var ortSession: ORTSession? // Variable to hold the ONNX Runtime Session
        
            // Initialize ONNX runtime environment with a warning logging level.
            // If it fails, prints an error message and returns nil.
        do {
            ortEnvironment = try ORTEnv(loggingLevel: ORTLoggingLevel.warning)
        } catch {
            print("Failed to initialize ONNX Runtime Environment: \(error)")
            return nil
        }
        // Load the ONNX Model. If it fails, prints a message and returns nil.
        guard let modelPath = Bundle.main.resourcePath
        else {
            print("Failed to find the model file.")
            return nil
        }
        print("model name  modelPath : " + modelPath)
            // Create ORTSession. If it fails, prints an error message and returns nil.
        do {
            ortSession = try ORTSession(env: ortEnvironment!, modelPath: (modelPath + "/compose-resources/" + model), sessionOptions: ORTSessionOptions())
        } catch {
            print("Failed to create ONNX Runtime Session: \(error)")
            return nil
        }
            // Define an array for input
        let flatArray = [year]
            // Define the shape of the tensor
        let shape: [NSNumber] = [1, 1]
        
            // Create NSMutableData
        let data = NSMutableData()
        for value in flatArray {
            var float = value
            data.append(&float, length: MemoryLayout<Float>.size)
        }
        
            //  Convert your input data to an ORTValue
            //  An ORTValue encapsulates data used as an input or output to a model at runtime.
        let tensor = try ORTValue(tensorData: data, elementType: .float, shape: shape)
        
            // Run inference using the tensor, where "input" and "output" were specified in python code during the creating of the onnx model
        let outputKey: Set<String> =  ["output"]
        let outputTensor:[String:ORTValue]? = try ortSession?.run(withInputs: ["input": tensor], outputNames: outputKey, runOptions: nil)
            // Extract and process the output tensor data
        guard let outputData: NSMutableData = try outputTensor?["output"]?.tensorData() else{ return nil }
        let resultArray: [Float]? = arrayCopiedFromData(outputData as Data)
        return resultArray?.first
    }
        // Helper method to convert Data to an array of a specified type
    private static func arrayCopiedFromData<T>(_ data: Data) -> [T]? {
        guard data.count % MemoryLayout<T>.stride == 0 else { return nil }
        return data.withUnsafeBytes { bytes -> [T] in
            return Array(bytes.bindMemory(to: T.self))
        }
    }
}

