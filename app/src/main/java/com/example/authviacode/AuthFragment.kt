package com.example.authviacode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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