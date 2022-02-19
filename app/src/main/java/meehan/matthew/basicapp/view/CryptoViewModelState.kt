package meehan.matthew.basicapp.view

sealed class CryptoViewModelState {
    object Loading : CryptoViewModelState()
    data class Loaded(val data: String): CryptoViewModelState()
    data class Error(val code: String, val message: String): CryptoViewModelState()
}
