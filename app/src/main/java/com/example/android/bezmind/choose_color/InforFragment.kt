package com.example.android.bezmind.choose_color

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.android.bezmind.databinding.InforFragmentBinding
import com.jakewharton.rxbinding2.widget.color
import io.reactivex.disposables.CompositeDisposable
import jp.qosmo.neurobeatbox.obtainViewModel
import timber.log.Timber
import android.R.attr.button
import android.graphics.Color
import com.example.android.bezmind.R
import com.example.android.bezmind.welcome.WelcomeFragment
import com.jakewharton.rxbinding2.view.clicks
import java.util.concurrent.TimeUnit

//import javax.swing.text.StyleConstants.getBackground



class InforFragment : Fragment() {
    private lateinit var binding: InforFragmentBinding
    private val viewModel by lazy {
        activity!!.obtainViewModel(InforViewModel::class.java)
    }
    private val disposeBag = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InforFragmentBinding.inflate(inflater, container, false)
        Timber.plant(Timber.DebugTree())
        return binding.root

    }
    companion object {
        const val TAG = "InforFragment"
        private const val DOUBLE_CLICK_PROTECTION_INTERVAL = 500L
        fun newInstance() = InforFragment()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        bindInputsTo(viewModel)
        setupColorChoosingButton()

    }
    private fun bindInputsTo(inputs: InforViewModel) {
        with(binding) {
            disposeBag.add (
                startButton.clicks()
                    .throttleFirst(InforFragment.DOUBLE_CLICK_PROTECTION_INTERVAL, TimeUnit.MILLISECONDS)
                    .subscribe {
                        inputs?.onTapStartButton()
                    }
            )
        }
    }
    private var listColors =
        arrayOf(
            R.color.color_white_choose,
            R.color.color_red,
            R.color.color_orange,
            R.color.color_green,
            R.color.color_yellow,
            R.color.color_clayan,
            R.color.color_blue,
            R.color.color_light_violet,
            R.color.color_violet,
            R.color.color_pink,
            R.color.color_pink_2,
            R.color.color_default
        )
    private var curPlayer1Color = 0
    private var curPlayer2Color = 11
    private fun setupColorChoosingButton(){
        var player1Button =
            arrayOf(
                binding.button01,
                binding.button11,
                binding.button21,
                binding.button31,
                binding.button41,
                binding.button51,
                binding.button61,
                binding.button71,
                binding.button81,
                binding.button91,
                binding.button101,
                binding.button111
            )
        var player2Button =
            arrayOf(
                binding.button02,
                binding.button12,
                binding.button22,
                binding.button32,
                binding.button42,
                binding.button52,
                binding.button62,
                binding.button72,
                binding.button82,
                binding.button92,
                binding.button102,
                binding.button112
            )
        var player1ButtonBkg =
            arrayOf(
                binding.bkgButton01,
                binding.bkgButton11,
                binding.bkgButton21,
                binding.bkgButton31,
                binding.bkgButton41,
                binding.bkgButton51,
                binding.bkgButton61,
                binding.bkgButton71,
                binding.bkgButton81,
                binding.bkgButton91,
                binding.bkgButton101,
                binding.bkgButton111


            )
        var player2ButtonBkg =
            arrayOf(
                binding.bkgButton02,
                binding.bkgButton12,
                binding.bkgButton22,
                binding.bkgButton32,
                binding.bkgButton42,
                binding.bkgButton52,
                binding.bkgButton62,
                binding.bkgButton72,
                binding.bkgButton82,
                binding.bkgButton92,
                binding.bkgButton102,
                binding.bkgButton112
            )

        player1ButtonBkg[curPlayer1Color].visibility = Button.VISIBLE
        player2ButtonBkg[curPlayer2Color].visibility = Button.VISIBLE
        viewModel.colorPlayer1 = getResources().getColor(listColors[curPlayer1Color])
        viewModel.colorPlayer2 = getResources().getColor(listColors[curPlayer2Color])


        for ((index, button) in player1Button.withIndex() ){
            button.setOnClickListener{
                player1ButtonBkg[curPlayer1Color].visibility = Button.INVISIBLE
                player1ButtonBkg[index].visibility = Button.VISIBLE
                var color = getResources().getColor(listColors[index])
                viewModel.colorPlayer1 = color
                binding.bkgPlayer1.setBackgroundColor(color)
                binding.bkgPlayer1.alpha = 0.4f
                curPlayer1Color = index

            }
        }
        for((index, button) in player2Button.withIndex()){
            button.setOnClickListener{
                player2ButtonBkg[curPlayer2Color].visibility = Button.INVISIBLE
                player2ButtonBkg[index].visibility = Button.VISIBLE
                var color = getResources().getColor(listColors[index])
                binding.bkgPlayer2.setBackgroundColor(color)
                viewModel.colorPlayer2 = color
                viewModel.colorPlayer2
                binding.bkgPlayer2.alpha = 0.65f
                curPlayer2Color = index
            }
        }
    }


}