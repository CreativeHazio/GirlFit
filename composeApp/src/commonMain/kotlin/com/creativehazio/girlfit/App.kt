package com.creativehazio.girlfit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creativehazio.designsystem.components.InfoBubble
import com.creativehazio.designsystem.components.PrimaryButton
import com.creativehazio.designsystem.components.SearchBar
import com.creativehazio.designsystem.components.WorkoutCard
import com.creativehazio.designsystem.theme.GirlFitTheme
import org.jetbrains.compose.resources.painterResource

import girlfit.composeapp.generated.resources.Res
import girlfit.composeapp.generated.resources.carbohydrate

@Composable
fun App() {

    GirlFitTheme {
        var showContent by remember { mutableStateOf(false) }
        var query by remember { mutableStateOf("")}
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryButton(
                text = "Click me!",
                onClick = { showContent = !showContent }
            )
            Spacer(Modifier.size(20.dp))
            SearchBar(
                query = query,
                onQueryChange = {
                    query = it
                },
                placeholderText = "e.g weightloss meals",
                onSearchPressed = {},
                showFilterIcon = true,
                onFilterClick = {}
            )
            Spacer(Modifier.size(20.dp))
            WorkoutCard(
                imageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400",
                title = "Flat \nStomach",
                durationText = "🕑7 mins",
                buttonText = "Start",
                onCardClick = {}
            )
            Spacer(Modifier.size(20.dp))
            InfoBubble(
                color = MaterialTheme.colorScheme.secondary,
                icon = Res.drawable.carbohydrate,
                text = "Carbohydrates",
                subText = "100g"
            )
        }
    }
}