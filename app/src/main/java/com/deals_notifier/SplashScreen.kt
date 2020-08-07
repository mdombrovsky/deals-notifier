package com.deals_notifier

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.deals_notifier.query.model.QueryHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        CoroutineScope(IO).launch {
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            intent.putExtra(
                QueryHolder::class.java.simpleName,
                QueryHolder.load(this@SplashScreen.applicationContext)
            )

            delay(1000)

            withContext(Main) {
                this@SplashScreen.startActivity(intent)
                this@SplashScreen.finish()
            }

        }
    }
}