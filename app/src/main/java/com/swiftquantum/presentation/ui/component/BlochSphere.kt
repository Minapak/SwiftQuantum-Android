package com.swiftquantum.presentation.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.swiftquantum.R
import com.swiftquantum.domain.model.BlochSphereState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BlochSphere(
    state: BlochSphereState,
    modifier: Modifier = Modifier,
    sphereColor: Color = Color(0xFF6366F1),
    axisXColor: Color = Color(0xFFEF4444),
    axisYColor: Color = Color(0xFF22C55E),
    axisZColor: Color = Color(0xFF3B82F6),
    stateVectorColor: Color = Color(0xFFF59E0B)
) {
    val theta = state.theta
    val phi = state.phi

    Column(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = minOf(size.width, size.height) / 2.5f

                // Draw sphere outline
                drawCircle(
                    color = sphereColor.copy(alpha = 0.3f),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Draw equator (XY plane)
                drawEllipse(
                    color = sphereColor.copy(alpha = 0.2f),
                    centerX = centerX,
                    centerY = centerY,
                    radiusX = radius,
                    radiusY = radius * 0.3f,
                    strokeWidth = 1.dp.toPx()
                )

                // Draw meridian (XZ plane)
                drawCircle(
                    color = sphereColor.copy(alpha = 0.2f),
                    radius = radius,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 1.dp.toPx())
                )

                // Draw axes
                val axisLength = radius * 1.2f

                // X axis (horizontal, red)
                drawAxis(
                    startX = centerX - axisLength,
                    startY = centerY,
                    endX = centerX + axisLength,
                    endY = centerY,
                    color = axisXColor,
                    label = "X"
                )

                // Y axis (diagonal, green) - perspective
                val yOffset = axisLength * 0.7f
                drawAxis(
                    startX = centerX - yOffset * 0.7f,
                    startY = centerY + yOffset * 0.3f,
                    endX = centerX + yOffset * 0.7f,
                    endY = centerY - yOffset * 0.3f,
                    color = axisYColor,
                    label = "Y"
                )

                // Z axis (vertical, blue)
                drawAxis(
                    startX = centerX,
                    startY = centerY + axisLength,
                    endX = centerX,
                    endY = centerY - axisLength,
                    color = axisZColor,
                    label = "Z"
                )

                // Draw |0⟩ and |1⟩ labels
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 14.dp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    drawText("|0⟩", centerX, centerY - radius - 20.dp.toPx(), paint)
                    drawText("|1⟩", centerX, centerY + radius + 30.dp.toPx(), paint)
                }

                // Calculate state vector position on sphere
                val stateX = sin(theta) * cos(phi)
                val stateY = sin(theta) * sin(phi)
                val stateZ = cos(theta)

                // Project 3D to 2D (simple orthographic projection)
                val projX = centerX + radius * stateX
                val projY = centerY - radius * stateZ // Invert Z for screen coordinates

                // Draw state vector
                drawLine(
                    color = stateVectorColor,
                    start = Offset(centerX, centerY),
                    end = Offset(projX, projY),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Draw state point
                drawCircle(
                    color = stateVectorColor,
                    radius = 8.dp.toPx(),
                    center = Offset(projX, projY)
                )

                // Draw shadow on equator
                val shadowX = centerX + radius * stateX
                val shadowY = centerY + radius * stateY * 0.3f
                drawCircle(
                    color = stateVectorColor.copy(alpha = 0.3f),
                    radius = 4.dp.toPx(),
                    center = Offset(shadowX, shadowY)
                )

                // Draw dashed line to shadow
                drawDashedLine(
                    color = stateVectorColor.copy(alpha = 0.3f),
                    start = Offset(projX, projY),
                    end = Offset(shadowX, shadowY),
                    dashLength = 4.dp.toPx(),
                    gapLength = 4.dp.toPx()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // State information
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.state_vector),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StateParameter(
                        label = "θ",
                        value = String.format("%.2f°", Math.toDegrees(theta)),
                        color = axisZColor
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    StateParameter(
                        label = "φ",
                        value = String.format("%.2f°", Math.toDegrees(phi)),
                        color = axisXColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CoordinateDisplay("X", state.x, axisXColor)
                    Spacer(modifier = Modifier.width(16.dp))
                    CoordinateDisplay("Y", state.y, axisYColor)
                    Spacer(modifier = Modifier.width(16.dp))
                    CoordinateDisplay("Z", state.z, axisZColor)
                }
            }
        }
    }
}

@Composable
private fun StateParameter(
    label: String,
    value: String,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label = $value",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CoordinateDisplay(
    axis: String,
    value: Double,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$axis: ${String.format("%.3f", value)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun DrawScope.drawAxis(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    color: Color,
    label: String
) {
    drawLine(
        color = color,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )

    // Draw arrow at end
    val arrowSize = 8.dp.toPx()
    val angle = kotlin.math.atan2(endY - startY, endX - startX)
    val arrowAngle = PI / 6

    drawLine(
        color = color,
        start = Offset(endX, endY),
        end = Offset(
            endX - arrowSize * cos(angle - arrowAngle).toFloat(),
            endY - arrowSize * sin(angle - arrowAngle).toFloat()
        ),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawLine(
        color = color,
        start = Offset(endX, endY),
        end = Offset(
            endX - arrowSize * cos(angle + arrowAngle).toFloat(),
            endY - arrowSize * sin(angle + arrowAngle).toFloat()
        ),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawEllipse(
    color: Color,
    centerX: Float,
    centerY: Float,
    radiusX: Float,
    radiusY: Float,
    strokeWidth: Float
) {
    val path = Path()
    val segments = 60
    for (i in 0..segments) {
        val angle = 2 * PI * i / segments
        val x = centerX + radiusX * cos(angle).toFloat()
        val y = centerY + radiusY * sin(angle).toFloat()
        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    drawPath(path, color, style = Stroke(width = strokeWidth))
}

private fun DrawScope.drawDashedLine(
    color: Color,
    start: Offset,
    end: Offset,
    dashLength: Float,
    gapLength: Float
) {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val distance = kotlin.math.sqrt(dx * dx + dy * dy)
    val dashCount = (distance / (dashLength + gapLength)).toInt()

    for (i in 0 until dashCount) {
        val startRatio = i * (dashLength + gapLength) / distance
        val endRatio = (i * (dashLength + gapLength) + dashLength) / distance
        drawLine(
            color = color,
            start = Offset(
                start.x + dx * startRatio,
                start.y + dy * startRatio
            ),
            end = Offset(
                start.x + dx * endRatio.coerceAtMost(1f),
                start.y + dy * endRatio.coerceAtMost(1f)
            ),
            strokeWidth = 1.dp.toPx()
        )
    }
}
