package com.onespan.android.interview.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onespan.android.interview.ConnectivityChecker
import com.onespan.android.interview.model.CatBreedModel
import com.onespan.android.interview.repository.CatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatBreedViewModel(
    private val repository: CatRepository = CatRepository(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val connectivityChecker: ConnectivityChecker

) : ViewModel() {
    private val _breeds = MutableStateFlow<List<CatBreedModel>>(emptyList())
    val breeds: StateFlow<List<CatBreedModel>> = _breeds

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private fun isInternetAvailable(): Boolean {
        return connectivityChecker.isInternetAvailable() // Use the interface
    }
    init {
        fetchBreeds()
    }

    fun fetchBreeds() {
        viewModelScope.launch(dispatcher) {
            _isLoading.value = true
            _errorMessage.value = null
            if (isInternetAvailable()) {
                try {
                    val result = repository.fetchBreeds()
                    result.onSuccess { breeds ->
                        _breeds.value = breeds
                    }.onFailure { exception ->
                        _errorMessage.value = exception.localizedMessage ?: "An error occurred"
                    }
                } catch (e: Exception) {
                    _errorMessage.value = e.message
                }
            } else {
                _errorMessage.value = "No internet connection"

            }
            _isLoading.value = false

        }}

}