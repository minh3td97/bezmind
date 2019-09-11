package com.example.android.bezmind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.android.bezmind.choose_color.InforFragment
import com.example.android.bezmind.welcome.WelcomeFragment
import com.example.android.bezmind.welcome.WelcomeViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val disposeBag = CompositeDisposable()
    private lateinit var welcomeViewModel: WelcomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeViewModel = obtainViewModel(WelcomeViewModel::class.java)

        bindOutputsFrom(welcomeViewModel)

        Handler().postDelayed({
            //showWelcome()
            showInfo()
        }, 2000)
    }
    private fun showWelcome(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, WelcomeFragment.newInstance(), "WelcomeFragment")
            .commit()
    }
    private fun showInfo(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, InforFragment.newInstance(), "InforFragment")
            .commit()
    }
    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }
    private fun bindOutputsFrom(outputs: WelcomeViewModel) {
        val context = this
        with(outputs) {
            disposeBag.add(
                goToMainProcessor
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe {
                        Timber.d("goToMain")
                        showInfo()
                    }
            )
        }
    }
}
