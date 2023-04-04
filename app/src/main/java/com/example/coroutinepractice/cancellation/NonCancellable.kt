package com.example.coroutinepractice.cancellation

import kotlinx.coroutines.*

// 취소 불가능한 블록
// 어떤 코드는 취소가 불가능 해야한다.
// withContext(NonCancellable)을 이용하면 취소 불가능한 블럭을 만들 수 있다.
// 예를들어 어떤 자원은 무조건 해제해야하는 경우가 있으면 취소 불가능한 코드를 만들어 사용할 수 있다.
// 취소 불가능한 코드를 finally절에 사용할 수도 있다.
// finallay 수행 중에도 cancel이 일어날 수 있다. 이 경우에 무조건 수행되어야 한다면 withContext(NonCancellable)를 사용하여
// 취소 불가능한 블럭을 만들어서 쓸 수 있다.
private suspend fun doOneTwoThree() = coroutineScope {
    val job1 = launch {
        withContext(NonCancellable) {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        }
        delay(1000L)
        print("job1: end")
    }

    val job2 = launch {
        withContext(NonCancellable) {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        }
        delay(1000L)
        print("job2: end")
    }

    val job3 = launch {
        withContext(NonCancellable) {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("2!")
        }
        delay(1000L)
        print("job3: end")
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