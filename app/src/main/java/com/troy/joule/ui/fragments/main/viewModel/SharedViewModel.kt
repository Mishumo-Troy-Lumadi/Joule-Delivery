package com.troy.joule.ui.fragments.main.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.troy.joule.repository.Repository
import com.troy.joule.repository.models.Driver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SharedViewModel(): ViewModel() {

    val drivers: MutableLiveData<List<Driver>?> = MutableLiveData()
    private lateinit var repository: Repository
    private var isInitialised = false

    fun init(repository: Repository){
        this.repository = repository
        isInitialised = true
    }

     fun getDrivers(){
         if(isInitialised) {
             viewModelScope.launch {
                 val response = repository.getDrivers()

                 if (response.isSuccessful) {
                     drivers.value = response.body()
                 }
             }
         }
     }

}