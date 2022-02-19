package meehan.matthew.network

import retrofit2.Response
import retrofit2.http.GET

interface CryptoApiService {
    @GET("api/v2/ticker/ethusd/")
    suspend fun getEthereumPriceList(): Response<EthereumResponse>
}