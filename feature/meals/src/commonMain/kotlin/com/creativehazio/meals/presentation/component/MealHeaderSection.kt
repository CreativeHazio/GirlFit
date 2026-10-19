package com.creativehazio.meals.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.creativehazio.designsystem.components.GirlFitInfoBubble
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.meals.generated.resources.Res
import girlfit.feature.meals.generated.resources.camera_track_calories
import girlfit.feature.meals.generated.resources.gradient_cam
import girlfit.feature.meals.generated.resources.premium_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MealHeaderSection(
    onMealScanCamClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(Sizing.IconMedium),
                painter = painterResource(Res.drawable.premium_icon),
                contentDescription = null
            )
        }

        GirlFitInfoBubble(
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 40.dp, end = Spacing.Medium),
            text = stringResource(Res.string.camera_track_calories),
            onClick = onMealScanCamClicked
        )

        Image(
            modifier = Modifier.align(Alignment.TopEnd)
                .padding(top = 87.dp, end = 180.dp)
                .size(60.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onMealScanCamClicked()
                },
            painter = painterResource(Res.drawable.gradient_cam),
            contentDescription = null
        )
    }
}