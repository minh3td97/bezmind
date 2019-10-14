package com.example.android.bezmind.main_game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import timber.log.Timber
import io.reactivex.processors.PublishProcessor

class MainGameViewModel(
    private var context: Application
    ) : AndroidViewModel(context){

    companion object {
        const val TAG = "MainGameViewModel"
    }
    var colorPlayer1 = 0
    var colorPlayer2 = 0
    var isFirstWinner = true
    var scoreWinner = 0
    val goToEndGame = PublishProcessor.create<Unit>()

    fun onEndGame(){
        Timber.v("-----------------------Endgame")
        goToEndGame.onNext(Unit)
    }

    override fun onCleared() {
        Timber.v("onCleared")
        super.onCleared()
    }
}