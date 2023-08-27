package com.example.todoapp.util

import android.os.CountDownTimer

object TimerUtil {
    private var countDownTimer: CountDownTimer? = null
    fun startTimer(
        duration: Long,
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ) {
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                onTick(secondsRemaining)
            }

            override fun onFinish() {
                onFinish()
            }
        }
        countDownTimer?.start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
    }
}

