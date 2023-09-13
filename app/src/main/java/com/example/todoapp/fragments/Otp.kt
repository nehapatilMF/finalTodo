package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentOtpBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimerUtil
import com.example.todoapp.viewModels.OtpViewModel

class Otp : Fragment() {

    private var binding: FragmentOtpBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.navigate_to_register)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigate_to_register)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater, container, false)

        val viewModel = ViewModelProvider(this)[OtpViewModel::class.java]
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
                        viewModel.signupVerifyOtp(email, otp)
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
                viewModel.resendUserOtp(email)
                binding?.progressBar?.visibility = View.VISIBLE

            }else{
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel.otpResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.navigate_from_otp_to_todoMain)
            }else{
                val message = status.toString()
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            }

        }
        viewModel.resendOtpResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                viewModel.newOtpResult.observe(viewLifecycleOwner){ otp ->
                    val newOtp = otp.toString()
                    binding?.jsonOtp?.text = newOtp
                }
            }else{
                val message = status.toString()
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            }
        }
        val sessionManager = SessionManager(requireContext())
        Constants.accessToken = sessionManager.getAccessToken()
        Constants.refreshToken = sessionManager.getRefreshToken()

        viewModel.getAuthTokens().observe(viewLifecycleOwner){ authTokens ->
            Constants.clearAccessToken()
            val accessToken = authTokens.accessToken
            Constants.accessToken = accessToken
            val refreshToken = authTokens.refreshToken
            Constants.refreshToken = refreshToken
            sessionManager.saveTokens(accessToken,refreshToken)

        }

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
