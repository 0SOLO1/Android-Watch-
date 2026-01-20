package com.example.mf_watch.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.mf_watch.data.FundModel

@Composable
fun FundsListRound(
    funds: List<FundModel>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fixed Header
            Text(
                text = "Your Funds",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 5.dp),
                textAlign = TextAlign.Center
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(funds) { fund ->
                    FundCard(fund = fund)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    // Row(
                    //     verticalAlignment = Alignment.CenterVertically,
                    //     horizontalArrangement = Arrangement.Center,
                    //     modifier = Modifier.fillMaxWidth()
                    // ) {
                    //     Canvas(modifier = Modifier.size(16.dp)) {
                    //         val w = size.width
                    //         val h = size.height
                    //         // Hand-drawing a simple swipe-back / refresh style icon
                    //         drawArc(
                    //             color = Color.White.copy(alpha = 0.4f),
                    //             startAngle = 60f,
                    //             sweepAngle = 240f,
                    //             useCenter = false,
                    //             style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                    //         )
                    //         // Arrow head
                    //         val path = androidx.compose.ui.graphics.Path().apply {
                    //             moveTo(w * 0.1f, h * 0.2f)
                    //             lineTo(w * 0.25f, h * 0.1f)
                    //             lineTo(w * 0.35f, h * 0.25f)
                    //         }
                    //         drawPath(
                    //             path = path,
                    //             color = Color.White.copy(alpha = 0.4f),
                    //             style = Stroke(width = 1.5.dp.toPx())
                    //         )
                    //     }
                    //     Spacer(modifier = Modifier.width(6.dp))
                    //     Text(
                    //         text = "Swipe back",
                    //         fontSize = 11.sp,
                    //         color = Color.White.copy(alpha = 0.4f),
                    //         textAlign = TextAlign.Center
                    //     )
                    // }
                    // Spacer(modifier = Modifier.height(30.dp))
                }
                
            }
        }

        // Indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            )
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF2196F3))
            )
        }
    }
}

@Composable
fun FundCard(fund: FundModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .background(
                color = Color(0xFF121212),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = fund.name,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "₹${formatCurrency(fund.currentValue)}",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFF30D158).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                val isProfit = fund.returnPercentage >= 0
                val color = if (isProfit) Color(0xFF30D158) else Color(0xFFFF453A)
                Text(
                    text = "${if(isProfit) "+" else ""}${String.format("%.2f", fund.returnPercentage)}%",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}


private fun formatCurrency(value: Double): String {
    return if (value >= 100000) {
        String.format("%.2fL", value / 100000)
    } else {
        String.format("%.1fK", value / 1000)
    }
}
