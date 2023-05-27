package com.example.eventsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.ApiService
import com.example.eventsapp.Event
import com.example.eventsapp.LoginJson
import com.example.eventsapp.LoginResponse
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentLoginBinding
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.proceedButton.setOnClickListener {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://18.185.8.100/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val api: ApiService = retrofit.create(ApiService::class.java)

            val username: String = binding.editTextUsername.text.toString()
            val password: String = binding.editTextPassword.text.toString()
            val loginData = LoginJson(username=username, password=password)

            val loginCall = api.loginUser(loginData)

            loginCall.enqueue(object: Callback<LoginResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful){
                        val loginResponse: LoginResponse? = response.body()

                        val responseUserId = loginResponse!!.id
                        val responseUsername = loginResponse.username
                        val bundle = Bundle()
                        bundle.putInt("id", responseUserId)
                        bundle.putString("username", responseUsername)

                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment, bundle)

                    } else if (response.code() == 401) {
                        Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }

            })
        }

        return binding.root
    }

}