package com.example.coroutinepractice.mistakes

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay


// 문제점: 일시 중단 함수가 cancellation exception을 catch한다.
// cancellation이 코루틴과 어떻게 작동하는지
// 해결방법1. : 특정한 exception만 catch하도록 범위를 좁혀서 cancellation exception을 포함하지 않도록 하기
// (ex. Exception -> HttpException )
// 해결방법2. : cacellation exception일 경우 다시 던져서 부모에게 전파될 수 있도록 하기
suspend fun riskyTask() {
    // throw CancellationException() // 이때는 suspend 함수 내부에서 바로 throw하기 때문에 부모에게 전달된다.
    try {
        // throw CancellationException() // 해당 코드를 try catch 블럭에 작성할 경우 catch블럭에서 익셉션으로 들어오긴하나, 부모 scope에 전달되지 않는다.
        delay(3000L)
        println("The answer is ${10 / 0}")
    } catch (e: Exception) { // 모든 종류의 익셉션을 잡을 수 있으므로 일시중단 함수가 취손되는 경우 cancellation 익셉션도 불리게 된다.
        // 이는 cancellation이 코루틴의 부모 scope에게 전파되지 않음을 의미한다.
        if (e is CancellationException ) {
            throw e
        }
        println("Oops, that didn't work")
    }
}