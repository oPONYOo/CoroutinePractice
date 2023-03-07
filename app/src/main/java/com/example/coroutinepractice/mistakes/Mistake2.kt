package com.example.coroutinepractice.mistakes

import kotlinx.coroutines.*
import kotlin.random.Random

// 복잡한 task를 실행할 때 코루틴은 cancellation을 체크할 수 있는지 보장해야한다.
// 왜냐하면 job을 cancel했을 때 루프 안에서 계속 돌고 있을 경우 취소할 수 있는지 정보를 알 수 없기 떄문
suspend fun doSomething() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        var random = Random.nextInt(100_000)
        //Check this property in long-running computation loops to support cancellation
        while (random != 50000 && isActive) { // isActive는 코루틴이 취소되었을 때 false로 바뀌고 while문에서 빠져나갈 수 있게된다.
            random = Random.nextInt(100_000)
            // ensureActive() 코루틴이 active함을 보장, active하지 않으면 cancellationException발생
        }
    }
    delay(500L)
    job.cancel()
}

fun main() {
    runBlocking {
        doSomething()
    }
}