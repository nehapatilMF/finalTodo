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

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHourOfDay, selectedMinute ->
                val formattedTime = formatTime(selectedHourOfDay, selectedMinute)
                onTimeSelected(formattedTime)
            },
            hourOfDay,
            minute,
            false // 12-hour format
        )
        timePickerDialog.show()
    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val amPm = if (hourOfDay < 12) "AM" else "PM"
        val formattedHour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, formattedHour)
        calendar.set(Calendar.MINUTE, minute)
        return timeFormat.format(calendar.time)
    }
}






