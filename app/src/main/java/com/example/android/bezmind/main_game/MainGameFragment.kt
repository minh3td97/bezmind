package com.example.android.bezmind.main_game

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.android.bezmind.R
import com.example.android.bezmind.databinding.MainGameFragmentBinding
import com.wajahatkarim3.easyflipview.EasyFlipView
import io.reactivex.disposables.CompositeDisposable
import jp.qosmo.neurobeatbox.obtainViewModel
import timber.log.Timber

@Suppress("DEPRECATION")
class MainGameFragment : Fragment(){
    private lateinit var binding: MainGameFragmentBinding
    private val viewModel by lazy {
        activity!!.obtainViewModel(MainGameViewModel::class.java)
    }
    private val disposeBag = CompositeDisposable()
    var colorPlayer1 = 0
    var colorPlayer2 = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainGameFragmentBinding.inflate(inflater, container, false)
        Timber.plant(Timber.DebugTree())
        return binding.root

    }
    companion object {
        const val TAG = "MainGameFragment"
        private const val DOUBLE_CLICK_PROTECTION_INTERVAL = 500L
        fun newInstance() = MainGameFragment()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        bindInputsTo(viewModel)
        setupListImageIndex()
        setupGridImage()
        setupColorOfScene()
        setupPlayer()
        //setupTimeCounter()
    }
    private var listFlipState = mutableListOf<EasyFlipView>()
    private var listMatches = mutableMapOf<Int, Int>()  //gan tag cho moi o
    private var listImages = mutableListOf<Int>()   //danh sach hinh anh
    private var isFirstFlip : Boolean = true
    private var isSecondFlip: Boolean = false
    private var listOpenedSlot = mutableListOf<Int>()
    private var firstIndex = -1
    private var secondIndex = -1
    private var uiChangeHandler = Handler()

    private fun bindInputsTo(inputs: MainGameViewModel) {

        for (index: Int in 0 until binding.gameLayout.childCount){

            listFlipState.add(binding.gameLayout.getChildAt(index) as EasyFlipView )

            listFlipState[index].setOnFlipListener(object :EasyFlipView.OnFlipAnimationListener {
                override fun onViewFlipCompleted(
                    easyFlipView: EasyFlipView?,
                    newCurrentSide: EasyFlipView.FlipState?
                ) {
                    if (newCurrentSide == EasyFlipView.FlipState.BACK_SIDE){
                        listOpenedSlot.add(index)
                        if(isFirstFlip){
                            isFirstFlip = false
                            isSecondFlip = true
                            firstIndex = listMatches.getValue(index)
                        } else if (!isFirstFlip && isSecondFlip){
                            isSecondFlip = false
                            secondIndex = listMatches.getValue(index)
                        }

                        if(!isSecondFlip && !isFirstFlip) {
                            if(firstIndex == secondIndex){
                                matchCorrect()
                            } else {
                                flipAllDown()
                                changeTurn()
                            }
                        }

                    }

                }
            })
        }

    }
    private fun setupListImageIndex(){
        listImages.add(R.drawable.card1)
        listImages.add(R.drawable.card2)
        listImages.add(R.drawable.card3)
        listImages.add(R.drawable.card4)
        listImages.add(R.drawable.card5)
        listImages.add(R.drawable.card6)
        listImages.add(R.drawable.card7)
        listImages.add(R.drawable.card8)
        listImages.add(R.drawable.card9)
        listImages.add(R.drawable.card10)
        listImages.add(R.drawable.card11)
        listImages.add(R.drawable.card12)
        listImages.add(R.drawable.card13)
        listImages.add(R.drawable.card14)
        listImages.add(R.drawable.card15)
        listImages.add(R.drawable.card16)
        listImages.add(R.drawable.card17)
        listImages.add(R.drawable.card18)
        listImages.add(R.drawable.card19)
        listImages.add(R.drawable.card20)
        listImages.add(R.drawable.card21)
    }
    private fun setupGridImage(){
        var listIndex = mutableListOf<Int>()
        for (index: Int in 0 until binding.gameLayout.childCount){
            listIndex.add(index)
        }
        listIndex.shuffle()
        val nuImage = listImages.size/3
        var counter = 0
        var imageIndex = 0
        listIndex.forEach{
            listMatches.put(it, imageIndex)
            (listFlipState[it].getChildAt(0) as ImageView)
                .setImageResource(listImages[imageIndex])

            ++counter
            if(counter > 1){
                counter = 0
                ++imageIndex
                if(imageIndex >= nuImage) imageIndex = 0
            }
        }
    }
    private fun setupPlayer(){
        player1Score = 0
        player2Score = 0
        isPlayer1Turn = true

    }



    private fun setupColorOfScene(){
        var listColorGrad = IntArray(2)
        listColorGrad[0] = colorPlayer1
        listColorGrad[1] = colorPlayer2
        viewModel.colorPlayer1 = colorPlayer1
        viewModel.colorPlayer2 = colorPlayer2

        var gradient = GradientDrawable()
        gradient.setColors(listColorGrad)
        gradient.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradient.orientation = GradientDrawable.Orientation.BL_TR
        binding.backgroudGradient.background = gradient
        binding.backgroudGradient.alpha = 0.6f


    }

    private fun flipAllDown(){
        listOpenedSlot.forEach{value ->
            uiChangeHandler.post{
                listFlipState[value].flipTheView()
            }
        }
        isFirstFlip = true
        isSecondFlip = false
        listOpenedSlot.clear()

    }
    var player1Score = 0
    var player2Score = 0
    var isPlayer1Turn = true

    @SuppressLint("SetTextI18n")
    private fun matchCorrect(){
        listOpenedSlot.forEach{value ->
            uiChangeHandler.post{
                listFlipState[value].isFlipEnabled = false
                for (index: Int in 0 until listFlipState[value].childCount){
                    listFlipState[value].getChildAt(index).alpha = 0.0f
                }
            }
        }
        isFirstFlip = true
        isSecondFlip = false
        listOpenedSlot.clear()
        if(isPlayer1Turn){
            ++player1Score
            uiChangeHandler.post {
                binding.player1Score.text = "Score: " + player1Score.toString()
            }
        } else {
            ++player2Score
            uiChangeHandler.post {
                binding.player2Score.text = "Score: " + player2Score.toString()
            }
        }
        if(player1Score > player2Score){
            viewModel.scoreWinner = player1Score
            viewModel.isFirstWinner = true

        } else{
            viewModel.scoreWinner = player2Score
            viewModel.isFirstWinner = false
        }

        if(player1Score + player2Score == listMatches.size/2){
            endGame()
        }
    }
    private fun changeTurn() {
        isPlayer1Turn = !isPlayer1Turn
        if (isPlayer1Turn) {
            uiChangeHandler.post {
                binding.player1Time.visibility = TextView.VISIBLE
                //timePlayer1CountDownThread!!.startPlaying()
                binding.player2Time.visibility = TextView.INVISIBLE
                //timePlayer2CountDownThread!!.stopPlaying()
            }
        } else {
            uiChangeHandler.post {
                binding.player2Time.visibility = TextView.VISIBLE
                //timePlayer2CountDownThread!!.startPlaying()
                binding.player1Time.visibility = TextView.INVISIBLE
                //timePlayer1CountDownThread!!.stopPlaying()

            }
        }
    }

    private fun endGame(){
        viewModel.onEndGame()
    }
    private var timePlayer1CountDownThread: TimeCountDownThread?= null
    private var timePlayer2CountDownThread: TimeCountDownThread?= null

    private fun setupTimeCounter(){
        timePlayer1CountDownThread = TimeCountDownThread(this, binding.player1Time)
            .apply {
                this.start()
            }
        timePlayer2CountDownThread = TimeCountDownThread(this, binding.player2Time)
            .apply {
                this.start()
            }
        timePlayer1CountDownThread!!.startPlaying()
    }

    inner class TimeCountDownThread(
        private val mainGameFragment: MainGameFragment,
        private val timeCounter : TextView
    ) : Thread(){
        private var isRunning = false
        private val DEFAULT_COUNT_DOWN_TIME = 10000L
        private var timeLeft = 0L
        private var timeStart = 0L
        private var elapsedTime = 0L

        override fun run() {
            timeLeft = DEFAULT_COUNT_DOWN_TIME
            timeStart = SystemClock.uptimeMillis()

            while (isRunning ){
                elapsedTime = SystemClock.uptimeMillis() - timeStart
                var secondLeft = (timeLeft - elapsedTime)/1000
                //Timber.v("------------TimeLeft" + timeStart.toString())
                if(timeLeft - elapsedTime <= 0){
                    stopPlaying()
                } else {
                    uiChangeHandler.post ( Runnable {
                        timeCounter.text = secondLeft.toString()
                    })
                }


                try{
                    sleep(25)
                }catch (e: InterruptedException) {
                    break
                }
            }
        }
        fun stopPlaying(){
            isRunning = false
            timeLeft = 0L
            mainGameFragment.changeTurn()
        }
        fun increaseTimeLeft(time: Int){
            timeLeft += time
        }
        fun startPlaying(){
            timeLeft = DEFAULT_COUNT_DOWN_TIME
            timeStart = SystemClock.uptimeMillis()
            Timber.v("------------TimeLeft" + timeStart.toString())
            isRunning = true

        }
    }

}