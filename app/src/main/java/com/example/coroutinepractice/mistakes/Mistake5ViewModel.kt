package com.example.coroutinepractice.mistakes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class Mistake5ViewModel: ViewModel() {
   // 뷰모델보다 더 오래 살아있는 코루틴이 필요할 경우 일반적인 coroutineScope와 원하는 디스패처를 쓰면된다.
    val scope = CoroutineScope(Dispatchers.Main)
    // 이렇게 쓸 경우 더 이상 필요하지 않을 때 scope를 취소하는 것을 신경쓰면서 사용해야한다.

    /*suspend fun postValueApi() {
        delay(10000L)
    }*/

    // suspend 키워드 대신 간단하게 viewmodel lifecycle scope에서 launch하고
    // 내부에 네트워크 코드 등을 넣어주면 된다.
    // 이렇게 하면 suspend 함수가 아니므로 mainActivity에서 lifecycle scope를 사용할 필요가 없다.

    fun postValueApi() {
        /*viewModelScope.launch {
            delay(10000L)
        }*/
    }
}