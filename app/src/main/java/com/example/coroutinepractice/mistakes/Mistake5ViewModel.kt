package com.example.coroutinepractice.mistakes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class Mistake5ViewModel: ViewModel() {

    suspend fun postValueApi() {
        delay(10000L)
    }
}