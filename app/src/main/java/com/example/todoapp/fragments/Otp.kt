package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentOtpBinding
import com.example.todoapp.util.TimerUtil
import com.example.todoapp.viewModels.RegisterViewModel

class Otp : Fragment() {
    private var binding : FragmentOtpBinding? = null
    private val editTextList = mutableListOf<View>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registerViewModel: RegisterViewModel by activityViewModels()

        binding?.enteredEmail?.text = registerViewModel.email

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.back_to_register)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding?.buttonAuthorise?.setOnClickListener{
            findNavController().navigate(R.id.navigate_from_otp_to_todoMain)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(layoutInflater, container, false)

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

        val timerDuration : Long = getString(R.string.timer_duration).toLongOrNull()?:0L
        TimerUtil.startTimer(
            duration = timerDuration,
            onTick = {remainingTime ->
                val minutes = remainingTime / 60
                val seconds =  remainingTime % 60
                binding?.timer?.text = String.format("%02d:%02d", minutes, seconds)
            },
            onFinish = {
                binding?.timer?.text = getString(R.string.timer_placeholder)
            }
        )
        return binding?.root
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
