package com.example.android.bezmind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.android.bezmind.choose_color.InforFragment
import com.example.android.bezmind.choose_color.InforViewModel
import com.example.android.bezmind.main_game.MainGameFragment
import com.example.android.bezmind.welcome.WelcomeFragment
import com.example.android.bezmind.welcome.WelcomeViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val disposeBag = CompositeDisposable()
    private lateinit var welcomeViewModel: WelcomeViewModel
    private lateinit var inforViewModel : InforViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeViewModel = obtainViewModel(WelcomeViewModel::class.java)
        inforViewModel = obtainViewModel(InforViewModel::class.java)

        bindOutputsFrom(welcomeViewModel)
        bindOutputsFrom(inforViewModel)

        Handler().postDelayed({
            //showWelcome()
            //showInfo()
            showGame()
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
    private fun showGame(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainGameFragment.newInstance(), "MainGameFragment")
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
                goToInforProcessor
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe {
                        Timber.d("goToMain")
                        showInfo()
                    }
            )
        }
    }

    private fun bindOutputsFrom(outputs: InforViewModel) {
        val context = this
        with(outputs) {
            disposeBag.add(
                goToMainGame
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe {
                        Timber.d("goToMain")
                        showGame()
                    }
            )
        }
    }

}
