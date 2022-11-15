package com.example.coroutinepractice.deferred

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

suspend fun returnFour(): Int {
    delay(1000L) // 일시 중단 함수
    return 4
}

suspend fun returnOne(): Int {
    delay(1000L)
    return 1
}


fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = returnFour()
        val two = returnOne()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")



}