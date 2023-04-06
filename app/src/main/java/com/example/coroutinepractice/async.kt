package com.example.coroutinepractice

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.system.measureTimeMillis

// async를 이용해 동시 수행하기
// async 키워드를 이용하면 동시에 다른 블럭을 수행할 수 있다.
// lauch와 비슷하게 보이지만 수행 결과를 await 키워드를 통해 받을 수 있다는 차이가 있다.
// 결과를 받아야한다면 async, 결과를 받지 않아도 된다면 launch를 선택할 수 있다.
// await 키워드를 만나면 async블럭이 수행이 끝났는지 확인하고 아직 끝나지 않았다면
// suspend되었다가 나중에 다시 깨어나고 반환값을 받아온다.

// 각각 1000을 소비하는데 한 번에 같이 소비해서 총 1000 내외로 코드가 수행된다.
// 두 개의 코드를 한 번에 수행시켰기 때문
// async와 await은 같이 쓰인다.
// async는 호출이 끝난 다음에 값을 가져오기 위해 넣는 것. getRandom1을 호출하기 위한 것은 아님
// await을 통해 값을 받아올 필요가 없다면 launch를 쓰면됨
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
        val value1 = async { // this: 코루틴
            getRandom1()
        } // getRandom1()도 별도의 코루틴으로 호출된다.
        val value2 = async { getRandom2() } // getRandom2()도 별도의 코루틴으로 호출된다.
        // await: job.join + 결과도 가져오는 역할
        // await을 호출하는 과정은 suspension point
        // 잠이 들었다가 값이 계산되면 깨어남. 어떤 작업이 순차적으로 수행되는지 알 필요가 없음
        println("${value2.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
    }
    println(elapsedTime)
}