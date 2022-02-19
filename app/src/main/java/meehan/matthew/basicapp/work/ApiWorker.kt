package meehan.matthew.basicapp.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import meehan.matthew.basicapp.repository.CryptoRepository

@HiltWorker
class ApiWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: CryptoRepository
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val result = repository.getEthereumPrice()

        return when (result.isSuccessful) {
            true -> {
                repository.storeEthereumPrice(result.body()?.last.orEmpty())
                Result.success()
            }
            else -> Result.failure()
        }
    }
}