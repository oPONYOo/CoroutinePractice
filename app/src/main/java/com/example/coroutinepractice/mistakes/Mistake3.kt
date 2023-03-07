package com.example.coroutinepractice.mistakes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.random.Random


// 문제점: 일사중단 network call은 main safe하지 않음
// 코루틴에서 main safe는 메인 스레드에서 코루틴과 일시 중단 함수를 실행하는 것을 의미
suspend fun doNetworkCall(): Result<String>  {
    val result = networkCall() // execute network call
    return if (result == "Success") { // return to result
        Result.success(result)
    } else Result.failure(Exception())
}

// 디스패처를 정해주지 않으면 원하지 않는 메인 디스패처에서 작동하게 된다.
// io 디스패처가 있으므로 withContext를 통해 컨텍스트를 반환함으로써
// io 디스패처로 바꾸는 것을 보장할 수 있다.
// 이렇게 하면 네트워크 함수에 대한 job은 withContext 외부에서 어떤 디스패처를 사용하든 상관이 없게된다.
// 이미 room, retrofit과 같은 라이브러들이 io 디스패처로 바꿀 필요 없도록 내부적으로 고려하고 있기 때문에
// 다른 네트워크 라이브러리 혹은 내가 만든 job을 사용할 때만 바꿔서 사용하면 된다.
suspend fun networkCall(): String {
    return withContext(Dispatchers.IO) {
        delay(3000L)
        if (Random.nextBoolean()) "Success" else "Error"
    }

}

fun main() {
    runBlocking {
        doSomething()
    }
}