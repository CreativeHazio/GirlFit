//package com.creativehazio.designsystem.components
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.unit.dp
//import org.jetbrains.compose.resources.DrawableResource
//import org.jetbrains.compose.resources.painterResource
//
//data class BottomBarTab(
//    val route: Route,
//    val title: String,
//    val icon: DrawableResource
//)
//
//@Composable
//fun GirlFitBottomBar(
//    tabs: List<BottomBarTab>,
//    currentRoute: Route?,
//    onTabSelected: (Route) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Surface(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 24.dp, vertical = 16.dp),
//        shape = CircleShape,
//        shadowElevation = 8.dp,
//        color = MaterialTheme.colorScheme.surface
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(8.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            tabs.forEach { tab ->
//                val isSelected = currentRoute == tab.route
//
//                CustomBottomBarItem(
//                    tab = tab,
//                    isSelected = isSelected,
//                    onClick = { onTabSelected(tab.route) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun CustomBottomBarItem(
//    tab: BottomBarTab,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    // Smoothly animate the background width when selected
//    val backgroundOpacity by animateFloatAsState(
//        targetValue = if (isSelected) 0.1f else 0f,
//        animationSpec = tween(300)
//    )
//
//    Row(
//        modifier = Modifier
//            .clip(CircleShape)
//            .background(MaterialTheme.colorScheme.primary.copy(alpha = backgroundOpacity))
//            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null, // Removes the standard ripple for a cleaner look
//                onClick = onClick
//            )
//            .padding(horizontal = 16.dp, vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        Icon(
//            painter = painterResource(tab.icon),
//            contentDescription = tab.title,
//            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
//            modifier = Modifier.size(24.dp)
//        )
//
//        // Only show the text if the tab is selected!
//        AnimatedVisibility(visible = isSelected) {
//            Text(
//                text = tab.title,
//                color = MaterialTheme.colorScheme.primary,
//                style = MaterialTheme.typography.labelLarge,
//                modifier = Modifier.padding(start = 8.dp)
//            )
//        }
//    }
//}