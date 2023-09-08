package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentForgotPasswordBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.ForgotPasswordViewModel

class ForgotPassword : Fragment() {
    private val viewModel:ForgotPasswordViewModel by activityViewModels()
    private var binding:FragmentForgotPasswordBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextChangeListeners()


        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.forgot_password_title)
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.navigate_from_forgot_password_to_login)

        }
        binding?.btnSubmit?.setOnClickListener{
            val email =binding?.enterEmail?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                if( ValidPatterns.isValidEmail(email)&& email.isNotEmpty()) {
                    viewModel.email = email
                    viewModel.forgotPasswordRequestOtp(email)
                }else{
                    val message =getString(R.string.required_fields_are_empty)
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
                }
            }    else {

                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
    }
    private fun setupTextChangeListeners() {
        binding?.enterEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!ValidPatterns.isValidEmail(email)) {
                    binding?.enterEmail?.error = getString(R.string.invalid_or_empty_email_id)
                } else {
                    binding?.enterEmail?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        viewModel.forgotPasswordResult.observe(viewLifecycleOwner){ status ->
            if(status == "200") {
                findNavController().navigate(R.id.navigate_to_forgotPasswordOtp)
            }else{

                val message = getString(R.string.invalid_or_empty_email_id)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}

