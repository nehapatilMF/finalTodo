package com.example.todoapp

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

object Calendar{
    private val calendar = Calendar.getInstance()

    fun getFormattedDate(date: String): String {
        val myFormat = "dd-MMM-yyyy" // Define the date pattern you want
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        val parsedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(date)

        return parsedDate?.let { simpleDateFormat.format(it).toString() } ?: ""
    }

    fun getCurrentDate(): Calendar {
        return calendar
    }
}
