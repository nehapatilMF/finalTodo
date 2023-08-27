package com.example.todoapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentOtpBinding
import com.example.todoapp.util.TimerUtil
import okhttp3.internal.toLongOrDefault

class Otp : Fragment() {
    private var binding : FragmentOtpBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val timerDuration : Long = getString(R.string.timer_duration).toLongOrNull()?:0L
        Log.d("Timer", "Timer Duration: $timerDuration")
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
    override fun onDestroy() {
        TimerUtil.cancelTimer()
        super.onDestroy()
        binding = null
    }
}
