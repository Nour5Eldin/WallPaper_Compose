package com.noureldin.wallpaperhub.presentation.util

import androidx.compose.material3.SnackbarDuration

data class SnackbarEvent(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short
)