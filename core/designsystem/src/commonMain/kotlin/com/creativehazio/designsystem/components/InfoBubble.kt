package com.creativehazio.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creativehazio.designsystem.theme.Sizing
import com.creativehazio.designsystem.theme.Spacing
import girlfit.core.designsystem.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoBubble(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    icon: DrawableResource? = null,
    text: String,
    subText: String? = null,
) {

    Card(
        modifier = modifier.wrapContentHeight()
            .shadow(
                elevation = Sizing.CardElevation,
                shape = MaterialTheme.shapes.extraLarge.copy(topStart = CornerSize(0.dp))
            ),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        shape = MaterialTheme.shapes.extraLarge.copy(topStart = CornerSize(0.dp)),
    ) {
        Row(
            modifier = Modifier.padding(Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Column {
                    Image(
                        modifier = Modifier.size(Sizing.IconSmall),
                        painter = painterResource(icon),
                        contentDescription = null
                    )
                }
            }
            Column {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight(500))
                )
                Spacer(Modifier.size(Spacing.ExtraSmall))
                subText?.let {
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight(500))
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun InfoBubblePreview() {
    InfoBubble(

//        icon = Res.drawable.carbohydrate,
        text = "Carbohydrates",
        subText = "100g"
    )
}
