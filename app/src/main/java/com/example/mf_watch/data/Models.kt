package com.example.mf_watch.data

data class FundModel(
    val name: String,
    val currentValue: Double,
    val returnPercentage: Double,
    val icon: String
)

data class PortfolioModel(
    val totalValue: Double,
    val totalInvested: Double,
    val returnPercentage: Double,
    val fundCount: Int,
    val funds: List<FundModel>
)
