package com.example.coroutinepractice.mistakes

import kotlinx.coroutines.*

// 문제점: 순차적으로 할 필요가 없는 다른 것들도 순차적으로 작동한다.
// 하나의 일시 중단 함수를 실행하기 위해 호출할 함수도 일시 중단으로 선언하면
// list에 10개의 이름이 담겨있다면 루프가 순차적으로 돌면서 첫 번째 이름에 대한 함수 호출이 일어나서 First name을 반환하면(call이 끝나면) 그 떄
// 두 번쨰.. ...마지막 10번째 이름의 First Name을 반환받으므로 총 10초가 걸리게 된다.
suspend fun getUserFirstNames(userIds: List<String>): List<String> {
    val firstNames = mutableListOf<String>()
    for (id in userIds) {
        firstNames.add(getFirstName(id))
    }
    return firstNames
}

suspend fun getFirstName(userId: String): String {
    delay(1000L)
    return "First name"
}

// 같은 시간에 호출하고 싶다면 async 사용하기
// async는 새로운 코루틴을 생성하고 결과를 반환해줌
suspend fun getUserFirstNames2(userIds: List<String>): List<String> {
    val firstNames = mutableListOf<Deferred<String>>()
    coroutineScope {
        for (id in userIds) {
            val firstName = async { //getUserFirstNames2 일시 중단 함수와 독립적인 코루틴이므로 일시 중단 영향을 받지 않는다.
                getFirstName(id)// 각각의 코루틴들이 독립적이여서 같은 시점에 작동하게된다.
            }
            firstNames.add(firstName)
        }
    }

    return firstNames.awaitAll()  // await 함수를 통해 완료되면 실제 결과 값을 얻을 수 있다.
    // 일반적으로 코루틴에서 실행해야하는 많은 일시 중단 함수를 호출해야할 때 async 블럭을 사용하면 서로 의존하지 않는다.
}




fun main() {
    runBlocking {
  //     println(getUserFirstNames(listOf("jina", "ponyo", "mimi")))
      println(getUserFirstNames2(listOf("jina", "ponyo", "mimi")))
    }
}