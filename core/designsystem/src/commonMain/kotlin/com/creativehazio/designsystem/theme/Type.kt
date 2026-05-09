package com.creativehazio.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography

import girlfit.core.designsystem.generated.resources.Res
import girlfit.core.designsystem.generated.resources.poppins_medium
import girlfit.core.designsystem.generated.resources.poppins_regular
import girlfit.core.designsystem.generated.resources.poppins_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun getPoppinsFontFamily() = FontFamily(
    Font(Res.font.poppins_regular, FontWeight.Normal),
    Font(Res.font.poppins_medium, FontWeight.Medium),
    Font(Res.font.poppins_semibold, FontWeight.SemiBold)
)

@Composable
fun girlFitAppTypography(): Typography {
    val poppins = getPoppinsFontFamily()

    return Typography(
        // H1: 20px, SemiBold
        headlineSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
        ),
        // H2: 16px, SemiBold
        titleLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        ),
        // H3: 16px, Medium
        titleMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        // H4: 16px, Regular
        bodyLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        // 14px, Medium (Ideal for Buttons and Tabs)
        labelLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        ),
        // Body text: 14px, Regular
        bodyMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        // Body text: 12px, Medium
        labelMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
        ),
        // Hint text / Body text: 12px, Regular
        bodySmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
        // Label: 10px, Regular
        labelSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
        )
    )
}