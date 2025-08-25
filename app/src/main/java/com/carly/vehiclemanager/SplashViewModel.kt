package com.carly.vehiclemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carly.vehicles.data.datasource.catalog.CatalogSeeder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val catalogSeeder: CatalogSeeder
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun initializeApp() {
        viewModelScope.launch {
            try {
                catalogSeeder.seedIfNeeded()
            } finally {
                _isLoading.value = false
            }
        }
    }
}