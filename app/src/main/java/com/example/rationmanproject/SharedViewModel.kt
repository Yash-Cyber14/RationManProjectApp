package com.example.rationmanproject

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _userLocation = MutableStateFlow<userlocation?>(null)
    val userLocation: StateFlow<userlocation?> = _userLocation

    fun setUserLocation(loc: userlocation) {
        _userLocation.value = loc
    }
}