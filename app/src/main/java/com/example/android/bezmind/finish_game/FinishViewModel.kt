package com.example.android.bezmind.finish_game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import timber.log.Timber
import io.reactivex.processors.PublishProcessor

class FinishViewModel(
    private var context: Application
) : AndroidViewModel(context){

    companion object {
        const val TAG = "MainGameViewModel"
    }

    val goBackInfor = PublishProcessor.create<Unit>()

    fun onGoBackInfor(){
        Timber.v("-----------------------Endgame")
        goBackInfor.onNext(Unit)
    }

    override fun onCleared() {
        Timber.v("onCleared")
        super.onCleared()
    }
}