package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentOtpBinding
import com.example.todoapp.interfaces.UserAPI
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimerUtil
import com.example.todoapp.viewModelFactory.UserViewModelFactory
import com.example.todoapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class Otp : Fragment() {

    private var binding: FragmentOtpBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_otp_to_register)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_otp_to_register)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val sessionManager = SessionManager(requireContext())
        val userAPI: UserAPI? = RetrofitClient.getInstance()?.create(UserAPI::class.java)
        val userRepository = userAPI?.let { UserRepository(it) }
        val viewModelFactory = userRepository?.let { UserViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(UserViewModel::class.java)

        val email = Constants.userEmail.toString()
        val otp1 =  Constants.userOtp.toString()

        binding?.enteredEmail?.text = email
        binding?.jsonOtp?.text = otp1
        binding?.timer?.visibility = View.VISIBLE
        binding?.tvOtpExp?.visibility = View.VISIBLE
        binding?.resendCode?.visibility = View.INVISIBLE
        startOtpTimer()

        binding?.btnNext?.setOnClickListener {
            val otp = binding?.etOtp?.text.toString()
            when{
                !NetworkUtil.isNetworkAvailable(requireContext()) -> DialogUtils.showAutoDismissAlertDialog(requireContext(), getString(R.string.no_internet_connection))
                else -> {
                    viewModel?.signupVerifyOtp(email, otp)
                    binding?.progressBar?.visibility = View.VISIBLE
                }
            }
        }

        binding?.resendCode?.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable(requireContext())) {
                binding?.timer?.visibility = View.VISIBLE
                binding?.tvOtpExp?.visibility = View.VISIBLE
                binding?.resendCode?.visibility = View.INVISIBLE
                startOtpTimer()
                viewModel?.resendUserOtp(email)
                binding?.progressBar?.visibility = View.VISIBLE

            }else{
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel?.otpResult?.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_otp_to_home)
            }else{
                binding?.progressBar?.visibility = View.INVISIBLE
                val message = status.toString()
                Snackbar.make(requireView(),message,Snackbar.LENGTH_SHORT).show()
            }

        }
        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                viewModel.otpResult.observe(viewLifecycleOwner){ otp ->
                    val newOtp = otp.toString()
                    binding?.jsonOtp?.text = newOtp
                }
            }else{
                binding?.progressBar?.visibility = View.INVISIBLE
                val message = status.toString()
                Snackbar.make(requireView(),message,Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel?.getAuthTokens()?.observe(viewLifecycleOwner){ authTokens ->
            val accessToken = authTokens.accessToken
            val refreshToken = authTokens.refreshToken
            sessionManager.clearTokens()
            sessionManager.saveTokens(accessToken,refreshToken)
        }
        viewModel?.name?.observe(viewLifecycleOwner){name ->
            sessionManager.saveName(name.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    private fun startOtpTimer(){
        val timerDuration: Long = getString(R.string.timer_duration).toLongOrNull() ?: 0L
        TimerUtil.startTimer(
            duration = timerDuration,
            onTick = { remainingTime ->
                val minutes = remainingTime / 60
                val seconds = remainingTime % 60
                binding?.timer?.text = String.format("%02d:%02d", minutes, seconds)

            },
            onFinish = {
                binding?.timer?.text = getString(R.string.timer_placeholder)
                binding?.timer?.visibility = View.INVISIBLE
                binding?.tvOtpExp?.visibility = View.INVISIBLE
                binding?.resendCode?.visibility = View.VISIBLE
            }
        )
    }
    override fun onDestroy() {
        TimerUtil.cancelTimer()
        super.onDestroy()
        binding = null
    }
}
