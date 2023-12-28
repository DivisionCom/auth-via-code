package com.example.authviacode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.authviacode.databinding.FragmentAuthBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhoneSample()
        changeBtnColors(enabled = false)
        getCodeElements()
    }

    private fun codeElementsVisibility(visibility: Int){
        binding.tvLabelCode.visibility = visibility
        binding.layoutCode.visibility = visibility
    }

    private fun getCodeElements(){
        codeElementsVisibility(View.INVISIBLE)
        binding.btnContinue.setOnClickListener {
            codeElementsVisibility(View.VISIBLE)
            binding.btnContinue.visibility = View.GONE
            binding.btnAuth.visibility = View.VISIBLE
        }
    }

    private fun changeBtnColors(enabled: Boolean){
        val btn = binding.btnContinue
        if(!enabled){
            btn.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.btnDisBack, null))
            btn.setTextColor(ResourcesCompat.getColor(resources, R.color.btnDisText, null))
        } else {
            btn.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainBlack, null))
            btn.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
        }
    }

    private fun setupPhoneSample() {
        val listener: MaskedTextChangedListener = MaskedTextChangedListener.installOn(
            binding.etPhone,
            "+7 ([000]) [000]-[00]-[00]",
            object: MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String,
                    tailPlaceholder: String
                ) {
                    changeBtnColors(maskFilled)
                }
            }
        )
        binding.etPhone.hint = listener.placeholder()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AuthFragment()
    }
}