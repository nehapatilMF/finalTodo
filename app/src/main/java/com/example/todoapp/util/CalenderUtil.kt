package com.example.todoapp.util

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalenderUtil{
    private val calendar = Calendar.getInstance()
    fun showDatePickerDialog(context: Context, onDateSelected: (selectedDate: String) -> Unit) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth-${selectedMonth + 1}-$selectedYear"
                val formattedDate = getFormattedDate(selectedDate)
                onDateSelected(formattedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }
    private fun getFormattedDate(date: String): String {
       val myFormat = "yyyy-MM-dd" // Define the date pattern you want
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        val parsedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(date)

        return parsedDate?.let { simpleDateFormat.format(it).toString() } ?: ""
    }


}



