package com.example.coroutinepractice

import kotlinx.coroutines.*

// coroutineScope 역시 간략하게 expension body로 쓸 수 있음
// coroutineScope는 launch 1,2,3 끝날 때까지 기다림
// coroutineScope cancel 하면 자식들도 cancel됨
// suspend function 내에서 코루틴 스코프를 호출해서 코루틴 빌더를 suspend function내에서 쓸 수 있게 된 것
// coroutineScope는 오로지 코루틴 빌더를 호출하기 위해 존재함
// runBlocking은 현재 스레드를 멈추게 하고 기다리게 만드는 코루틴 빌더
// coroutineScope는 기다리게 하지 않고 다른 누군가가 일하려고 하면 일하게 만듬
// 현재 스레드를 멈추게 하지 않고 호출한 쪽이 suspend되고 시간이 되면 다시 활동하게 됨
// withContext와 runBlocking은 일이 끝날 때까지 붙잡음

// 코루틴 빌더 launch는 Job 객체를 반환하여 이를 통해 종료될 때까지 기다릴 수 있다.
private suspend fun doOneTwoThree() = coroutineScope { // this: 부모 코루틴
    val job = launch { // this: 자식 코루틴 Receiver 수신객체
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L) // suspension point
        println("3!")
    }
    job.join() // suspension point 첫 번째 launch 블럭이 끝날 때까지 기다리게. 끝나면 다음 다음 코드 호출
    // 1000L 동안 양보할 수 없음 3이 출력될 때까지 기다림
    launch {
        println("launch1: ${Thread.currentThread().name}")
        println("1!")
    }
//    repeat(1000) {
        launch {
            println("launch1: ${Thread.currentThread().name}")
            delay(500L)
            println("2!")
        }
//    }
    println("4!") // 두 번째로 출력
}
// 코루틴은 협력적으로 동작하기 때문에 여러 코루틴을 만드는 것에 큰 비용이 들지 않는다.
fun main(): Unit = runBlocking<Unit> {// this는 코루틴
    doOneTwoThree() // suspension point
    println("runBlocking: ${Thread.currentThread().name}")
    delay(100L)
    println("5!") // 자식들이 다 끝난 후에 출력

}