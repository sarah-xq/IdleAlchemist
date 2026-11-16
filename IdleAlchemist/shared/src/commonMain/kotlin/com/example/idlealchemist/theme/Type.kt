package com.example.idlealchemist.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val TomeFont = FontFamily.Monospace
private val Base = Typography()

/** Every style uses the tome's monospace look; sizes are only overridden where the game needs them. */
val ClickerTypography = Typography(
    displayLarge = TextStyle(fontFamily = TomeFont, fontSize = 40.sp, lineHeight = 46.sp, fontWeight = FontWeight.Black), // score
    headlineSmall = TextStyle(fontFamily = TomeFont, fontSize = 26.sp, fontWeight = FontWeight.Black),                    // floating +N
    titleMedium = TextStyle(fontFamily = TomeFont, fontSize = 17.sp, fontWeight = FontWeight.Bold),                       // upgrade name
    labelLarge = TextStyle(fontFamily = TomeFont, fontSize = 16.sp, fontWeight = FontWeight.Bold),                        // price
    labelSmall = TextStyle(fontFamily = TomeFont, fontSize = 12.sp, letterSpacing = 2.sp),                                // banner / section labels
    bodySmall = TextStyle(fontFamily = TomeFont, fontSize = 12.sp),                                                       // descriptions
    displayMedium = Base.displayMedium.copy(fontFamily = TomeFont),
    displaySmall = Base.displaySmall.copy(fontFamily = TomeFont),
    headlineLarge = Base.headlineLarge.copy(fontFamily = TomeFont),
    headlineMedium = Base.headlineMedium.copy(fontFamily = TomeFont),
    titleLarge = Base.titleLarge.copy(fontFamily = TomeFont),
    titleSmall = Base.titleSmall.copy(fontFamily = TomeFont),
    bodyLarge = Base.bodyLarge.copy(fontFamily = TomeFont),
    bodyMedium = Base.bodyMedium.copy(fontFamily = TomeFont),
    labelMedium = Base.labelMedium.copy(fontFamily = TomeFont, fontSize = 10.sp, letterSpacing = 0.sp),                   // nav labels
)
