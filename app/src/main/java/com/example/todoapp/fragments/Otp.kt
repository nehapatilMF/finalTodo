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
import com.example.todoapp.databinding.FragmentOtpBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.TimerUtil
import com.example.todoapp.viewModels.OtpViewModel
import com.example.todoapp.viewModels.RegisterViewModel

class Otp : Fragment() {

    private val viewModel: OtpViewModel by activityViewModels()
    private var binding: FragmentOtpBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.back_to_register)

        }
        val registerViewModel: RegisterViewModel by activityViewModels()
        val email = registerViewModel.email
        binding?.enteredEmail?.text = email

        registerViewModel.otpResult.observe(viewLifecycleOwner) { opt ->
            binding?.jsonOtp?.text = opt
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        binding?.timer?.visibility = View.VISIBLE
        binding?.tvOtpExp?.visibility = View.VISIBLE
        binding?.resendCode?.visibility = View.INVISIBLE
        startOtpTimer()
        viewModel.otpResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                findNavController().navigate(R.id.navigate_from_otp_to_todoMain)
            }else{
                viewModel.otpResult.observe(viewLifecycleOwner){msg ->
                    val message = msg.toString()
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
                }
            }

        }
        viewModel.resendOtpResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                viewModel.newOtpResult.observe(viewLifecycleOwner){ newOtp ->
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
