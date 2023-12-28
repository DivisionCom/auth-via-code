package com.example.authviacode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.authviacode.data.remote.AuthApi
import com.example.authviacode.data.remote.responses.Code
import com.example.authviacode.databinding.FragmentAuthBinding
import com.example.authviacode.util.Constants.BASE_URL
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var authApi: AuthApi
    private lateinit var phoneNumber: String

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

        initRetrofit()
    }

    private fun initRetrofit(){
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        authApi = retrofit.create(AuthApi::class.java)
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
            binding.layoutPhone.isEnabled = false
            codeRequest()
        }
    }

    private fun codeRequest(){
        CoroutineScope(Dispatchers.Default).launch {
            authApi.getAuthCode(phoneNumber)
            val response = authApi.getAuthCode(phoneNumber)
            gettingCode(response)
        }
    }

    private fun gettingCode(response: Response<Code>){
        val code = response.body()
        requireActivity().runOnUiThread{
            Toast.makeText(context, "Ваш код: ${code?.code}", Toast.LENGTH_LONG).show()
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
                    if(maskFilled){
                        phoneNumber = "7$extractedValue"
                    }
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