package com.example.coroutinepractice

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// runBlocking: 코루틴을 만드는 함수로 코루틴 빌더라고 부른다.
// runBlocking은 코루틴을 만들고 코드 블록이 수행이 끝날 때까지
// runBlocking 다음의 코드를 수행하지 못하게 막는다.(그래서 blocking)
// 수신객체 == extension lambda
// 람다를 확장한 것 처럼 쓸 수 있다는 의미
// 마치 코드블럭이 코루틴을 확장한것 처럼 쓸 수 있는 개념, 그래서 현재 코드 블럭에서 코루틴에 있는 모든 기능을 사용 가능
fun main() = runBlocking {
    println(this) // 코루틴이 수신객체(Receiver)인 것을 알 수 있다.
    // BlockingCoroutine{Active}@2ac273d3
    // BlockingCoroutine은 CoroutineScope의 자식
    // 코틀린 코루틴을 쓰는 모든 곳에는 코루틴 스코프가 있다고 생각하기
    // 코루틴의 시작은 코루틴 스코프
    println(coroutineContext)
    // 코루틴 스코프는 코루틴을 제대로 처리하기 위한 정보, 코루틴 컨텍스트 프로퍼티를 가지고 있다.
    // 수신 객체의 coroutineContext를 호출할 수 있다.

    launch {
        // 새로운 코루틴을 만들기 때문에 새로운 코루틴 스코프를 만들게 되고,
        // launch는 할 수 있다면 다른 코루틴 코드를 같이 수행시키는 코루틴 빌더
        println("launch: ${Thread.currentThread().name}")
        delay(100L)
        println("World!")
        // launch 코루틴 빌더에 있는 내용이 runBlocking이 있는 메인 흐름보다 늦게 수행됨
        // 둘 다 메인 스레드를 사용하기 때문에 runBlocking의 코드들이 메인 스레드를 다 사용할 때 까지
        // launch의 코드 블럭이 기다리는 것
        // launch는 우리가 만든 코드를 큐에 넣어놓고 다음 순서를 기다리는 것
        // 한 번에 수행될 수 있는 코드가 여러 개인 디스패처를 사용하고 있다면
        // 한 번에 여러 코드를 수행할 수 있다.
    }
    println("runBlocking: ${Thread.currentThread().name}")
    delay(500L)
    // 해당 스레드를 해제하고 잠시 쉬는 형태. 그 스레드를 다른 코루틴이 쓸 수 있도록 양보
    // 500을 쉬는 동안 launch 코드 블럭이 실행되고 그 후 runBlocking이 수행
    // launch가 쓸 수 있도록 양보
    println("Hello")
    // runBlocking은 Hello를 출력하고 나서 종료하지 않고 launch 코드 블럭의 내용이 다 끝날 때 까지 기다린다.
    // launch는 기다리지 않는다.
}
// 코드 블럭 2개가 같은 스레드에서 실행되는 서로 양보 받으면서
// 누가 먼저 수행될지 결정되게 되는 것
// 코루틴의 장점: 하나의 스레드 밖에 없지만 양보 하면서 최선의 결과를 낼 수 있음
// delay가 호출될 때 마다 현재 코드 블럭이 잠들게 되는데
// 이를 suspension point라고 한다.
// 코루틴은 협조적으로 여러 코루틴이 같이 활용하는 형태
