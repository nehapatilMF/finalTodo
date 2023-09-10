package com.example.todoapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.DialogCustomBackConfirmationBinding
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.viewModels.TodoViewModel

class NewTask : Fragment() {
    private var binding: FragmentNewTaskBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "New Todo"

        binding?.toolbar?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
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
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val viewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        binding?.btnSave?.setOnClickListener {
            val title = binding?.editTextTitle?.text.toString()
            val description = binding?.editTextDescription?.text.toString()
            val date = binding?.tvDate?.text.toString()
            val time = binding?.tvTime?.text.toString()
            val status = 0
            when{
            !NetworkUtil.isNetworkAvailable(requireContext()) -> {
                DialogUtils.showAutoDismissAlertDialog(
                    requireContext(),
                    getString(R.string.no_internet_connection)
                )
            }else ->
                viewModel.addTodo(title,description,date,time,status)
            }
                  }
        viewModel.addTodoStatus.observe(viewLifecycleOwner){ status ->
            when (status) {
                "201" -> {
                    goBackToTodoMain()
viewModel.todoMessage.observe(viewLifecycleOwner){ msg ->
    val tMsg = msg.toString()
    DialogUtils.showAutoDismissAlertDialog(requireContext(),tMsg)
}
                }
                "422" -> {
                    viewModel.todoMessage.observe(viewLifecycleOwner){ msg ->
                        val errorMsg = msg.toString()
                        DialogUtils.showAutoDismissAlertDialog(requireContext(), errorMsg)
                    }
                }
                else -> {
                    viewModel.todoMessage.observe(viewLifecycleOwner) { msg ->
                        val errorMsg = msg.toString()
                        DialogUtils.showAutoDismissAlertDialog(requireContext(), errorMsg)
                    }
                }
            }
        }


        return binding?.root
    }


    private fun goBackToTodoMain(){
        findNavController().navigate(R.id.navigate_from_newTask_to_todoMain)
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(requireContext())
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            findNavController().navigate(R.id.navigate_from_newTask_to_todoMain)
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