package meehan.matthew.basicapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import meehan.matthew.basicapp.R
import meehan.matthew.basicapp.databinding.FragmentCryptoBinding
import meehan.matthew.basicapp.work.ApiWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CryptoFragment : Fragment(R.layout.fragment_crypto) {

    private val viewModel: CryptoViewModel by viewModels()
    private var binding: FragmentCryptoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when (it) {
                    is CryptoViewModelState.Loaded -> binding?.ethereumPriceText?.text = getString(
                        R.string.ethereum_price_format_string,
                        it.data
                    )
                    else -> {}
                }
            }
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val worker = PeriodicWorkRequestBuilder<ApiWorker>(30, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .addTag("apiWorker")
            .build()

        WorkManager.getInstance(requireContext().applicationContext)
            .enqueueUniquePeriodicWork(
                "apiWork",
                ExistingPeriodicWorkPolicy.KEEP,
                worker
            )
    }
}