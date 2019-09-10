package com.example.android.bezmind.welcome

import android.os.Bundle
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
    }
    private var gameNameThread: GameNameThread? = null
    private fun makeGameNameMove(){
        gameNameThread = GameNameThread().apply {
            this.start()
        }
    }

    inner class GameNameThread: Thread(){
        private var isRunning = true
        private val unitTime = 1000L
        private val limitRotation = 4.0f
        private var rotateTime = 0L
        override fun run() {
            rotateTime = SystemClock.uptimeMillis()
            var buttonTextSize = binding.startButton.textSize
            var currentTime = 0L
            while (isRunning && !Thread.interrupted()){
                currentTime = SystemClock.uptimeMillis() - rotateTime
                var time = 1.0f

                if(currentTime >= 2 * unitTime){
                    rotateTime = SystemClock.uptimeMillis()
                    time = -1.0f
                } else if (currentTime >= unitTime){
                    time = 1 - currentTime.toFloat()/unitTime.toFloat()
                } else {
                    time = currentTime.toFloat()/unitTime.toFloat()
                }

                binding.gameName.rotation = limitRotation * time
                binding.startButton.textSize = time
                try{
                    sleep(25)
                }catch (e: InterruptedException) {
                    break
                }
            }

        }
    }
}