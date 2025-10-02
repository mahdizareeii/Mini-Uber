package com.shared.miniuber.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WaitingAnimationComponent() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val colorPrimary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .padding(32.dp)
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
        )
        
        Canvas(modifier = Modifier.size(50.dp)) {
            drawRoundRect(
                color = colorPrimary,
                topLeft = Offset(size.width * 0.2f, size.height * 0.4f),
                size = androidx.compose.ui.geometry.Size(size.width * 0.6f, size.height * 0.3f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
            
            drawRoundRect(
                color = colorPrimary,
                topLeft = Offset(size.width * 0.3f, size.height * 0.25f),
                size = androidx.compose.ui.geometry.Size(size.width * 0.4f, size.height * 0.2f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
            
            drawCircle(
                color = Color.DarkGray,
                radius = size.width * 0.08f,
                center = Offset(size.width * 0.3f, size.height * 0.7f)
            )
            drawCircle(
                color = Color.DarkGray,
                radius = size.width * 0.08f,
                center = Offset(size.width * 0.7f, size.height * 0.7f)
            )
        }
        
        CircularProgressIndicator(
            modifier = Modifier.size(100.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp
        )
    }
}