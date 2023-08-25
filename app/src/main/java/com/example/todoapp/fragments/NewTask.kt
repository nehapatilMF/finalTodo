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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}