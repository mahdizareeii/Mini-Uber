package com.shared.miniuber.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AimComponent(
    modifier: Modifier = Modifier,
    pinColor: Color = Color.Black,
    holeColor: Color = Color.White
) {
    Canvas(
        modifier = modifier.size(
            width = 40.dp,
            height = 60.dp
        )
    ) {
        val width = size.width
        val height = size.height
        val bulbDiameter = width
        val bulbRadius = bulbDiameter / 2f

        val path = Path().apply {
            moveTo(0f, bulbRadius)

            arcTo(
                rect = Rect(0f, 0f, bulbDiameter, bulbDiameter),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )

            quadraticTo(
                bulbDiameter, bulbRadius + (height - bulbDiameter) / 2,
                bulbRadius, height
            )

            quadraticTo(
                0f, bulbRadius + (height - bulbDiameter) / 2,
                0f, bulbRadius
            )

            close()
        }

        drawPath(path, pinColor)

        drawCircle(
            color = holeColor,
            radius = bulbRadius / 2.5f,
            center = Offset(bulbRadius, bulbRadius)
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0)
fun PreviewAimComponent() {
    Box(modifier = Modifier.size(200.dp)) {
        AimComponent(modifier = Modifier.align(Alignment.Center))
    }
}