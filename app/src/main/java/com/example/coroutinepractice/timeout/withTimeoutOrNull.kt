package com.example.coroutinepractice.timeout

import kotlinx.coroutines.*


private suspend fun doCount() = coroutineScope {
    val job1 = launch(Dispatchers.IO) {
        var i = 1
        var nextTime = System.currentTimeMillis() + 100L

        while (i <= 10 && isActive) {
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextTime) {
                println(i)
                nextTime = currentTime + 100L
                i++
            }
        }
    }
}

// 성공할 경우 withTimeoutOrNull의 마지막에서 true를 리턴하게 하고 실패했을 경우
// null을 반환할테니 엘비스 연산자를 이용해 false를 리턴하게 했다.
// 엘비스 연산자는 null 값인 경우 다른 값으로 치환한다.
fun main() = runBlocking {
    val result = withTimeoutOrNull(500L) {// 500 이상의 시간이 걸리면 작업이 취소된다.
        doCount()
        true
    } ?: false // 엘비스 연산자로 처리
    println(result)
}
