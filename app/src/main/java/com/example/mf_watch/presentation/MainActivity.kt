/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.mf_watch.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.mf_watch.data.DummyDataRepository
import com.example.mf_watch.data.FundModel
import com.example.mf_watch.data.PortfolioModel
import com.example.mf_watch.presentation.screens.*
import com.example.mf_watch.presentation.theme.Mf_watchTheme
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import org.json.JSONObject

class MainActivity : ComponentActivity(), DataClient.OnDataChangedListener, CapabilityClient.OnCapabilityChangedListener {
    
    private var portfolioState = mutableStateOf<PortfolioModel?>(null)
    private var layoutState = mutableStateOf(WatchLayout.ROUND)
    private var showSyncNotify = mutableStateOf(false)
    private var syncMessage = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        // Initialize with null to show connection message initially
        // portfolioState.value = DummyDataRepository.getPortfolioData()

        setContent {
            WearApp(
                portfolio = portfolioState.value,
                layout = layoutState.value,
                showSyncNotify = showSyncNotify.value,
                syncMessage = syncMessage.value,
                onNotifyDismissed = { showSyncNotify.value = false }
            )
        }
        
        // Fetch latest data and check connection status
        fetchLatestData()
        checkPhoneConnection()
    }

    private fun fetchLatestData() {
        val dataClient = Wearable.getDataClient(this)
        dataClient.dataItems.addOnSuccessListener { dataItems ->
            dataItems.forEach { item ->
                when (item.uri.path) {
                    "/portfolio_data" -> {
                        val dataMap = DataMapItem.fromDataItem(item).dataMap
                        val jsonStr = dataMap.getString("json_data") ?: ""
                        if (jsonStr.isNotEmpty()) {
                            try {
                                portfolioState.value = parsePortfolioJson(jsonStr)
                            } catch (e: Exception) {
                                Log.e("WatchSync", "Error parsing initial portfolio JSON", e)
                            }
                        }
                    }
                    "/change_layout" -> {
                        val dataMap = DataMapItem.fromDataItem(item).dataMap
                        val jsonStr = dataMap.getString("json_data") ?: ""
                        layoutState.value = if (jsonStr == "round") WatchLayout.ROUND else WatchLayout.SQUARE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
        Wearable.getCapabilityClient(this).addListener(this, "phone_app")
    }

    override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
        Wearable.getCapabilityClient(this).removeListener(this)
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        val isPhoneAvailable = capabilityInfo.nodes.isNotEmpty()
        Log.d("WatchSync", "Capability changed. Phone available: $isPhoneAvailable")
        
        if (!isPhoneAvailable) {
            // Immediately clear data when phone app is no longer reachable
            portfolioState.value = null
        } else {
            // Only fetch if we don't have data yet
            if (portfolioState.value == null) {
                fetchLatestData()
            }
        }
    }

    private fun checkPhoneConnection() {
        val capabilityClient = Wearable.getCapabilityClient(this)
        capabilityClient.getCapability("phone_app", CapabilityClient.FILTER_REACHABLE)
            .addOnSuccessListener { capabilityInfo ->
                val isPhoneAvailable = capabilityInfo.nodes.isNotEmpty()
                Log.d("WatchSync", "Initial connection check: $isPhoneAvailable")
                
                if (!isPhoneAvailable) {
                    portfolioState.value = null
                } else {
                    fetchLatestData()
                }
            }
            .addOnFailureListener {
                portfolioState.value = null
            }
    }

    override fun onDataChanged(dataEvents: com.google.android.gms.wearable.DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path ?: return@forEach
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val jsonStr = dataMap.getString("json_data") ?: ""

                Log.d("WatchSync", "Path Received: $path")

                when (path) {
                    "/portfolio_data" -> {
                        try {
                            if (jsonStr.isNotEmpty()) {
                                val portfolio = parsePortfolioJson(jsonStr)
                                portfolioState.value = portfolio
                                syncMessage.value = "Portfolio Updated"
                                showSyncNotify.value = true
                            }
                        } catch (e: Exception) {
                            Log.e("WatchSync", "Error parsing portfolio JSON", e)
                        }
                    }
                    "/change_layout" -> {
                        layoutState.value = if (jsonStr == "round") WatchLayout.ROUND else WatchLayout.SQUARE
                        syncMessage.value = "Layout Changed"
                        showSyncNotify.value = true
                    }
                    "/disconnect" -> {
                        Log.d("WatchSync", "Disconnecting... Clearing data")
                        portfolioState.value = null
                        syncMessage.value = "Disconnected"
                        showSyncNotify.value = true
                    }
                }
            }
        }
    }

    private fun parsePortfolioJson(jsonStr: String): PortfolioModel {
        val json = JSONObject(jsonStr)
        val fundsArray = json.getJSONArray("funds")
        val funds = mutableListOf<FundModel>()
        
        for (i in 0 until fundsArray.length()) {
            val f = fundsArray.getJSONObject(i)
            funds.add(
                FundModel(
                    name = f.getString("name"),
                    currentValue = f.getDouble("currentValue"),
                    returnPercentage = f.getDouble("returnPercentage"),
                    icon = f.optString("icon", "💰")
                )
            )
        }

        return PortfolioModel(
            totalValue = json.getDouble("totalValue"),
            totalInvested = json.getDouble("totalInvested"),
            returnPercentage = json.getDouble("returnPercentage"),
            fundCount = json.getInt("fundCount"),
            funds = funds
        )
    }
}

