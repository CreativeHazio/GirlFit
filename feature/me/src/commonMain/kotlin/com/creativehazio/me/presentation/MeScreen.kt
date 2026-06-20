package com.creativehazio.me.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.size.Size
import com.creativehazio.designsystem.components.GirlFitPrimaryButton
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import com.creativehazio.designsystem.theme.greyDisabledButtonLight
import com.creativehazio.designsystem.theme.textHighlightedLight
import girlfit.feature.me.generated.resources.Res
import girlfit.feature.me.generated.resources.countdown_time_icon
import girlfit.feature.me.generated.resources.feedback_icon
import girlfit.feature.me.generated.resources.language_icon
import girlfit.feature.me.generated.resources.pen_icon
import girlfit.feature.me.generated.resources.plus_icon
import girlfit.feature.me.generated.resources.premium_icon
import girlfit.feature.me.generated.resources.privacy_policy_icon
import girlfit.feature.me.generated.resources.rate_icon
import girlfit.feature.me.generated.resources.reminder_icon
import girlfit.feature.me.generated.resources.reset_progress_icon
import girlfit.feature.me.generated.resources.rest_time_icon
import girlfit.feature.me.generated.resources.right_arrow_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun MeScreenRoot(
    paddingValues: PaddingValues = PaddingValues.Zero,
    viewModel: MeViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent

    MeScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        onEvent = event
    )
}

@Composable
internal fun MeScreen(
    paddingValues: PaddingValues,
    uiState: MeState,
    onEvent: (MeEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(horizontal = Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        contentPadding = paddingValues
    ) {

        item {
            MeHeader(
                userImageUrl = "https://images.unsplash.com/photo-1692459356342-60362b105a69?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                userName = "Ria",
                isPremiumUser = false,
                userWins = uiState.userWins
            )
        }

        item {
            Text(
                text = "Workout Settings",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.size(Spacing.Large))
            SettingsRow(
                icon = Res.drawable.reminder_icon,
                title = "Reminder",
                subtitle = "00:00",
                trailingIcon = Res.drawable.plus_icon,
                onClick = {}
            )
            SettingsRow(
                icon = Res.drawable.rest_time_icon,
                title = "Rest time",
                subtitle = "30 secs",
                onClick = {}
            )
            SettingsRow(
                icon = Res.drawable.countdown_time_icon,
                title = "Countdown time",
                subtitle = "3 secs",
                onClick = {}
            )
        }

        item {
            Text(
                text = "General Settings",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.size(Spacing.Large))
            SettingsRow(
                icon = Res.drawable.language_icon,
                title = "Language",
                subtitle = "English",
                onClick = {}
            )
            SettingsRow(
                icon = Res.drawable.rate_icon,
                title = "Rate us",
                onClick = {}
            )
            SettingsRow(
                icon = Res.drawable.feedback_icon,
                title = "Feedback",
                onClick = {}
            )
            SettingsRow(
                icon = Res.drawable.privacy_policy_icon,
                title = "Privacy policy",
                onClick = {}
            )
            SettingsRow(
                icon = Res.drawable.premium_icon,
                title = "Subscription",
                onClick = {}
            )
        }

        item {
            GirlFitPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Reset progress",
                leadingIcon = Res.drawable.reset_progress_icon,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {}
            )
            Spacer(Modifier.size(20.dp))
        }
    }

}

@Composable
internal fun MeHeader(
    userImageUrl: String,
    userName: String,
    isPremiumUser: Boolean,
    userWins: List<UserWin>
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier.size(120.dp)
                .clip(CircleShape),
            model = userImageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                modifier = Modifier.size(Sizing.IconLarge),
                onClick = {}
            ) {
                Icon(
                    modifier = Modifier.size(Sizing.IconSmall),
                    painter = painterResource(Res.drawable.pen_icon),
                    contentDescription = null
                )
            }
        }

        GirlFitPrimaryButton(
            modifier = Modifier.width(230.dp),
            text = "Backup & Restore",
            onClick = {}
        )

        if (!isPremiumUser) {
            GirlFitPrimaryButton(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6FD7A0),
                                Color(0xFF6FD7A0),
                                textHighlightedLight,
                                MaterialTheme.colorScheme.secondary,
                            )
                        ),
                        shape = MaterialTheme.shapes.large
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                leadingIcon = Res.drawable.premium_icon,
                leadingIconSize = Sizing.IconMedium,
                text = "Go Premium",
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.size(1.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    top = Spacing.Medium,
                    start = Spacing.Medium,
                    end = Spacing.Medium,
                    bottom = Spacing.Large
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Wins",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF2C5640)
                        ),
                        onClick = {}
                    ) {
                        Text(
                            text = "View all",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    userWins.forEach { 
                        WinsIconAndText(
                            drawable = it.image,
                            text = it.name
                        )
                    }
                }
            }
        }

    }

}

@Composable
internal fun WinsIconAndText(
    drawable: DrawableResource,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(drawable),
            contentDescription = null
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun SettingsRow(
    icon: DrawableResource,
    title: String,
    subtitle: String? = null,
    trailingIcon: DrawableResource = Res.drawable.right_arrow_icon,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Sizing.IconLarge)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(Sizing.IconSmall)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Icon(
                painter = painterResource(trailingIcon),
                contentDescription = null,
                modifier = Modifier.size(Sizing.IconMedium)
            )
        }
        SettingsDivider()
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = greyDisabledButtonLight
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
internal fun MeScreenPreview() {
    MeHeader(
        userImageUrl = "",
        userName = "Ria",
        isPremiumUser = false,
        userWins = emptyList()
    )
}