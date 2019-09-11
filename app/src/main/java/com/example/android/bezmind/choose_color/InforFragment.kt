package com.example.android.bezmind.choose_color

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.android.bezmind.databinding.InforFragmentBinding
import com.example.android.bezmind.welcome.WelcomeFragment
import com.example.android.bezmind.welcome.WelcomeViewModel
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.selected
import io.reactivex.disposables.CompositeDisposable
import jp.qosmo.neurobeatbox.obtainViewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

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

    }
    private fun setupColorChoosingButton(){
        var player1ButtonColors =
            arrayOf(
                binding.button1,
                binding.button2,
                binding.button3
            )
//        player1ButtonColors.forEach { button ->
//            button.setOnClickListener {
//
//                it.isSelected = true
//                Timber.v("click")
//            }
//        }
        binding.button1.setOnClickListener{
            binding.bkgPlayer1.setBackgroundColor(Color.parseColor("#FF8080"))
            it.isSelected = true
        }
        binding.button2.setOnClickListener{
            binding.bkgPlayer2.setBackgroundColor(Color.parseColor("#FF8080"))
            it.isSelected = true
        }

    }


}