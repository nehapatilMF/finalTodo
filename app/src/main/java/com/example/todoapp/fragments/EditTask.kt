package com.example.todoapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.DialogCustomBackConfirmationBinding
import com.example.todoapp.databinding.DialogDeleteConfirmationBinding
import com.example.todoapp.databinding.FragmentEditTaskBinding
import com.example.todoapp.interfaces.TodoAPI
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.viewModelFactory.TodoViewModelFactory
import com.example.todoapp.viewModels.TodoViewModel
import com.google.android.material.snackbar.Snackbar

class EditTask : Fragment() {
    private lateinit var date1 : String
    private lateinit var time1 : String
    private var binding: FragmentEditTaskBinding? = null
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
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_editTask_to_home)
            }
        }
        binding?.toolbar?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
       // val status = arguments?.getString("status")
        val date = arguments?.getString("date")
        val time = arguments?.getString("time")
        val id = arguments?.getString("id")

        binding?.editTextTitle?.setText(title)
        binding?.editTextDescription?.setText(description)
        binding?.tvTime?.text = time
        binding?.tvDate?.text = date


        binding?.tvTime?.setOnClickListener {
            TimePickerUtil.showTimePickerDialog(requireContext()) { selectedTime ->
                binding?.tvTime?.text = selectedTime
            }
        }



        val sessionManager = SessionManager(requireContext())
        val todoAPI: TodoAPI? = RetrofitClient.getInstance()?.create(TodoAPI::class.java)
        val todoRepository = todoAPI?.let { TodoRepository(it) }
        val viewModelFactory = todoRepository?.let { TodoViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(TodoViewModel::class.java)

        binding?.btnSave?.setOnClickListener {
            val id1 = id.toString()
            val title1 = binding?.editTextTitle?.text.toString()
            val description1 = binding?.editTextDescription?.text.toString()
            val status1 = getStatusInt(binding?.spinnerStatus?.selectedItem.toString())
            convertDateTime()
            if (id != null) {
                when {
                    !NetworkUtil.isNetworkAvailable(requireContext()) -> {
                        DialogUtils.showAutoDismissAlertDialog(
                            requireContext(),
                            getString(R.string.no_internet_connection)
                        )
                    }

                    else -> {
                        viewModel?.updateTodo(id1, title1, description1, status1, date1, time1)
                        binding?.progressBar?.visibility = View.VISIBLE
                        binding?.editForm?.visibility = View.INVISIBLE
                    }
                }
            }
        }



        binding?.btnDelete?.setOnClickListener {
            if (id != null) {
                val customDialog = Dialog(requireContext())
                val dialogBinding = DialogDeleteConfirmationBinding.inflate(layoutInflater)
                customDialog.setContentView(dialogBinding.root)
                customDialog.setCanceledOnTouchOutside(false)
                dialogBinding.tvYes.setOnClickListener {
                    viewModel?.deleteTodo(id)
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.editForm?.visibility = View.INVISIBLE

                    goBack()
                    customDialog.dismiss()
                }
                dialogBinding.tvNo.setOnClickListener {
                    customDialog.dismiss()
                }
                customDialog.show()
            }
        }

        binding?.tvDate?.setOnClickListener {
            CalenderUtil.showDatePickerDialog(requireContext()) { selectedDate ->
                binding?.tvDate?.text = selectedDate
            }
        }


        viewModel?.status?.observe(viewLifecycleOwner) { status ->
            if (status == "204") {
                goBack()
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.editForm?.visibility = View.VISIBLE

                viewModel.todoMessage.observe(viewLifecycleOwner) { msg ->
                    val errorMsg = msg.toString()
                    Snackbar.make(requireView(),errorMsg, Snackbar.LENGTH_SHORT).show()
                }

            }else {
                Snackbar.make(requireView(),status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel?.status?.observe(viewLifecycleOwner) { status ->
            if (status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.editForm?.visibility = View.VISIBLE

                viewModel.todoMessage.observe(viewLifecycleOwner) { msg ->
                    val toastMsg = msg.toString()
                    Snackbar.make(requireView(),toastMsg, Snackbar.LENGTH_SHORT).show()
                    goBack()
                }

            }else {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.editForm?.visibility = View.VISIBLE
                Snackbar.make(requireView(),status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.spinnerStatus?.adapter = adapter


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTaskBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }
    private fun goBack(){
        findNavController().navigate(R.id.action_editTask_to_home)
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(requireContext())
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {
            findNavController().navigate(R.id.action_editTask_to_home)
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
            time.let { it1 -> TimePickerUtil.convertTime(it1) }.toString()
        }else{
            time
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}