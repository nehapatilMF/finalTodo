package com.example.todoapp.fragments

import android.annotation.SuppressLint
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
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.DialogCustomBackConfirmationBinding
import com.example.todoapp.databinding.FragmentNewTaskBinding
import com.example.todoapp.util.CalenderUtil
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.TimePickerUtil
import com.example.todoapp.viewModels.RefreshTokenViewModel
import com.example.todoapp.viewModels.TodoViewModel

class NewTask : Fragment() {
    private var binding: FragmentNewTaskBinding? = null
    private lateinit var date1 : String
    private lateinit var time1 : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "New Todo"

        binding?.toolbar?.setNavigationOnClickListener{
            customDialogForBackButton()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customDialogForBackButton()

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
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        val rViewModel = ViewModelProvider(this)[RefreshTokenViewModel::class.java]
        val sessionManager = SessionManager(requireContext())

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
                    {viewModel.addTodo(title, description, date1, time1, status)
                        binding?.progressBar?.visibility = View.VISIBLE
                        binding?.newForm?.visibility = View.INVISIBLE
                    }

                }
            }

        viewModel.addTodoStatus.observe(viewLifecycleOwner){ status ->
            if(status == "201"){

                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.newForm?.visibility = View.VISIBLE
                goBackToTodoMain()
                viewModel.todoMessage.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    Toast.makeText(requireContext(),tMsg, Toast.LENGTH_SHORT).show()
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
                    } else{
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
                binding?.newForm?.visibility = View.VISIBLE
                Toast.makeText(requireContext(),status.toString(), Toast.LENGTH_SHORT).show()
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