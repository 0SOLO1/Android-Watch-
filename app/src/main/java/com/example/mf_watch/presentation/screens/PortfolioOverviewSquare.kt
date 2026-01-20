package com.example.mf_watch.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.mf_watch.data.PortfolioModel

@Composable
fun PortfolioOverviewSquare(
    portfolio: PortfolioModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Progress Bar (Linear for square)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFF2A2A2A), MaterialTheme.shapes.small)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(portfolio.returnPercentage.toFloat() / 100f)
                        .height(8.dp)
                        .background(Color(0xFF4ADE80), MaterialTheme.shapes.small)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E3A8A)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B82F6)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total Value
            Text(
                text = "₹${String.format("%.2f", portfolio.totalValue / 100000)}L",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Return Percentage
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF166534),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "📈 +${String.format("%.2f", portfolio.returnPercentage)}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4ADE80)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Funds and Invested in Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(
                            color = Color(0xFF1F1F1F),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "${portfolio.fundCount}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Funds",
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(
                            color = Color(0xFF1F1F1F),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "₹${String.format("%.2f", portfolio.totalInvested / 100000)}L",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Invested",
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Swipe hint
            Text(
                text = "🔄 Swipe for funds",
                fontSize = 11.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )
        }
    }
}
