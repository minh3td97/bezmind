package com.example.android.bezmind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.android.bezmind.choose_color.InforFragment
import com.example.android.bezmind.choose_color.InforViewModel
import com.example.android.bezmind.finish_game.FinishFragment
import com.example.android.bezmind.finish_game.FinishViewModel
import com.example.android.bezmind.main_game.MainGameFragment
import com.example.android.bezmind.main_game.MainGameViewModel
import com.example.android.bezmind.welcome.WelcomeFragment
import com.example.android.bezmind.welcome.WelcomeViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val disposeBag = CompositeDisposable()
    private lateinit var welcomeViewModel: WelcomeViewModel
    private lateinit var inforViewModel : InforViewModel
    private lateinit var gameViewModel : MainGameViewModel
    private lateinit var finishViewModel: FinishViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeViewModel = obtainViewModel(WelcomeViewModel::class.java)
        inforViewModel = obtainViewModel(InforViewModel::class.java)
        gameViewModel = obtainViewModel(MainGameViewModel::class.java)
        finishViewModel = obtainViewModel(FinishViewModel::class.java)

        bindOutputsFrom(welcomeViewModel)
        bindOutputsFrom(inforViewModel)
        bindOutputsFrom(gameViewModel)
        bindOutputsFrom(finishViewModel)

        Handler().postDelayed({
            showWelcome()
            //showInfo()
            //showGame()
            //showFinish()
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
    private fun showGame(colorPlayer1: Int, colorPlayer2: Int){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainGameFragment.newInstance().apply {
                this.colorPlayer1 = colorPlayer1
                this.colorPlayer2 = colorPlayer2
            }, "MainGameFragment")
            .commit()
    }
    private fun showFinish(colorPlayer1: Int, colorPlayer2: Int, score: Int){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, FinishFragment.newInstance().apply {
                this.colorPlayer1 = colorPlayer1
                this.colorPlayer2 = colorPlayer2
                this.score = score
            }, "FinishFragment")
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
                        Timber.d("goToChoose color")
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
                        showGame(inforViewModel.colorPlayer1, inforViewModel.colorPlayer2)
                    }
            )
        }
    }
    private fun bindOutputsFrom(outputs: MainGameViewModel) {
        val context = this
        with(outputs) {
            disposeBag.add(
                goToEndGame
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe {
                        Timber.d("goToFinish")
                        showFinish(gameViewModel.colorPlayer1, gameViewModel.colorPlayer2, gameViewModel.scoreWinner)
                    }
            )
        }
    }
    private fun bindOutputsFrom(outputs: FinishViewModel) {
        val context = this
        with(outputs) {
            disposeBag.add(
                goBackInfor
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe {
                        Timber.d("goToInfo")
                        showInfo()
                    }
            )
        }
    }

}
