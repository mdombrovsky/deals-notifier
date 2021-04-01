package com.deals_notifier

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.deals_notifier.deal.model.DealManager
import com.deals_notifier.deal.model.DealService
import com.deals_notifier.deal.model.ValidDealHolder
import com.deals_notifier.query.model.QueryHolder
import com.deals_notifier.scraper.model.ScraperHolder
import com.deals_notifier.settings.model.SettingsSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(IO).launch {
            val intent = Intent(this@SplashScreen, MainActivity::class.java)

            if(DealManager.instance == null) {
                DealManager.initialize(
                    ValidDealHolder(
                        QueryHolder.load(this@SplashScreen.applicationContext),
                        ScraperHolder.load(this@SplashScreen.applicationContext)
                    )
                )
            }


            if (!DealService.isRunning() && SettingsSingleton.instance.notificationsEnabled) {
                DealService.start(
                    this@SplashScreen.applicationContext
                )
            }

            withContext(Main) {
                this@SplashScreen.startActivity(intent)
                this@SplashScreen.finish()
            }

        }
    }
}