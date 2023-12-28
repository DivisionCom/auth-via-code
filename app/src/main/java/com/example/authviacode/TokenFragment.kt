package com.example.authviacode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.authviacode.databinding.FragmentTokenBinding

class TokenFragment : Fragment() {
    private lateinit var binding: FragmentTokenBinding
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTokenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPhone.text = "Здравствуйте, \"${model.liveDataUser.value?.phone}\""
        binding.tvToken.text = "\"${model.liveDataToken.value?.token}\""
    }

    companion object {
        @JvmStatic
        fun newInstance() = TokenFragment()
    }
}