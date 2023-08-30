package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentForgotPasswordOtpBinding
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimerUtil

class ForgotPasswordOtp : Fragment() {

    private var binding: FragmentForgotPasswordOtpBinding? = null
    private val editTextList = mutableListOf<View>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnBack?.setOnClickListener{
            findNavController().navigate(R.id.navigate_to_forgotPassword)
        }
        binding?.btnNext?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                findNavController().navigate(R.id.navigate_to_newPassword)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding?.resendCode?.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable(requireContext())) {
                binding?.timer?.visibility = View.VISIBLE
                binding?.tvOtpExp?.visibility = View.VISIBLE
                binding?.resendCode?.visibility = View.INVISIBLE
                startOtpTimer()
            }else{
                Toast.makeText(requireContext(),getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordOtpBinding.inflate(inflater, container, false)

        binding?.otpBox1?.let { editTextList.add(it) }
        binding?.otpBox2?.let { editTextList.add(it) }
        binding?.otpBox3?.let { editTextList.add(it) }
        binding?.otpBox4?.let { editTextList.add(it) }
        binding?.otpBox1?.requestFocus()

        for (i in 0 until editTextList.size) {
            val editText = editTextList[i] as EditText
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    editText.setBackgroundResource(R.drawable.otp_box_changed_background)
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        moveToNextBox(i)
                    } else if (s?.isEmpty() == true) {
                        moveToPreviousBox(i)
                    }
                }
            })
        }
        binding?.timer?.visibility = View.VISIBLE
        binding?.tvOtpExp?.visibility = View.VISIBLE
        binding?.resendCode?.visibility = View.INVISIBLE
        startOtpTimer()
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

    private fun moveToNextBox(currentIndex: Int) {
        if (currentIndex < editTextList.size - 1) {
            editTextList[currentIndex].clearFocus()
            editTextList[currentIndex + 1].requestFocus()
        }
    }
    private fun moveToPreviousBox(currentIndex: Int) {
        if (currentIndex > 0) {
            editTextList[currentIndex].clearFocus()
            editTextList[currentIndex - 1].requestFocus()
        }
    }
    override fun onDestroy() {
        TimerUtil.cancelTimer()
        super.onDestroy()
        binding = null
    }
}
