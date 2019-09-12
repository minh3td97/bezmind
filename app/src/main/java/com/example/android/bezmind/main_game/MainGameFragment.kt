package com.example.android.bezmind.main_game

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.postDelayed
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.android.bezmind.R
import com.example.android.bezmind.databinding.MainGameFragmentBinding
import com.example.android.bezmind.main_game.MainGameViewModel
import com.jakewharton.rxbinding2.view.enabled
import com.wajahatkarim3.easyflipview.EasyFlipView
import io.reactivex.disposables.CompositeDisposable
import jp.qosmo.neurobeatbox.obtainViewModel
import kotlinx.android.synthetic.main.main_game_fragment.view.*
import timber.log.Timber
import java.sql.Time

class MainGameFragment : Fragment(){
    private lateinit var binding: MainGameFragmentBinding
    private val viewModel by lazy {
        activity!!.obtainViewModel(MainGameViewModel::class.java)
    }
    private val disposeBag = CompositeDisposable()
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

    }
    private var listFlipState = mutableListOf<EasyFlipView>()
    private var listMatchNumber = mutableListOf<Int>()  //gan tag cho moi o
    private var listImages = mutableListOf<Int>()   //danh sach hinh anh
    private var isFirstFlip : Boolean = true
    private var isSecondFlip: Boolean = false
    private var listOpenedSlot = mutableListOf<Int>()
    private var firstIndex = -1
    private var secondIndex = -1
    private var uiChangeHandler = Handler()

    private fun bindInputsTo(inputs: MainGameViewModel) {

        for (index: Int in 0..(binding.gameLayout.childCount - 1)){

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
                            firstIndex = index
                        } else if (!isFirstFlip && isSecondFlip){
                            isSecondFlip = false
                            secondIndex = index
                        }

                        if(!isSecondFlip && !isFirstFlip) {
                            if(firstIndex == secondIndex){
                                matchCorrect()
                            } else {
                                flipAllDown()
                            }
                        }

                    }

                }
            })
        }

        //(binding.flip00.getChildAt(0) as ImageView).setImageResource(R.drawable.heart)
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
    private fun matchCorrect(){
        listOpenedSlot.forEach{value ->
            uiChangeHandler.post{
                listFlipState[value].isFlipEnabled = false
                for (index: Int in 0..(listFlipState[value].childCount - 1)){
                    listFlipState[value].getChildAt(index).alpha = 0.0f
                }
            }
        }
        isFirstFlip = true
        isSecondFlip = false
        listOpenedSlot.clear()
    }

}