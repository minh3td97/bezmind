package com.example.android.bezmind.choose_color

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.processors.PublishProcessor
import timber.log.Timber

class InforViewModel (
    private var context: Application
) : AndroidViewModel(context){

    companion object {
        const val TAG = "InforViewModel"
    }
    val goToMainGame = PublishProcessor.create<Unit>()

    fun onTapStartButton(){
        Timber.v("-----------------------Click")
        goToMainGame.onNext(Unit)
    }


    override fun onCleared() {
        Timber.v("onCleared")
        super.onCleared()
    }
}