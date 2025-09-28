package com.shared.miniuber.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AimComponent(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.size(48.dp)
    ) {
        drawLine(
            color = Color.Red,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            strokeWidth = 4f
        )
        drawLine(
            color = Color.Red,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 4f
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAimComponent() {
    AimComponent()
}