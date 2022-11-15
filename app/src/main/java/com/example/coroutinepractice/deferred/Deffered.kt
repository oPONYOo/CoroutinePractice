package com.example.coroutinepractice.deferred

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


suspend fun returnFive(): Int {
    delay(1000L) // 일시 중단 함수
    return 5
}

suspend fun returnTen(): Int {
    delay(1000L)
    return 10
}


fun main() = runBlocking {

    val time = measureTimeMillis {
        val one = async { returnFive() }
        val two = async { returnTen() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")

}





