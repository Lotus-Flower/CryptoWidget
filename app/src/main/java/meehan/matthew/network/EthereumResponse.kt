package meehan.matthew.network

data class EthereumResponse(
    val ask: String?,
    val bid: String?,
    val high: String?,
    val last: String?,
    val low: String?,
    val `open`: String?,
    val timestamp: String?,
    val volume: String?,
    val vwap: String?
)