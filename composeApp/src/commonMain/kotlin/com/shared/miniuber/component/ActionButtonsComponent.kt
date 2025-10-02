package com.shared.miniuber.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtonsComponent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    confirmText: String,
    cancelText: String? = null,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.onPrimary
    val errorColor = MaterialTheme.colorScheme.onErrorContainer
    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (cancelText != null) Arrangement.SpaceBetween else Arrangement.Center
        ) {
            ElevatedButton(
                onClick = onConfirmClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                if (isLoading) {
                    LoadingComponent()
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(18.dp)) {
                                val width = size.width
                                val height = size.height
                                drawLine(
                                    color = primaryColor,
                                    start = Offset(width * 0.2f, height * 0.5f),
                                    end = Offset(width * 0.45f, height * 0.7f),
                                    strokeWidth = width * 0.1f,
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    color = primaryColor,
                                    start = Offset(width * 0.45f, height * 0.7f),
                                    end = Offset(width * 0.8f, height * 0.3f),
                                    strokeWidth = width * 0.1f,
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = confirmText,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            if (cancelText != null) {
                Spacer(modifier = Modifier.width(12.dp))
                ElevatedButton(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(18.dp)) {
                                val width = size.width
                                val height = size.height
                                drawLine(
                                    color = errorColor,
                                    start = Offset(width * 0.2f, height * 0.2f),
                                    end = Offset(width * 0.8f, height * 0.8f),
                                    strokeWidth = width * 0.1f,
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    color = errorColor,
                                    start = Offset(width * 0.2f, height * 0.8f),
                                    end = Offset(width * 0.8f, height * 0.2f),
                                    strokeWidth = width * 0.1f,
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = cancelText,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}