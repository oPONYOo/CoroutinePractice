package com.example.coroutinepractice.cancellation

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// finally
// launch에서 자원을 할당한 경우에는 어떻게 처리해야할까?
// suspend 함수들은 JobCancellationException을 발생시키기 때문에
// 표준 try catch finally로 대응할 수 있다.
// 취소 가능한 job에서 어떤 자원의 해제를 취소되었을 때 하기 위해서 finally 사용
private suspend fun doOneTwoThree() = coroutineScope {
    // 해당 예제에서는 exception을 구체적으로 처리하지 않을 것이기 때문에 catch안씀
    val job1 = launch {
        try {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        } finally {
            println("job1 is finishing!")
            // 파일을 닫아주는 코드
        }
    }

    val job2 = launch {
        try {
            println("launch2: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        } finally {
            println("job2 is finishing!")
            // 소켓을 닫아주는 코드
        }
    }

    val job3 = launch {
        try {
            println("launch3: ${Thread.currentThread().name}")
            delay(1000L)
            println("2!")
        } finally {
            println("job3 is finishing!")
        }
    }
    delay(800L)
    job1.cancel()
    job2.cancel()
    job3.cancel()
    println("4!")

}


fun main() = runBlocking {
    doOneTwoThree()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5!")

}