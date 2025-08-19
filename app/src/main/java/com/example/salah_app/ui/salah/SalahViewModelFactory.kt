package com.example.salah_app.ui.salah

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salah_app.data.SalahRepository

class SalahViewModelFactory(
    private val application: Application,
    private val salahRepository: SalahRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SalahViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SalahViewModel(application, salahRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}