package meehan.matthew.basicapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import meehan.matthew.basicapp.repository.CryptoRepository
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: CryptoRepository
): ViewModel() {

    val state: MutableStateFlow<CryptoViewModelState> = MutableStateFlow(CryptoViewModelState.Loading)

    init {
        viewModelScope.launch {
            repository.getStoredEthereumPrice().collect { storedPrice ->
                when (!storedPrice.isNullOrEmpty()) {
                    true -> state.value = CryptoViewModelState.Loaded(
                        data = storedPrice
                    )
                    false -> {}
                }
            }
        }
    }
}