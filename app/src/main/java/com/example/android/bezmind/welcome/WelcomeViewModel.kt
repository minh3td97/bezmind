package com.example.android.bezmind.welcome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import timber.log.Timber

class WelcomeViewModel(
    private var context: Application
    ) : AndroidViewModel(context){
    companion object {
        const val TAG = "WelcomeViewModel"
    }

//    val goToMainProcessor = PublishProcessor.create<Unit>()
//
//    fun onTapRecordButton(){
//        goToMainProcessor.onNext(Unit)
//    }

    override fun onCleared() {
        Timber.v("onCleared")
        super.onCleared()
    }
}