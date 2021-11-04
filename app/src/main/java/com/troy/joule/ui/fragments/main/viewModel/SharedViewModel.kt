package com.troy.joule.ui.fragments.main.viewModel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.troy.joule.repository.Repository
import com.troy.joule.repository.models.Driver
import com.troy.joule.repository.models.JouleLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SharedViewModel() : ViewModel() {

    val drivers: MutableStateFlow<List<Driver>> = MutableStateFlow(emptyList())
    val location: MutableLiveData<Location> = MutableLiveData()

    private lateinit var repository: Repository
    private var isInitialised = false

    fun init(repository: Repository) {
        this.repository = repository
        isInitialised = true
    }

    fun getDrivers() {
        if (isInitialised) {
            viewModelScope.launch {
                val response = repository.getDrivers()

                if (response.isSuccessful) {
                    drivers.value = response.body()!!
                }
            }
        }
    }

    fun updateLocation(uid: String, l: Location) {
        if (isInitialised) {
            val j = JouleLocation(l.latitude.toString(), l.longitude.toString())
            viewModelScope.launch {
                repository.updateLocation(uid, j)
            }
        }

    }

    fun getInvoices(uid: String) {
        if (isInitialised) {
            viewModelScope.launch {
                repository.loadInvoices(uid)
            }
        }
    }

}