package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.R
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.databinding.FragmentEditTaskBinding

class EditTask : Fragment() {
    private var binding : FragmentEditTaskBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigate_from_newTask_to_todoMain)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
        binding?.tvDate?.setOnClickListener {
            CalenderUtil.showDatePickerDialog (requireContext()){ selectedDate ->
                binding?.tvDate?.text = selectedDate
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTaskBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }

}