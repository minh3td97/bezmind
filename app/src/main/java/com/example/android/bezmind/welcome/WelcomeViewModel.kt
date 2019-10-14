package com.example.android.bezmind.welcome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import timber.log.Timber
import io.reactivex.processors.PublishProcessor

class WelcomeViewModel(
    private var context: Application
    ) : AndroidViewModel(context){

    companion object {
        const val TAG = "WelcomeViewModel"
    }

    val goToInforProcessor = PublishProcessor.create<Unit>()

    fun onTapStartButton(){
        Timber.v("-----------------------Click")
        goToInforProcessor.onNext(Unit)
    }

    override fun onCleared() {
        Timber.v("onCleared")
        super.onCleared()
    }
}