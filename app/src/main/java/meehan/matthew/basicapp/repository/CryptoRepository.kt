package meehan.matthew.basicapp.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import meehan.matthew.basicapp.storage.ETHEREUM_KEY
import meehan.matthew.basicapp.dependencyInjection.IoDispatcher
import meehan.matthew.basicapp.storage.DataStoreHelper
import meehan.matthew.network.CryptoApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoRepository @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val cryptoApiService: CryptoApiService,
    private val dataStoreHelper: DataStoreHelper
) {
    suspend fun getEthereumPrice() = withContext(dispatcher) { cryptoApiService.getEthereumPriceList() }

    suspend fun storeEthereumPrice(price: String) = dataStoreHelper.putString(ETHEREUM_KEY, price)

    fun getStoredEthereumPrice() = dataStoreHelper.getString(ETHEREUM_KEY)
}