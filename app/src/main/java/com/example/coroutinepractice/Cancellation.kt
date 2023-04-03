package com.example.coroutinepractice

import kotlinx.coroutines.*

private suspend fun doOneTwoThree() = coroutineScope {
    val job1 = launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L)
        println("3!")
    }
    val job2 = launch {
        println("launch1: ${Thread.currentThread().name}")
        println("1!")
    }
    val job3 = launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(500L)
        println("2!")
    }
    delay(800L)
    job1.cancel()
    job2.cancel()
    job3.cancel()
    println("4!")
}

// 취소 불가능한 job
suspend fun doCount() = coroutineScope {
    val job1 = launch(Dispatchers.Default) {
        var i = 1
        var nextTime = System.currentTimeMillis() + 100L

        // cancel 가능한 코드를 만드는 것의 핵심
        while (i <= 10 && isActive) { // 코루틴이 활성화되어 있는 경우만 루프가 돌기 때문에 cancel이 가능한 코루틴
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextTime) {
                println(i)
                nextTime = currentTime + 100L
                i++
            }
        }
    }
    delay(200L)
    /*job1.cancel()
    job1.join() // cancel이 끝날때까지 기다린다.*/
    // 하지만 실제로는 이 코드(isActive가 없으면)는 cancel되지 않고
    // 작업이 끝나고 doCount Done!가 출력되는 것을 확인하기 위함
    job1.cancelAndJoin() // 위의 코드와 결과는 동일 하지만 간결
    println("doCount Done!") // join을 해주지 않으면 실제 작업이 끝나지 않았는데 출력됨.
}

fun main() = runBlocking {
    /*doOneTwoThree()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5!")*/
    doCount()
}