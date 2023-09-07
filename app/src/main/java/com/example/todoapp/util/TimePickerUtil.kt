package com.example.todoapp.util
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimePickerUtil {

    fun showTimePickerDialog(
        context: Context,
        onTimeSelected: (formattedTime: String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val selectedTime = formatTime(selectedHour,selectedMinute, second)
                onTimeSelected(selectedTime)
            },
            hourOfDay,
            minute,
             true// 12-hour format
        )

        timePickerDialog.show()
    }

    private fun formatTime(hourOfDay: Int, minute: Int, second: Int): String {
        val timeFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        return timeFormat.format(calendar.time)
    }
}


