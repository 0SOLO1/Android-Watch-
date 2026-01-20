package com.example.mf_watch.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import com.example.mf_watch.data.PortfolioModel

@Composable
fun PortfolioOverviewRound(
    portfolio: PortfolioModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Time moved slightly down from the top edge
        TimeText(
            timeSource = TimeTextDefaults.timeSource("h:mm a"),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp),
            timeTextStyle = TimeTextDefaults.timeTextStyle(
                fontSize = 8.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        )

        // Progress Rings
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 7.dp.toPx()
                val diameter = size.minDimension - strokeWidth - 10.dp.toPx()
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                val isProfit = portfolio.returnPercentage >= 0
                val accentColor = if (isProfit) Color(0xFF30D158) else Color(0xFFFF3B30)
                
                // Background Track - Rounded caps
                drawArc(
                    color = Color(0xFF121212),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Progress Arc Calculation
                val progressAngle = (portfolio.returnPercentage.toFloat() / 100f * 360f).coerceIn(0f, 360f)
                
                // Enhanced Glow (Maintaining relative thickness)
                drawArc(
                    color = accentColor.copy(alpha = 0.4f),
                    startAngle = -90f,
                    sweepAngle = progressAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth + 2.dp.toPx(), cap = StrokeCap.Round)
                )

                // Main Progress Arc - Rounded caps
                drawArc(
                    color = accentColor,
                    startAngle = -90f,
                    sweepAngle = progressAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Main Content Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Wallet Icon - Made slightly smaller with more top padding
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = Color(0xFF0A84FF).copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFF0A84FF).copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(15.dp)) {
                    val w = size.width
                    val h = size.height
                    val primaryColor = Color(0xFF0A84FF)
                    
                    // Wallet drawing using primary color
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(0f, h * 0.15f),
                        size = Size(w, h * 0.7f),
                        cornerRadius = CornerRadius(2.dp.toPx())
                    )
                    drawRoundRect(
                        color = Color.Black.copy(alpha = 0.2f),
                        topLeft = Offset(w * 0.45f, h * 0.35f),
                        size = Size(w * 0.45f, h * 0.3f),
                        cornerRadius = CornerRadius(1.dp.toPx())
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.8f),
                        radius = 0.8.dp.toPx(),
                        center = Offset(w * 0.8f, h * 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Value
            Text(
                text = "₹${formatLakhs(portfolio.totalValue)}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Green Pill
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF30D158).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFF00E676).copy(alpha = 0.3f),
                            style = Stroke(width = 1.dp.toPx()),
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(11.dp)) {
                        val scale = size.width / 24f
                        val trendColor = Color(0xFF22C55E)
                        val sw = 1.3.dp.toPx()
                        
                        // Main trend line (M21,7 l-6.79,6.79 ... L3,17)
                        val trendPath = Path().apply {
                            moveTo(21f * scale, 7f * scale)
                            lineTo(14.21f * scale, 13.79f * scale)
                            lineTo(11.79f * scale, 11.21f * scale)
                            lineTo(3f * scale, 17f * scale)
                        }
                        
                        drawPath(
                            path = trendPath,
                            color = trendColor,
                            style = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                        
                        // Arrow head (polyline 21 11 21 7 17 7)
                        val headPath = Path().apply {
                            moveTo(21f * scale, 11f * scale)
                            lineTo(21f * scale, 7f * scale)
                            lineTo(17f * scale, 7f * scale)
                        }
                        
                        drawPath(
                            path = headPath,
                            color = trendColor,
                            style = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+${String.format("%.2f", portfolio.returnPercentage)}%",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF22C55E)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Stats
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${portfolio.fundCount}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Funds",
                        fontSize = 7.sp,
                        color = Color.Gray.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                
                Box(modifier = Modifier.width(1.dp).height(15.dp).background(Color.White.copy(alpha = 0.3f)))

                Spacer(modifier = Modifier.width(12.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "₹${formatLakhs(portfolio.totalInvested)}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Invested",
                        fontSize = 7.sp,
                        color = Color.Gray.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(17.dp))

            // // Swipe Hint with Drawn Icon
            // Row(
            //     verticalAlignment = Alignment.CenterVertically,
            //     horizontalArrangement = Arrangement.Center
            // ) {
            //     Canvas(modifier = Modifier.size(14.dp)) {
            //         val w = size.width
            //         val h = size.height
            //         drawArc(
            //             color = Color.White.copy(alpha = 0.4f),
            //             startAngle = 60f,
            //             sweepAngle = 240f,
            //             useCenter = false,
            //             style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
            //         )
            //         // arrow head
            //         val path = Path().apply {
            //             moveTo(w * 0.1f, h * 0.2f)
            //             lineTo(w * 0.25f, h * 0.1f)
            //             lineTo(w * 0.35f, h * 0.25f)
            //         }
            //         drawPath(path, color = Color.White.copy(alpha = 0.4f), style = Stroke(width = 1.2.dp.toPx()))
            //     }
            //     Spacer(modifier = Modifier.width(6.dp))
            //     Text(
            //         text = "Swipe for funds",
            //         fontSize = 6.sp,
            //         color = Color.White.copy(alpha = 0.4f)
            //     )
            // }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Pager Indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF2196F3))
            )
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            )
        }
    }
}



private fun formatLakhs(value: Double): String {
    return if (value >= 100000) {
        val lakhs = value / 100000
        if (lakhs >= 100) String.format("%.2fCr", lakhs / 100)
        else String.format("%.2fL", lakhs)
    } else {
        String.format("%.1fK", value / 1000)
    }
}


