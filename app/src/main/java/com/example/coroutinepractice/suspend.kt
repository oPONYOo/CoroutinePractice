package com.example.coroutinepractice

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// suspend 함수
// delay, lauch 등의 함수들을 포함한 코드들을
// 함수로 분리할 때는 함수의 앞에 suspend 키워드를 붙이면 된다.
// 딜레이 함수는 코루틴 혹은 suspend 함수 내에서만 쓸 수 있음
suspend fun doThree() {
    println("launch1: ${Thread.currentThread().name}")
    delay(1000L)
    println("3!")
}

suspend fun doOne() { // delay가 없기 때문에 suspend 필요없음 통일성을 위해 씀
    println("launch1: ${Thread.currentThread().name}")
    println("1!")
}

suspend fun doTwo() {
    println("launch1: ${Thread.currentThread().name}")
    delay(500L)
    println("2!")
}

// 컴파일러가 예상치 못할 경우에 runBlocking에 unit을 붙혀야함
fun main() = runBlocking<Unit> { //리턴 타입지정가능
    launch {
        doThree()
    }
    launch {
        doOne()
    }
    doTwo()
}