package com.example.coroutinepractice.mistakes

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory

class Mistake5Activity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val viewModel : Mistake5ViewModel = ViewModelProvider(this)[Mistake5ViewModel::class.java]
        val button = findViewById<Button>(androidx.core.R.id.dialog_button)

        // lifecycleScope는 액티비티가 destroy될 때 취소되므로 누수가 발생하지 않음
        // 이 점이 문제인데 lifecycleScope 액티비티가  destroy될 때 같이 destroy되므로
        // 디바이스를 돌린다거나 다크 모드로 변경하는 등 구성 변경이 일어난다거나
        // 버튼을 클릭하면 ui에 데이터를 업데이트 혹은 네트워크 call을 할때 구성변경이 일어나면
        /*button.setOnClickListener {
            lifecycleScope.launch {
                viewModel.postValueApi()
            }
        }*/
    }

}