package meehan.matthew.basicapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import meehan.matthew.basicapp.R
import meehan.matthew.basicapp.repository.CryptoRepository
import meehan.matthew.basicapp.work.WidgetWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CryptoWidget : GlanceAppWidget() {

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        CryptoWidgetContent(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(8.dp)
                .background(
                    color = R.color.white
                )
        )
    }
}

@AndroidEntryPoint
class CryptoWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var repository: CryptoRepository

    override val glanceAppWidget: GlanceAppWidget = CryptoWidget()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let { nonNullContext ->
            startWork(nonNullContext)
        }
    }

    private fun startWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val worker = PeriodicWorkRequestBuilder<WidgetWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("widgetWorker")
            .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniquePeriodicWork(
                "widgetWork",
                ExistingPeriodicWorkPolicy.KEEP,
                worker
            )
    }
}