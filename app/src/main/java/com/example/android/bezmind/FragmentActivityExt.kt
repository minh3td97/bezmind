package jp.qosmo.neurobeatbox


import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.android.bezmind.ViewModelFactory

fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application))
                .get(viewModelClass)
