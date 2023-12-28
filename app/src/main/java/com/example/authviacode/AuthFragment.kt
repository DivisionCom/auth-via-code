package com.example.authviacode

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.authviacode.data.remote.AuthApi
import com.example.authviacode.data.remote.responses.Code
import com.example.authviacode.databinding.FragmentAuthBinding
import com.example.authviacode.util.Constants.BASE_URL
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var authApi: AuthApi
    private lateinit var phoneNumber: String
    private var requesting: Int = 0
    private val model: MainViewModel by activityViewModels()

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

        initRetrofit()
        getCodeElements()

        btnAuthEnabling()
    }

    private fun btnAuthOnClick() {
        binding.btnAuth.setOnClickListener {

        }
    }

    private fun btnAuthEnabling() {
        changeBtnAuthColors(!binding.btnAuth.isEnabled)
        binding.etCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnAuth.isEnabled = binding.etCode.text?.length == 6
                changeBtnAuthColors(binding.btnAuth.isEnabled)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun changeBtnAuthColors(enabled: Boolean){
        if(enabled){
            binding.btnAuth.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.mainBlack, null))
            binding.btnAuth.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
        } else {
            binding.btnAuth.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.btnDisBack, null))
            binding.btnAuth.setTextColor(ResourcesCompat.getColor(resources, R.color.btnDisText, null))
        }
    }

    private fun initRetrofit() {
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

    private fun codeElementsVisibility(visibility: Int) {
        binding.tvLabelCode.visibility = visibility
        binding.layoutCode.visibility = visibility
    }

    private fun getCodeElements() {
        codeElementsVisibility(View.INVISIBLE)
        binding.btnContinue.setOnClickListener {
            codeElementsVisibility(View.VISIBLE)
            binding.btnContinue.visibility = View.GONE
            binding.btnAuth.visibility = View.VISIBLE
            binding.btnCodeRequest.visibility = View.VISIBLE
            binding.layoutPhone.isEnabled = false
            codeRequest()
        }
    }

    private fun codeRequest() {
        CoroutineScope(Dispatchers.Default).launch {
            val response = authApi.getAuthCode(phoneNumber)
            gettingCode(response)
        }
    }

    private fun gettingCode(response: Response<Code>) {
        val code = response.body()
        requireActivity().runOnUiThread {
            if (code?.status == "new" || requesting > 0) {
                Toast.makeText(context, "Ваш код: ${code?.code}", Toast.LENGTH_LONG).show()
            } else if (code?.status == "old") {
                Toast.makeText(
                    context,
                    "Если забыли код, нажмите \"Запросить код\"",
                    Toast.LENGTH_SHORT
                ).show()
            }
            model.liveDataUser.value = User(
                phone = phoneNumber,
                code = code?.code
            )
            btnCodeRequestOnClick()
        }
    }

    private fun btnCodeRequestOnClick() {
        binding.btnCodeRequest.setOnClickListener {
            requesting++
            codeRequest()
        }
    }

    private fun changeBtnColors(enabled: Boolean) {
        val btn = binding.btnContinue
        if (!enabled) {
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
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String,
                    tailPlaceholder: String
                ) {
                    changeBtnColors(maskFilled)
                    if (maskFilled) {
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