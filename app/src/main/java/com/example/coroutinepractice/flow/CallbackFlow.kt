package com.example.coroutinepractice.flow

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun justFlow(): Unit = runBlocking {
    flow {
        for (i in 1..5) emit(i)
    }.collect { println(it) }
}

fun justChannelFlow(): Unit = runBlocking {
    channelFlow {
        for (i in 1..5) send(i)
    }.collect { println(it) }
}


//위의 두 가지 예시는 람다에서 내보낸 1~5까지의 데이터만 처리하고 끝난다.
// callbackFLow 람다 외부에서 데이터를 보낼 수 있다
fun channelFlowWithAwaitClose(): Unit = runBlocking {
    channelFlow {
        for (i in 1..5) send(i)
        awaitClose()
        // awaitClose() 함수는 channelFlow의 선택사항이다. 함수가 종료되지 않고 계속 대기한다.
        // channelFlow를 통해 전송되는 추가 데이터에 대한 흐름을 유지하고 싶을 때 사용한다.
    }.collect { println(it) }
}


fun callbackFlowWithSend(): Unit = runBlocking {

    var sendData: suspend (data: Int) -> Unit = { }
    var closeChannel: () -> Unit = {}

    launch {// launch로 새로운 코루틴을 만들어 주지 않으면 callbackFlow함수가 close되기 전까지 기다리며 전체함수가 계속되는 것을 blocking 하므로 callbackFlow 함수 이후의 코드는 동작하지 않는다.
        callbackFlow {
            for (i in 1..5) send(i)
            sendData = { data -> send(data) }
            closeChannel = { close() }
            awaitClose()
        }.collect { println(it) }
    }
    delay(10)
    println("Sending 6")
    sendData(6)
    closeChannel()
    delay(10)
    sendData(7) // 에러 발생
// 채널 닫고 -> delay(10) -> send(7) 함수는 10만큼 일시중단 -> send는 일시중단 된 후 채널이 닫혀도 일시 중단된 호출이 중단되지 않으므로 데이터를 보내서 에러 발생
    /*Closing a channel after this function has suspended does not cause this suspended send invocation to abort,
    because closing a channel is conceptually like sending a special "close token" over this channel.*/
}

fun callbackFlowWithTrySend(): Unit = runBlocking {

    var sendData: (data: Int) -> Unit = { }
    var closeChannel: () -> Unit = {}

    launch {// launch로 새로운 코루틴을 만들어 주지 않으면 callbackFlow함수가 close되기 전까지 기다리며 전체함수가 계속되는 것을 blocking 하므로 callbackFlow 함수 이후의 코드는 동작하지 않는다.
        callbackFlow {
            for (i in 1..5) send(i)
            sendData = { data -> trySend(data).onFailure { println(it) } }
            closeChannel = { close() }
            awaitClose()
        }.collect { println(it) }
    }
    delay(10)
    println("Sending 6")
    sendData(6)
    closeChannel()
    delay(10)
    sendData(7)
// 채널 닫고 -> delay(10), trySend는 일시정지 하지 않으므로 delay와 상관없이 7보냄. -> 이미 채널은 닫혀있어서 성공하지 못함 -> trySend 호출이 성공하지 못한 결과를 반환하면 요소가 소비자에게 전달되지 않았음을 보장하므로 에러가 발생하지 않음.
}

// delay(10) -> send(1) -> 버퍼 크기 없으므로 방출 -> delay(100) -> println(1) -> 앞의 과정 10까지 반복
fun send() = runBlocking {
    channelFlow {
        for (i in 1..10) {
            delay(10)
            send(i) // suspending
            println("i $i")
        }
    }.buffer(0).collect {
        delay(100)
        println(it)
    }
}

// delay(10) -> trySend(1) -> 버퍼 크기 없으므로 방출 -> delay(100) -> delay 기다리지 않고 trySend(2...7) The other result is not emitted because it was blocked (due to the delay(100)).
// delay(100) 끝나서 println(1) -> 같은 시점에 trySend(8) -> 방출 -> delay(100) -> delay 기다리지 않고 trySend(9..10) 방출 x
// delay(100) 끝나서 println(8)
// 결론 방출 성공 1, 8뿐 나머지는 방출 실패(When there’s no buffer to emit, the trySend will result in a failure, and proceed to the next emit.)
fun trySend() = runBlocking {
    channelFlow {
        for (i in 1..10) {
            delay(10)
            trySend(i).onSuccess { println(it) }
            println("i $i")
        }
    }.buffer(0).collect {
        delay(100)
        println(it)
    }
}
// Adds element to this channel, blocking the caller while this channel is full
// This will block the emission until collect is completed before the next emission
fun trySendBlocking() = runBlocking {
    channelFlow {
        for (i in 1..10) {
            delay(10)
            trySendBlocking(i).onSuccess { println(it) }
            println("i $i")
        }
    }.buffer(0).collect {
        delay(100)
        println(it)
    }
}

fun main() {
//    callbackFlow()
//    send()
//    trySend()
    trySendBlocking()
//    callbackFlowWithSend()
//    callbackFlowWithTrySend()
}
// send(): suspending, suspend 키워드 붙음,
// trySend() : non-suspending, trySend 호출이 성공하지 못한 결과를 반환하면 요소가 소비자에게 전달되지 않았음을 보장한다.
// trySendBlocking(): runBlocking과 catch를 사용하여 차단 코드 내에서 안전한 방식으로 Channel.send 메서드를 호출하는 방법