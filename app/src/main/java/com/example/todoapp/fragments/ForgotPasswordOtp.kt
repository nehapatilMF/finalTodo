package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentForgotPasswordOtpBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimerUtil
import com.example.todoapp.viewModels.ForgotPasswordOtpViewModel
import com.example.todoapp.viewModels.ForgotPasswordViewModel

class ForgotPasswordOtp : Fragment() {
    private val viewModel: ForgotPasswordOtpViewModel by activityViewModels()

    private var binding: FragmentForgotPasswordOtpBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Forgot password"

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.navigate_From_forgotPasswordOtp_to_login)

        }
        val forgotPasswordViewModel: ForgotPasswordViewModel by activityViewModels()
        val email = forgotPasswordViewModel.email
        forgotPasswordViewModel.otpResult.observe(viewLifecycleOwner) { opt ->
            binding?.jsonOtp?.text = opt
        }

        binding?.btnNext?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {

                val otp = binding?.etOtp?.text.toString()

                if(otp.isNotEmpty()){
                    val otp1 = otp.toLong()
                    viewModel.forgotPasswordVerifyOtp(email, otp1)
                }else{

                    val message = "Please enter Otp"
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
                }

            } else {

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
        binding?.resendCode?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                binding?.timer?.visibility = View.VISIBLE
                binding?.tvOtpExp?.visibility = View.VISIBLE
                binding?.tvMin?.visibility = View.VISIBLE
                binding?.resendCode?.visibility = View.INVISIBLE
                startOtpTimer()
                viewModel.forgotPasswordResendOtp(email)
            } else {

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordOtpBinding.inflate(inflater, container, false)


        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        binding?.timer?.visibility = View.VISIBLE
        binding?.tvOtpExp?.visibility = View.VISIBLE
        binding?.tvMin?.visibility = View.VISIBLE
        binding?.resendCode?.visibility = View.INVISIBLE
        startOtpTimer()
        viewModel.otpResult.observe(viewLifecycleOwner) { status ->

            if (status == "200") {
                findNavController().navigate(R.id.navigate_to_newPassword)
                        }
        }
        viewModel.resendOtpResult.observe(viewLifecycleOwner) { status ->
            if (status == "200") {
                viewModel.newOtpResult.observe(viewLifecycleOwner) { newOtp ->
                    binding?.jsonOtp?.text = newOtp
                }
            }else{
                val message = getString(R.string.invalid_or_empty_email_id)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)


            }
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
                binding?.tvMin?.visibility = View.INVISIBLE
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
