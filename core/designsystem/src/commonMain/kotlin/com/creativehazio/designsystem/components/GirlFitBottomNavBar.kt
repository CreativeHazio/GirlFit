package com.creativehazio.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class BottomBarTab<T>(
    val route: T,
    val title: String,
    val unselectedIcon: DrawableResource,
    val selectedIcon: DrawableResource
)

@Composable
fun <T> GirlFitBottomBar(
    modifier: Modifier = Modifier,
    tabs: List<BottomBarTab<T>>,
    currentRoute: T?,
    onTabSelected: (T) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {

        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            HorizontalDivider(thickness = 0.2.dp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Row(
                modifier = Modifier
                    .padding(Spacing.Small)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val isSelected = currentRoute == tab.route

                    CustomBottomBarItem(
                        tab = tab,
                        isSelected = isSelected,
                        onClick = { onTabSelected(tab.route) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun <T> CustomBottomBarItem(
    tab: BottomBarTab<T>,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val selectedColor = Color(0xFF2C5640)
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    val contentColor = if (isSelected) selectedColor else unselectedColor
    val iconToUse = if (isSelected) tab.selectedIcon else tab.unselectedIcon

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(iconToUse),
            contentDescription = tab.title,
            tint = if (isSelected) Color.Unspecified else unselectedColor,
            modifier = Modifier.size(Sizing.IconMedium)
        )

        Spacer(modifier = Modifier.height(Spacing.ExtraSmall))

        Text(
            text = tab.title,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}
