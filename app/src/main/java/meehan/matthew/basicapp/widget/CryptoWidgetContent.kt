package meehan.matthew.basicapp.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import meehan.matthew.basicapp.storage.ETHEREUM_KEY
import meehan.matthew.basicapp.R

@Composable
fun CryptoWidgetContent(
    modifier: GlanceModifier
) {
    val context = LocalContext.current
    val prefs = currentState<Preferences>()

    Row(
        modifier = modifier
    ) {
        Image(
            provider = ImageProvider(
                resId = R.drawable.ethereum_icon
            ),
            contentDescription = null,
            modifier = GlanceModifier.width(
                width = 48.dp
            )
        )
        Column(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .fillMaxHeight()
        ) {
            Text(
                text = context.getString(
                    R.string.ethereum
                ),
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = context.getString(
                    R.string.price_format,
                    prefs[stringPreferencesKey(ETHEREUM_KEY)]
                )
            )
        }
    }
}