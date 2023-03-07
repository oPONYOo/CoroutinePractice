package com.example.coroutinepractice.mistakes

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

class Mistake5Activity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val viewModel : Mistake5ViewModel = ViewModelProvider(this)[Mistake5ViewModel::class.java]
        val button = findViewById<Button>(androidx.core.R.id.dialog_button)

        // lifecycleScope는 액티비티가 destroy될 때 취소되므로 누수가 발생하지 않음
        // 이 점이 문제인데 lifecycleScope 액티비티가  destroy될 때 같이 destroy되므로
        // 디바이스를 돌린다거나 다크 모드로 변경하는 등
        // 버튼을 클릭하면 ui에 데이터를 업데이트 혹은 네트워크 call을 하던 도중에 구성변경이 발생하면 취소되길 원치 않았지만 취소된다.
        // 이러한 이유로 코루틴 scope를 viewmodel에서 생성해야한다.
        // 액티비티 혹은 프래그먼트의 생명주기보다 오래 살아있는 viewModel scope가 있는 이유이기도 하다.
        /*button.setOnClickListener {
            lifecycleScope.launch {
                viewModel.postValueApi()
            }
        }*/

        // 버튼을 클릭해서 네트워크 코드를 초기화해도 이제 액티비티가 아닌 뷰모델의 라이프사이클에 종속되기 때문에
        // 구성 변경이 발생하면 뷰모델은 아직 살아있기 때문에 네트워크 call은 계속 실행된다.
        button.setOnClickListener {
            viewModel.postValueApi()
        }
    }

}