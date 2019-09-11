package com.example.android.bezmind

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bezmind.choose_color.InforViewModel
import com.example.android.bezmind.welcome.WelcomeViewModel


class ViewModelFactory private constructor(
    val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {

                    isAssignableFrom(WelcomeViewModel::class.java) ->
                        WelcomeViewModel(
                            application
                        )
                    isAssignableFrom(InforViewModel::class.java) ->
                        InforViewModel(
                            application
                        )
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return INSTANCE ?: ViewModelFactory(application).also { INSTANCE = it }
        }
    }
}
