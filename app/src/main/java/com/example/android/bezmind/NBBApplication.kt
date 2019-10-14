package jp.qosmo.neurobeatbox

import android.app.Application
import com.example.android.bezmind.BuildConfig
import com.orhanobut.hawk.Hawk
import timber.log.Timber

class NBBApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build();

        if (BuildConfig.BUILD_TYPE == "release") {
            // TODO: クラッシュレポート用のTimber Treeをplantする
        } else {
            // 通常のデバッグ用logcat出力
            Timber.plant(Timber.DebugTree())
        }


    }
}