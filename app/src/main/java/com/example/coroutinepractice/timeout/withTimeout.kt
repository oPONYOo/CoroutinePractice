package com.example.coroutinepractice.timeout

import kotlinx.coroutines.*

// 일정 시간이 끝난 후에  종료하고 싶다면 withTimeout을 이용할 수 있다.
// 취소가 되면 TimeoutCancellationException 예외가 발생한다.
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

fun main() = runBlocking {
    withTimeout(500L) {// 500 이상의 시간이 걸리면 작업이 취소된다.
        doCount()
    }
}