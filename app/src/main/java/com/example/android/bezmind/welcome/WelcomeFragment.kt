package com.example.android.bezmind.welcome

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.bezmind.databinding.WelcomeFragmentBinding
import io.reactivex.disposables.CompositeDisposable
import jp.qosmo.neurobeatbox.obtainViewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit
import com.jakewharton.rxbinding2.view.clicks

class WelcomeFragment: Fragment() {
    private lateinit var binding: WelcomeFragmentBinding
    private val viewModel by lazy {
        activity!!.obtainViewModel(WelcomeViewModel::class.java)
    }
    private val disposeBag = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        Timber.plant(Timber.DebugTree())
        return binding.root

    }

    companion object {
        const val TAG = "WelcomeFragment"
        private const val DOUBLE_CLICK_PROTECTION_INTERVAL = 500L
        fun newInstance() = WelcomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.welcomeFragment = viewModel
        bindInputsTo(viewModel)
        makeGameNameMove()
        setupSpots()

    }
    private fun bindInputsTo(inputs: WelcomeViewModel) {
        with(binding) {
            disposeBag.add (
                startText.clicks()
                    .throttleFirst(DOUBLE_CLICK_PROTECTION_INTERVAL, TimeUnit.MILLISECONDS)
                    .subscribe {
                        inputs?.onTapStartButton()
                    }
            )
        }
    }
    private var gameNameThread: GameNameThread? = null
    private var spotThread: SpotThread? = null
    val uiChangeHandler = Handler()

    private fun makeGameNameMove(){
        gameNameThread = GameNameThread().apply {
            this.start()
        }
    }
    inner class GameNameThread: Thread(){
        private var isRunning = true
        private val unitTime = 250L
        private var rotateTime = 0L
        override fun run() {
            rotateTime = SystemClock.uptimeMillis()
            var currentTime = 0L
            while (isRunning && !Thread.interrupted()){
                currentTime = SystemClock.uptimeMillis() - rotateTime

                if(currentTime >= unitTime){
                    var time = (-2..2).random()
                    binding.gameName.rotation =  time.toFloat()
                    rotateTime = SystemClock.uptimeMillis()
                }

                try{
                    sleep(25)
                }catch (e: InterruptedException) {
                    break
                }
            }

        }

        override fun interrupt() {
            isRunning = false
            binding.gameName.rotation =  0f
        }
    }
    private fun setupSpots(){
        spotThread = SpotThread().apply {
            this.start()
        }
    }
    inner class SpotThread(
    ): Thread(){
        private var isRunning = true
        private var maxScaleTime = 350L
        private var startScaleTime = 0L
        override fun run() {
            startScaleTime = SystemClock.uptimeMillis()

            var currentTime = 0L
            var visibleStatus = View.INVISIBLE
            var isShow = false
            while (isRunning && !Thread.interrupted()){
                currentTime = SystemClock.uptimeMillis() - startScaleTime
                var index = (currentTime / maxScaleTime).toInt()
                if(index >= 8){
                    if (isShow){
                        visibleStatus = View.INVISIBLE
                        isShow = false
                    } else {
                        visibleStatus = View.VISIBLE
                        isShow = true
                    }

                    startScaleTime = SystemClock.uptimeMillis()
                    index = 0
                }

                uiChangeHandler.post(){
                    getSpot(index).visibility = visibleStatus
                }
                try{
                    sleep(50)
                }catch (e: InterruptedException) {
                    break
                }

            }
        }
        private fun getSpot(index: Int) : View{
            when(index){
                1 -> return binding.spot2
                2 -> return binding.spot3
                3 -> return binding.spot4
                4 -> return binding.spot5
                5 -> return binding.spot6
                6 -> return binding.spot7
                else -> return  binding.spot1
            }
        }

        override fun interrupt() {
            isRunning = false
        }
    }
}