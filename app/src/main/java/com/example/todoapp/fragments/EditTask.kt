package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.CalenderUtil
import com.example.todoapp.R
import com.example.todoapp.TimePickerUtil
import com.example.todoapp.databinding.FragmentEditTaskBinding

class EditTask : Fragment() {
    private var binding : FragmentEditTaskBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEditTaskBinding.inflate(layoutInflater)
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
        binding?.buttonDeleteTask?.setOnClickListener{
            findNavController().navigate(R.id.navigate_from_edit_to_todoMain)
        }
        binding?.buttonSaveTask?.setOnClickListener{
            findNavController().navigate(R.id.navigate_from_edit_to_todoMain)
        }
        return binding?.root
    }

}