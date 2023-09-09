package com.example.todoapp.fragments

import android.app.Dialog
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
import com.example.todoapp.databinding.DialogCustomBackConfirmationBinding
import com.example.todoapp.databinding.DialogDeleteConfirmationBinding
import com.example.todoapp.databinding.FragmentEditTaskBinding
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.viewModels.TodoViewModel

class EditTask : Fragment() {

    private var binding : FragmentEditTaskBinding? = null
    private fun getStatusInt(statusText: String): Int {
        return when (statusText) {
            "Todo" -> 0
            "In Progress" -> 1
            "Done" -> 2
            else -> -1
        }
    }
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
         customDialogForBackButton()
        }
        binding?.tvTime?.setOnClickListener {
            TimePickerUtil.showTimePickerDialog(requireContext()){ selectedTime ->
                binding?.tvTime?.text = selectedTime
            }
        }

        binding?.btnSave?.setOnClickListener {
            val id1 = id.toString()
            val title1 = binding?.editTextTitle?.text.toString()
            val description1 = binding?.editTextDescription?.text.toString()
            val date1 = binding?.tvDate?.text.toString()
            val time1 = binding?.tvTime?.text?.toString()
            val status1 = getStatusInt(binding?.spinnerStatus?.selectedItem.toString())
            if (id != null && time1 != null){
                if(NetworkUtil.isNetworkAvailable(requireContext())) {
                    viewModel.updateTodo(id1, title1, description1, status1, date1, time1)
                    goBack()
                } else{
                    Toast.makeText(requireContext(),getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show()}
            }
        }
        binding?.btnDelete?.setOnClickListener {

            if (id != null) {
                viewModel.deleteTodo(id)
                customDialogForDeleteButton()
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
                goBack()
               // findNavController().navigate(R.id.action_editTask_to_todoMain)
            }
            viewModel.updateTodoStatus.observe(viewLifecycleOwner){ status ->
                if(status == "200"){

                }
            }

        }
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.spinnerStatus?.adapter = adapter
        return binding?.root
    }
    private fun goBack(){
        findNavController().navigate(R.id.action_editTask_to_todoMain)
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(requireContext())
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            findNavController().navigate(R.id.action_editTask_to_todoMain)
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
    private fun customDialogForDeleteButton() {
        val customDialog = Dialog(requireContext())
        val dialogBinding = DialogDeleteConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            findNavController().navigate(R.id.action_editTask_to_todoMain)
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}