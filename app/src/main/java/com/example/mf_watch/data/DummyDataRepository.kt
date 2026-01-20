package com.example.mf_watch.data

object DummyDataRepository {
    fun getPortfolioData(): PortfolioModel {
        return PortfolioModel(
            totalValue = 319000.0,
            totalInvested = 255000.0,
            returnPercentage = 24.94,
            fundCount = 6,
            funds = listOf(
                FundModel(
                    name = "Axis Bluechip",
                    currentValue = 62500.0,
                    returnPercentage = 25.00,
                    icon = "📊"
                ),
                FundModel(
                    name = "Parag Parikh",
                    currentValue = 96000.0,
                    returnPercentage = 28.00,
                    icon = "📈"
                ),
                FundModel(
                    name = "Mirae Emerging",
                    currentValue = 51200.0,
                    returnPercentage = 28.00,
                    icon = "🚀"
                ),
                FundModel(
                    name = "ICICI Prudential",
                    currentValue = 45000.0,
                    returnPercentage = 22.50,
                    icon = "💼"
                ),
                FundModel(
                    name = "SBI Bluechip",
                    currentValue = 38000.0,
                    returnPercentage = 20.00,
                    icon = "🏦"
                ),
                FundModel(
                    name = "Kotak Emerging",
                    currentValue = 26300.0,
                    returnPercentage = 18.50,
                    icon = "💰"
                )
            )
        )
    }
}
