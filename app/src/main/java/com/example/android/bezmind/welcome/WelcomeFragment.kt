package com.example.android.bezmind.welcome

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.bezmind.databinding.WelcomeFragmentBinding
import timber.log.Timber

class WelcomeFragment: Fragment() {
    private lateinit var binding: WelcomeFragmentBinding
//    private val viewModel by lazy {
//        activity!!.obtainViewModel(WelcomeViewModel::class.java)
//    }
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
        makeGameNameMove()
        setupSpots()
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
        private val unitTime = 600L
        private var rotateTime = 0L
        override fun run() {
            rotateTime = SystemClock.uptimeMillis()
            var currentTime = 0L
            while (isRunning && !Thread.interrupted()){
                currentTime = SystemClock.uptimeMillis() - rotateTime

                if(currentTime >= unitTime){
                    var time = (-3..3).random()
                    binding.gameName.rotation =  time.toFloat()
                    rotateTime = 0L
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
        private var maxScaleTime = 500L
        private var startScaleTime = 0L
        override fun run() {
            startScaleTime = SystemClock.uptimeMillis()

            var currentTime = 0L
            var visibleStatus = View.VISIBLE
            var isShow = true
            while (isRunning && !Thread.interrupted()){
                currentTime = SystemClock.uptimeMillis() - startScaleTime
                var index = (currentTime / maxScaleTime).toInt()
                if(index > 6){
                    if (isShow){
                        visibleStatus = View.INVISIBLE
                        isShow = false
                    } else {
                        visibleStatus = View.VISIBLE
                        isShow = true
                    }

                    currentTime = 0L
                    startScaleTime = SystemClock.uptimeMillis()
                    index = 0
                }

                uiChangeHandler.post(){
                    getSpot(index).visibility = visibleStatus
                }

            }
        }
        private fun getSpot(index: Int) : View{
            when(index){
                0 -> return binding.spot1
                1 -> return binding.spot2
                2 -> return binding.spot3
                3 -> return binding.spot4
                4 -> return binding.spot5
                5 -> return binding.spot6
                else -> return binding.spot7
            }
        }

        override fun interrupt() {
            isRunning = false
        }
    }
}