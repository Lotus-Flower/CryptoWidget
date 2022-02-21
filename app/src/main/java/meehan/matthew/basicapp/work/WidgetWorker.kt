package meehan.matthew.basicapp.work

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import meehan.matthew.basicapp.repository.CryptoRepository
import meehan.matthew.basicapp.storage.ETHEREUM_KEY
import meehan.matthew.basicapp.widget.CryptoWidget

@HiltWorker
class WidgetWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: CryptoRepository
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val result = repository.getEthereumPrice()

        return when (result.isSuccessful) {
            true -> {
                GlanceAppWidgetManager(applicationContext).getGlanceIds(CryptoWidget::class.java).firstOrNull()?.let { id ->
                    updateAppWidgetState(
                        context = applicationContext,
                        definition = PreferencesGlanceStateDefinition,
                        id
                    ) {
                        it.toMutablePreferences().apply {
                            this[stringPreferencesKey(ETHEREUM_KEY)] = result.body()?.last.orEmpty()
                        }
                    }
                    CryptoWidget().update(applicationContext, id)
                }
                Result.success()
            }
            else -> Result.failure()
        }
    }
}