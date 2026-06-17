package com.creativehazio.meals.presentation.mealscan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creativehazio.designsystem.components.GirlFitPrimaryButton
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.textSecondaryLight
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.back_icon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun MealScanScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: MealScanViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) { innerPadding ->
        MealScanScreen(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onEvent = event
        )
    }
}

@Composable
internal fun MealScanScreen(
    modifier: Modifier = Modifier,
    uiState: MealScanState,
    onEvent: (MealScanEvent) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }

    BindEffect(controller)

    var permissionState by remember { mutableStateOf(PermissionState.NotDetermined) }

    LaunchedEffect(Unit) {
        permissionState = controller.getPermissionState(Permission.CAMERA)

        if (permissionState == PermissionState.NotDetermined) {
            try {
                controller.providePermission(Permission.CAMERA)
                permissionState = PermissionState.Granted
            } catch (e: Exception) {
                permissionState = controller.getPermissionState(Permission.CAMERA)
            }
        }
    }



    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Large)
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(Res.drawable.back_icon),
                    contentDescription = null,
                )
            }
            Text(
                text = "Scan",
                style = MaterialTheme.typography.titleLarge
            )
            Box(Modifier.size(Sizing.IconLarge))

        }

        when (permissionState) {
            PermissionState.Granted -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(Color.Black)
                ) {
                    CameraPreview(
                        modifier = Modifier.matchParentSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .padding(vertical = 2.dp, horizontal = 2.dp)
                            .drawCaptureCorners(
                                color = MaterialTheme.colorScheme.onSecondary,
                                lineLength = 40.dp,
                                strokeWidth = 4.dp
                            )
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                            .size(72.dp)
                            .clickable(
                                onClick = {  }
                            )
                            .border(
                                width = 4.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            PermissionState.DeniedAlways, PermissionState.Denied, PermissionState.NotGranted -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .padding(Spacing.Large),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "We need camera access to scan your meals and calculate those calories!",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(Spacing.Large))
                    GirlFitPrimaryButton(
                        text = "Grant Camera Permission",
                        onClick = {
                            coroutineScope.launch {
                                if (permissionState == PermissionState.DeniedAlways) {
                                    controller.openAppSettings()
                                } else {
                                    try {
                                        controller.providePermission(Permission.CAMERA)
                                        permissionState = PermissionState.Granted
                                    } catch (e: Exception) {
                                        permissionState = controller.getPermissionState(Permission.CAMERA)
                                    }
                                }
                            }
                        },
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .background(Color.Black)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .padding(vertical = 2.dp, horizontal = 2.dp)
                            .drawCaptureCorners(
                                color = MaterialTheme.colorScheme.onSecondary,
                                lineLength = 40.dp,
                                strokeWidth = 4.dp
                            )
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                            .size(72.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {  }
                            )
                            .border(
                                width = 4.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }
        }

    }

}

@Composable
expect fun CameraPreview(modifier: Modifier)

fun Modifier.drawCaptureCorners(
    color: Color = Color.White,
    lineLength: Dp = 32.dp,
    strokeWidth: Dp = 4.dp
) = this.drawBehind {
    val length = lineLength.toPx()
    val stroke = strokeWidth.toPx()
    val w = size.width
    val h = size.height

    val path = Path().apply {
        moveTo(0f, length)
        lineTo(0f, 0f)
        lineTo(length, 0f)

        moveTo(w - length, 0f)
        lineTo(w, 0f)
        lineTo(w, length)

        moveTo(w, h - length)
        lineTo(w, h)
        lineTo(w - length, h)

        moveTo(length, h)
        lineTo(0f, h)
        lineTo(0f, h - length)
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = stroke)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
internal fun MealScanPreview() {
    MealScanScreen(
        uiState = MealScanState(),
        onEvent = {}
    )
}