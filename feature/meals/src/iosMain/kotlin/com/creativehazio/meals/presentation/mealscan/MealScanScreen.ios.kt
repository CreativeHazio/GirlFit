package com.creativehazio.meals.presentation.mealscan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.QuartzCore.CATransaction
import platform.UIKit.UIView
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraPreview(modifier: Modifier) {
    val cameraSession = remember { AVCaptureSession() }
    val previewLayer = remember { AVCaptureVideoPreviewLayer(session = cameraSession) }

    DisposableEffect(Unit) {
        onDispose {
            dispatch_async(
                dispatch_get_global_queue(
                    DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(),
                    0u
                )
            ) {
                if (cameraSession.isRunning()) {
                    cameraSession.stopRunning()
                }
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val cameraContainer = UIView()
            previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            cameraContainer.layer.addSublayer(previewLayer)

            dispatch_async(
                dispatch_get_global_queue(
                    DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(),
                    0u
                )
            ) {
                val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
                if (device != null) {
                    val input = AVCaptureDeviceInput.deviceInputWithDevice(device, null)
                    if (input != null && cameraSession.canAddInput(input)) {
                        cameraSession.addInput(input)
                    }
                }

                cameraSession.startRunning()
            }

            cameraContainer
        },
        update = { view ->
            CATransaction.begin()
            CATransaction.setDisableActions(true)

            previewLayer.frame = view.bounds

            CATransaction.commit()
        }
    )
}