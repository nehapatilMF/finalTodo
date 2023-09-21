package com.example.todoapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.DialogCustomBackConfirmationBinding
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.interfaces.TodoAPI
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.viewModelFactory.TodoViewModelFactory
import com.example.todoapp.viewModels.TodoViewModel
import com.google.android.material.snackbar.Snackbar

class NewTask : Fragment() {
    private var binding: FragmentNewTaskBinding? = null
    private lateinit var date1 : String
    private lateinit var time1 : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "New Todo"
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        binding?.toolbar?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customDialogForBackButton()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val sessionManager = SessionManager(requireContext())
        val todoAPI: TodoAPI? = RetrofitClient.getInstance()?.create(TodoAPI::class.java)
        val todoRepository = todoAPI?.let { TodoRepository(it) }
        val viewModelFactory = todoRepository?.let { TodoViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(TodoViewModel::class.java)


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
            val title = binding?.editTextTitle?.text.toString()
            val description = binding?.editTextDescription?.text.toString()
            convertDateTime()
            val status = 0
            when {
                !NetworkUtil.isNetworkAvailable(requireContext()) -> {
                    DialogUtils.showAutoDismissAlertDialog(
                        requireContext(),
                        getString(R.string.no_internet_connection)
                    )
                }
                else ->
                {viewModel?.addTodo(title, description, date1, time1, status)
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.newForm?.visibility = View.INVISIBLE
                }

            }
        }

        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "201"){

                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.newForm?.visibility = View.VISIBLE
                goBackToTodoMain()
                viewModel.todoMessage.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    Toast.makeText(requireContext(),tMsg, Toast.LENGTH_SHORT).show()
                }

            }else {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.newForm?.visibility = View.VISIBLE
                Snackbar.make(requireView(),status.toString(), Snackbar.LENGTH_SHORT).show()
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


    private fun goBackToTodoMain(){
        findNavController().navigate(R.id.action_newTask_to_home)
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(requireContext())
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            findNavController().navigate(R.id.action_newTask_to_home)
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
    private fun convertDateTime(){
        val date = binding?.tvDate?.text.toString()
        date1 = if(date.isNotBlank()){
            CalenderUtil.convertDateFormat(date)
        } else{
            date
        }
        val time = binding?.tvTime?.text.toString()
        time1 = if(time.isNotBlank()){
            TimePickerUtil.convertTime(time)
        }else{
            time
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}