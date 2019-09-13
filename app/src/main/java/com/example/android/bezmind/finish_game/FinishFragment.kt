package com.example.android.bezmind.finish_game

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android.bezmind.R
import com.example.android.bezmind.databinding.FinishFragmentBinding
import com.example.android.bezmind.databinding.WelcomeFragmentBinding
import com.example.android.bezmind.finish_game.FinishViewModel
import com.example.android.bezmind.welcome.WelcomeFragment
import io.reactivex.disposables.CompositeDisposable
import jp.qosmo.neurobeatbox.obtainViewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit
import com.jakewharton.rxbinding2.view.clicks

class FinishFragment: Fragment() {
    private lateinit var binding: FinishFragmentBinding
    private val viewModel by lazy {
        activity!!.obtainViewModel(FinishViewModel::class.java)
    }
    private val disposeBag = CompositeDisposable()
    var colorPlayer1 = 0
    var colorPlayer2 = 0
    var score = 0
    var isFirstPlayerWinner = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FinishFragmentBinding.inflate(inflater, container, false)
        Timber.plant(Timber.DebugTree())
        return binding.root

    }

    companion object {
        const val TAG = "FinishFragment"
        private const val DOUBLE_CLICK_PROTECTION_INTERVAL = 500L
        fun newInstance() = FinishFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        bindInputsTo(viewModel)
        setColor4Layout()
    }

    private var uiChangeHandler = Handler()

    private fun bindInputsTo(viewModel : FinishViewModel){
        with(binding){
            disposeBag.add(
                playAgainButton.clicks()
                    .throttleFirst(FinishFragment.DOUBLE_CLICK_PROTECTION_INTERVAL, TimeUnit.MILLISECONDS)
                    .subscribe {
                        viewModel?.onGoBackInfor()
                    }
            )
        }
    }
    private fun setColor4Layout(){
        var listColorGrad = IntArray(2)
        listColorGrad[0] = resources.getColor(R.color.color_red)
        listColorGrad[1] = resources.getColor(R.color.color_orange)

        var gradient = GradientDrawable()
        gradient.setColors(listColorGrad)
        gradient.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradient.orientation = GradientDrawable.Orientation.BL_TR
        binding.background.background = gradient
        binding.background.alpha = 0.6f
    }

}