package com.example.coroutinepractice.suspendingFunc

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.system.measureTimeMillis

// getRandom1 -> getRandom2 순차적으로 실행됨
// 한 번에 하나의 suspend 함수가 수행되기 때문
// 두 개의 별개의 코루틴을 만든다면
// suspend function을 한 번에 두 개씩 수행할 수 있다. -> async 사용하기
private suspend fun getRandom1(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

private suspend fun getRandom2(): Int {
    delay(1000L)
    return Random.nextInt(0, 500)
}

fun main() = runBlocking {
    val elapsedTime = measureTimeMillis {
        val value1 = getRandom1()
        val value2 = getRandom2()
        println("$value2 + $value2 = ${value1 + value2}")
    }
    println(elapsedTime)
}