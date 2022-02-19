package meehan.matthew.basicapp.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import meehan.matthew.basicapp.storage.ETHEREUM_KEY
import meehan.matthew.basicapp.R
import meehan.matthew.basicapp.repository.CryptoRepository
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

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        MainScope().launch {
            repository.getStoredEthereumPrice().collect { price ->
                val glanceId = GlanceAppWidgetManager(context).getGlanceIds(CryptoWidget::class.java).firstOrNull()
                glanceId?.let { id ->
                    updateAppWidgetState(
                        context = context,
                        definition = PreferencesGlanceStateDefinition,
                        id
                    ) {
                        it.toMutablePreferences().apply {
                            this[stringPreferencesKey(ETHEREUM_KEY)] = price.orEmpty()
                        }
                    }
                    glanceAppWidget.update(context, id)
                }
            }
        }
    }
}