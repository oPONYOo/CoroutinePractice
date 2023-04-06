package com.example.coroutinepractice

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// 잘못된 코드
// launch는 반드시 코루틴 내부에서 호출해야한다.
private suspend fun doOneTwoThree(): Unit { // suspension point
    /*launch { // 코드블럭 내에서 this는 코루틴 Receiver 수신객체
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L) // suspension point
        println("3!")
    }
    launch {
        println("launch1: ${Thread.currentThread().name}")
        println("1!")
    }
    launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(500L)
        println("2!")
    }
    println("4!")*/
}

fun main(): Unit  = runBlocking<Unit> {// this는 코루틴
    doOneTwoThree() // suspension point
    println("runBlocking: ${Thread.currentThread().name}")
    delay(100L)
    println("5!")

}