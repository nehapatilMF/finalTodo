package com.example.todoapp.fragments

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.R
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.util.NetworkUtil

class NewTask : Fragment() {
    private var binding: FragmentNewTaskBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigate_from_newTask_to_todoMain)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding?.tvDate?.setOnClickListener {
            CalenderUtil.showDatePickerDialog (requireContext()){ selectedDate ->
                binding?.tvDate?.text = selectedDate
                binding?.tvDate?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
        binding?.tvTime?.setOnClickListener {
            TimePickerUtil.showTimePickerDialog(requireContext()){ selectedTime ->
                binding?.tvTime?.text = selectedTime
                binding?.tvTime?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }



        binding?.btnSave?.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable(requireContext())){
            findNavController().navigate(R.id.navigate_from_newTask_to_todoMain)
        }else{
            Toast.makeText(requireContext(),getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}