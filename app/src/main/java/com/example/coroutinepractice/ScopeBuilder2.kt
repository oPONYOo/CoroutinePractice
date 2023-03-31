package com.example.coroutinepractice

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
// 코루틴 내에서 sleep
// sleep을 했기 때문에 다른 코루틴에게 기회를 주지 않고
// 자기 자신이 500을 바쁘게 쉬어간다. 그래서 runBlocking에서 쉬었지만
// 여전히 코드의 우선권은 runBlocking이 가지고 있고 쉰 다음에도 Hello를 출력하는게 먼저
// sleep과 delay의 차이점
// delay는 자기가 쉬는 동안 누군가 수행할 수 있게 양보하지만
// sleep은 양보하지 않고 자기가 가지고 있음
/*
fun main() = runBlocking {
    launch {
        println("launch: ${Thread.currentThread().name}")
        println("World!")
    }
    println("runBlocking: ${Thread.currentThread().name}")
//    Thread.sleep(500) // 양보 안함. 운영체제에게 잠시 쉬겠다고 알리는 것 뿐. 다른 코드에게 스레드를 넘기지 않
    delay(500L) // 양보 현재 스레드를 다른 코드가 수행할 수 있도록 넘겨주는 형식
    println("Hello")
}*/

// 예제7: 한번에 여러 launch
// 코루틴이 단일 스레드를 사용하는 경우에도 서로 양보하며 수행하기 때문에 매우 유용
fun main() {
    runBlocking {// 계층적, 구조
        launch {
            println("launch1: ${Thread.currentThread().name}") // 출력
            delay(1000L) // suspension point 1초동안 잠듬
            // runblocking 코드 수행 끝난 후 출력
            println("3!")
        }
        launch {
            println("launch2: ${Thread.currentThread().name}") // 출력
            println("1!")
        }
        println("runBlocking: ${Thread.currentThread().name}")
        // suspension point
        delay(500L) // delay(500)이 호출되면 runBlocing은 다른 코루틴(launch1)에게 양보하고 자기는 멈춤
        // lauch2 블럭 실행된 후
        println("2!")

    }
    println("4!")
}
// 예제8: 상위 코루틴은 하위 코루틴을 끝까지 책임진다.
// runBlocking 안에 두 lauch가 속해 있는데 계층화되어 있어서 구조적
// runBlocing은 그 속에 포함된 launch 자식들이 다 끝나기 전까지 종료되지 않음.
// 부모 runBlocking을 cancel하면 자식 lauch들도 같이 cancel
// 관리하기 용이