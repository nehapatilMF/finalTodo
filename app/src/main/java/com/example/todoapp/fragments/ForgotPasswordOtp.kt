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
import com.example.todoapp.databinding.FragmentForgotPasswordOtpBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimerUtil
import com.example.todoapp.viewModels.ForgotPasswordOtpViewModel

class ForgotPasswordOtp : Fragment() {

    private var binding: FragmentForgotPasswordOtpBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Forgot password"

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.navigate_From_forgotPasswordOtp_to_login)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigate_From_forgotPasswordOtp_to_login)
                            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordOtpBinding.inflate(layoutInflater, container, false)

        binding?.timer?.visibility = View.VISIBLE
        binding?.tvOtpExp?.visibility = View.VISIBLE

        binding?.resendCode?.visibility = View.INVISIBLE
        startOtpTimer()

        val viewModel = ViewModelProvider(this)[ForgotPasswordOtpViewModel::class.java]

        viewModel.otpResult.observe(viewLifecycleOwner) { status ->
            if (status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.navigate_to_newPassword)
                viewModel.msg.observe(viewLifecycleOwner){msg ->
                    val message = msg.toString()
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                }
            }else{
                val message = status.toString()
                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()

            }
        }
        viewModel.resendOtpResult.observe(viewLifecycleOwner) { status ->
            if (status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                viewModel.newOtpResult.observe(viewLifecycleOwner) { newOtp ->
                binding?.jsonOtp?.text = newOtp
                }
               viewModel.msg.observe(viewLifecycleOwner){msg ->
                   val message = msg.toString()
                   Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
               }
            }else{
                val message = status.toString()
                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()

            }
        }
        val email = Constants.userEmail.toString()
        val otp1 = Constants.userOtp.toString()
            binding?.jsonOtp?.text = otp1

        binding?.btnNext?.setOnClickListener {
            val otp = binding?.etOtp?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                    viewModel.forgotPasswordVerifyOtp(email, otp)
                binding?.progressBar?.visibility = View.VISIBLE
            } else {
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
        binding?.resendCode?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                binding?.timer?.visibility = View.VISIBLE
                binding?.tvOtpExp?.visibility = View.VISIBLE

                binding?.resendCode?.visibility = View.INVISIBLE
                startOtpTimer()
                viewModel.forgotPasswordResendOtp(email)
                binding?.progressBar?.visibility = View.VISIBLE
            } else {

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel.otp.observe(viewLifecycleOwner){ otp ->
            val newOtp = otp.toString()
            Constants.newOtp = newOtp
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
