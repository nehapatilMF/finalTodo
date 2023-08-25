package com.example.todoapp.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.CalenderUtil
import com.example.todoapp.R
import com.example.todoapp.TimePickerUtil
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
            CalenderUtil.showDatePickerDialog (requireContext()){ selectedDate ->
                binding?.tvDate?.text = selectedDate
            }
        }
        binding?.tvTime?.setOnClickListener {
            TimePickerUtil.showTimePickerDialog(requireContext()){ selectedTime ->
                binding?.tvTime?.text = selectedTime
            }
        }
        binding?.saveTask?.setOnClickListener {
            findNavController().navigate(R.id.navigate_from_newTask_to_todoMain)
        }
        return binding?.root
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