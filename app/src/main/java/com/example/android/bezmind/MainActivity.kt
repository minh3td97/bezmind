package com.example.android.bezmind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.android.bezmind.welcome.WelcomeFragment
import com.example.android.bezmind.welcome.WelcomeViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var welcomeViewModel: WelcomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeViewModel = obtainViewModel(WelcomeViewModel::class.java)
        Handler().postDelayed({
            showWelcome()

        }, 2000)
    }
    private fun showWelcome(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, WelcomeFragment.newInstance(), "WelcomeFragment")
            .commit()
    }
}
