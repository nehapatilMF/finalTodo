package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentEditTaskBinding
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.viewModels.TodoViewModel

class EditTask : Fragment() {

    private var binding : FragmentEditTaskBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        val status = arguments?.getString("status")
        val date = arguments?.getString("date")
        val time = arguments?.getString("time")
        val id = arguments?.getString("id")

        binding?.editTextTitle?.setText(title)
        binding?.editTextDescription?.setText(description)
        binding?.tvTime?.text = time
        binding?.tvDate?.text = date

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_editTask_to_todoMain)
        }
        binding?.tvTime?.setOnClickListener {
            TimePickerUtil.showTimePickerDialog(requireContext()){ selectedTime ->
                binding?.tvTime?.text = selectedTime
            }
        }

        binding?.btnSave?.setOnClickListener {
            val id1 = id.toString()
            val title1 = title.toString()
            val description1 = description.toString()
            val date1 = date.toString()
            val time1 = time.toString()
            val status1 = 2
            if (id != null &&
                title != null &&
                description != null && date != null && time != null
            ) {
                Toast.makeText(requireContext(),"$title,$description, $date, $time, $status", Toast.LENGTH_SHORT).show()
                viewModel.updateTodo(id1, title1, description1, status1, date1, time1)
            }
        }

        binding?.btnDelete?.setOnClickListener {
            if (id != null) {
                viewModel.deleteTodo(id)
            }
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
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val viewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        viewModel.deleteTodoStatus.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                findNavController().navigate(R.id.action_editTask_to_todoMain)
            }
            viewModel.updateTodoStatus.observe(viewLifecycleOwner){ status ->
                if(status == "200"){
                    findNavController().navigate(R.id.action_editTask_to_todoMain)
                }
            }

        }
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,  // An array resource containing your items
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.spinnerStatus?.adapter = adapter
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}