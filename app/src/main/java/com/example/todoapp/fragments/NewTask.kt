package com.example.todoapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Calendar.getCurrentDate
import com.example.todoapp.Calendar.getFormattedDate
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentNewTaskBinding
import java.util.Calendar

class NewTask : Fragment() {
    private var binding: FragmentNewTaskBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewTaskBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding?.tvDate?.setOnClickListener {
            showDatePickerDialog()
        }
        binding?.tvTime?.setOnClickListener {
            showTimePickerDialog()
        }
        binding?.saveTask?.setOnClickListener {
            findNavController().navigate(R.id.navigate_from_newTask_to_todoMain)
        }
        return binding?.root
    }

    private fun showDatePickerDialog() {
        val calendar = getCurrentDate()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        try {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = "$selectedDayOfMonth-${selectedMonth + 1}-$selectedYear"
                    val formattedDate = getFormattedDate(selectedDate)
                    binding?.tvDate?.text = formattedDate
                },
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        } catch (e: Exception) {
            // Handle the exception, e.g., log it or show an error message
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHourOfDay, selectedMinute ->

                val amPm = if (selectedHourOfDay < 12) "AM" else "PM"
                val formattedHour = if (selectedHourOfDay % 12 == 0) 12 else selectedHourOfDay % 12
                val formattedTime = String.format("%02d:%02d %s", formattedHour, selectedMinute, amPm)
                binding?.tvTime?.text = formattedTime
            },
            hourOfDay,
            minute,
            false // 12-hour format
        )
        timePickerDialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}