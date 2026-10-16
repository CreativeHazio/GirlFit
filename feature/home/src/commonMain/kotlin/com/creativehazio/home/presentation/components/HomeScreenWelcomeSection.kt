package com.creativehazio.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.feature.home.generated.resources.Res
import girlfit.feature.home.generated.resources.angry_emoji
import girlfit.feature.home.generated.resources.exhausted_emoji
import girlfit.feature.home.generated.resources.lollipop
import girlfit.feature.home.generated.resources.sad_emoji
import girlfit.feature.home.generated.resources.sick_emoji
import girlfit.feature.home.generated.resources.smile_emoji
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun HomeScreenWelcomeSection() {
    val feelings = remember {
        mutableStateMapOf(
            "Happy" to Res.drawable.smile_emoji,
            "Sad" to Res.drawable.sad_emoji,
            "Sick" to Res.drawable.sick_emoji,
            "Tired" to Res.drawable.exhausted_emoji,
            "Angry" to Res.drawable.angry_emoji,
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Text(
                    "Hi Ria ❤️",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "How do you feel today?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
            ) {
                Image(
                    modifier = Modifier.size(Sizing.IconExtraLarge),
                    painter = painterResource(Res.drawable.lollipop),
                    contentDescription = null
                )
                Text(
                    "6",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

        }

        Spacer(Modifier.size(Spacing.Large))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            feelings.forEach { resource ->
                Column(
                    modifier = Modifier.clickable {

                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
                ) {
                    Image(
                        modifier = Modifier.size(Sizing.IconLarge),
                        painter = painterResource(resource.value),
                        contentDescription = null
                    )
                    Text(resource.key, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

    }
}