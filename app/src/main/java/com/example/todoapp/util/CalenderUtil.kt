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
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }
    private fun getFormattedDate(date: String): String {
       val myFormat = "dd-MMM-yyyy" // Define the date pattern you want
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val inputFormat = SimpleDateFormat(myFormat, Locale.getDefault())

        val parsedDate = simpleDateFormat.parse(date)
        return parsedDate?.let { inputFormat.format(it).toString() } ?: ""
    }

    fun convertDateFormat(inputDate : String):String{
        val inputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)

    }
    fun reConvertDateFormat(inputDate : String):String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)

    }

}



