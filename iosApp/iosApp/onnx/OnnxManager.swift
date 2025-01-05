//
//  OnnxManager.swift
//  iosApp
//
//  Created by Dinesh S on 02/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

@objc public class OnnxManager : NSObject{
    
    @objc public static func runWithModel(year:Float, model: String) -> NSNumber {
        do {
            let result = try OnnxInferenceManager.run(year: year, model: model)
            return NSNumber(value: result ?? -1)
        } catch {
            print("Failed to run OnnxInferenceManager: \(error)")
        }
        return -1.0
    }
}
