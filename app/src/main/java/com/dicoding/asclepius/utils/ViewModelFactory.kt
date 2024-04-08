package com.dicoding.asclepius.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.repo.CancerRepository
import com.dicoding.asclepius.view.MainViewModel

class ViewModelFactory(
    val cancerRepository: CancerRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(cancerRepository) as T

    }

}