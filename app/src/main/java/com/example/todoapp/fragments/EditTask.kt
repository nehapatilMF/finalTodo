package com.example.todoapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.DialogCustomBackConfirmationBinding
import com.example.todoapp.databinding.DialogDeleteConfirmationBinding
import com.example.todoapp.databinding.FragmentEditTaskBinding
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.viewModels.RefreshTokenViewModel
import com.example.todoapp.viewModels.TodoViewModel

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

        val viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_editTask_to_todoMain)
            }
        }
        binding?.toolbar?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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


        binding?.tvTime?.setOnClickListener {
            TimePickerUtil.showTimePickerDialog(requireContext()) { selectedTime ->
                binding?.tvTime?.text = selectedTime
            }
        }

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
                        viewModel.updateTodo(id1, title1, description1, status1, date1, time1)
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
                    viewModel.deleteTodo(id)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTaskBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        val rViewModel = ViewModelProvider(this)[RefreshTokenViewModel::class.java]
        val sessionManager = SessionManager(requireContext())

        viewModel.deleteTodoStatus.observe(viewLifecycleOwner) { status ->
            if (status == "204") {
                goBack()
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.editForm?.visibility = View.VISIBLE

                viewModel.todoMessage.observe(viewLifecycleOwner) { msg ->
                    val errorMsg = msg.toString()
                    Toast.makeText(requireContext(),errorMsg, Toast.LENGTH_SHORT).show()
                }
            }else if(status.toString() == "Unauthenticated.") {
                val refreshToken1 = SessionManager(requireContext()).getRefreshToken()
                if(refreshToken1?.isNotBlank() == true){
                    rViewModel.refreshToken(refreshToken1.toString())
                    binding?.progressBar?.visibility = View.INVISIBLE
                }else{
                    DialogUtils.showAutoDismissAlertDialog(requireContext(),
                        getString(R.string.your_session_has_expired))
                    sessionManager.clearTokens()
                    Constants.clearAccessToken()
                    Constants.clearRefreshToken()
                    findNavController().navigate(R.id.navigate_to_intro)
                }
                rViewModel.result.observe(viewLifecycleOwner) { status1 ->
                    if (status1 == "true") {
                        sessionManager.clearTokens()
                        Constants.clearAccessToken()
                        rViewModel.getAuthTokens().observe(viewLifecycleOwner) { authTokens ->
                            val accessToken = authTokens.accessToken
                            Constants.accessToken = accessToken
                            val refreshToken = authTokens.refreshToken
                            Constants.refreshToken = refreshToken
                            sessionManager.saveTokens(accessToken, refreshToken)
                        }
                    }
                }
            }else {
                Toast.makeText(requireContext(),status.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.updateTodoStatus.observe(viewLifecycleOwner) { status ->
            if (status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.editForm?.visibility = View.VISIBLE

                viewModel.todoMessage.observe(viewLifecycleOwner) { msg ->
                    val toastMsg = msg.toString()
                        Toast.makeText(requireContext(),toastMsg, Toast.LENGTH_SHORT).show()
                                        goBack()
                }
            }else if(status.toString() == "Unauthenticated.") {
                val refreshToken1 = SessionManager(requireContext()).getRefreshToken()!!

                    rViewModel.refreshToken(refreshToken1)
                binding?.progressBar?.visibility = View.INVISIBLE
                rViewModel.result.observe(viewLifecycleOwner) { status1 ->
                    if (status1 == "200") {
                        sessionManager.clearTokens()
                        Constants.clearAccessToken()
                        rViewModel.getAuthTokens().observe(viewLifecycleOwner) { authTokens ->
                            val accessToken = authTokens.accessToken
                            Constants.accessToken = accessToken
                            val refreshToken = authTokens.refreshToken
                            Constants.refreshToken = refreshToken
                            sessionManager.saveTokens(accessToken, refreshToken)
                        }
                    }else{
                        DialogUtils.showAutoDismissAlertDialog(requireContext(),
                            getString(R.string.your_session_has_expired))
                        sessionManager.clearTokens()
                        Constants.clearAccessToken()
                        Constants.clearRefreshToken()
                        findNavController().navigate(R.id.navigate_to_intro)
                    }
                }
            }else {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.editForm?.visibility = View.VISIBLE
                Toast.makeText(requireContext(),status.toString(), Toast.LENGTH_SHORT).show()
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