enum class WatchLayout {
    ROUND, SQUARE
}

@Composable
fun WearApp(
    portfolio: PortfolioModel?,
    layout: WatchLayout,
    showSyncNotify: Boolean,
    syncMessage: String,
    onNotifyDismissed: () -> Unit
) {
    Mf_watchTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (portfolio != null) {
                SwipeablePortfolioScreen(
                    portfolio = portfolio,
                    layout = layout
                )
            } else {
                // Not Connected / No Data State
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Connect phone to view Portfolio",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            SyncNotification(
                show = showSyncNotify,
                message = syncMessage,
                onFinished = onNotifyDismissed
            )
        }
    }
}

@Composable
fun SyncNotification(
    show: Boolean,
    message: String,
    onFinished: () -> Unit
) {
    LaunchedEffect(show) {
        if (show) {
            kotlinx.coroutines.delay(2200)
            onFinished()
        }
    }

    AnimatedVisibility(
        visible = show,
        enter = fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.7f),
        exit = fadeOut(animationSpec = tween(800)) + scaleOut(targetScale = 0.7f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp) // Slightly smaller for loader focus
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF121212).copy(alpha = 0.95f))
                    .padding(1.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(1.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Modern Circular Loader
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        indicatorColor = Color(0xFF3B82F6),
                        trackColor = Color(0xFF3B82F6).copy(alpha = 0.1f),
                        strokeWidth = 5.dp
                    )
                    
                    // // Sync Icon in center
                    // Icon(
                    //     imageVector = Icons.Default.Sync,
                    //     contentDescription = null,
                    //     tint = Color(0xFF60A5FA),
                    //     modifier = Modifier.size(24.dp)
                    // )
                }
            }
        }
    }
}

@Composable
fun SwipeablePortfolioScreen(
    portfolio: PortfolioModel,
    layout: WatchLayout
) {
    var currentPage by remember { mutableStateOf(0) }
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX > 100) {
                            // Swipe right - go to previous page
                            currentPage = if (currentPage > 0) currentPage - 1 else 0
                        } else if (offsetX < -100) {
                            // Swipe left - go to next page
                            currentPage = if (currentPage < 1) currentPage + 1 else 1
                        }
                        offsetX = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX += dragAmount
                    }
                )
            }
    ) {
        when (currentPage) {
            0 -> {
                if (layout == WatchLayout.ROUND) {
                    PortfolioOverviewRound(portfolio = portfolio)
                } else {
                    PortfolioOverviewSquare(portfolio = portfolio)
                }
            }
            1 -> {
                if (layout == WatchLayout.ROUND) {
                    FundsListRound(funds = portfolio.funds)
                } else {
                    FundsListSquare(funds = portfolio.funds)
                }
            }
        }
    }
}