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

    val goToMainProcessor = PublishProcessor.create<Unit>()

    fun onTapStartButton(){
        Timber.v("-----------------------Click")
        goToMainProcessor.onNext(Unit)
    }

    override fun onCleared() {
        Timber.v("onCleared")
        super.onCleared()
    }
}