package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentOtpBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimerUtil
import com.example.todoapp.viewModels.OtpViewModel

@Suppress("NAME_SHADOWING")
class Otp : Fragment() {

    private var binding: FragmentOtpBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.back_to_register)
            activity?.finishAffinity()
        }
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

        binding?.buttonAuthorise?.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable(requireContext())) {
                val otp = binding?.etOtp?.text.toString()

                if(otp.isNotEmpty()) {
                    val otp1 = otp.toLong()
                    Toast.makeText(requireContext(),"$email, $otp",Toast.LENGTH_SHORT).show()
                    viewModel.signupVerifyOtp(email, otp1)
                }else{

                    val message = "Please enter Otp"
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
                }
            }else{
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        binding?.resendCode?.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable(requireContext())) {
                binding?.timer?.visibility = View.VISIBLE
                binding?.tvOtpExp?.visibility = View.VISIBLE
                binding?.resendCode?.visibility = View.INVISIBLE
                startOtpTimer()
                viewModel.resendUserOtp(email)

            }else{

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

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
                viewModel.newOtpResult.observe(viewLifecycleOwner){ otp ->
                    val newOtp = otp.toString()
                    binding?.jsonOtp?.text = newOtp
                }

            }else{
                val message = getString(R.string.invalid_or_empty_email_id)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel.getAuthTokens().observe(viewLifecycleOwner){ authTokens ->
            Constants.clearAccessToken()
            val accessToken = authTokens.accessToken
            Constants.accessToken = accessToken
            val refreshToken = authTokens.refreshToken
            Constants.refreshToken = refreshToken
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
