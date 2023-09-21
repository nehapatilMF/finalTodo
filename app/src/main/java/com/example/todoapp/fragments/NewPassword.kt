package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.viewModelFactory.UserViewModelFactory
import com.example.todoapp.base64.Base64
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.databinding.FragmentNewPasswordBinding
import com.example.todoapp.interfaces.UserAPI
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class NewPassword : Fragment() {
    private var binding : FragmentNewPasswordBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.forgot_password)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_newPassword_to_login)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_newPassword_to_login)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setupTextChangeListeners()
        binding?.btnNext?.visibility = View.INVISIBLE
        binding?.btnNext1?.visibility =View.VISIBLE

        val userAPI: UserAPI? = RetrofitClient.getInstance()?.create(UserAPI::class.java)
        val userRepository = userAPI?.let { UserRepository(it) }
        val viewModelFactory = userRepository?.let { UserViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(UserViewModel::class.java)
        val email = Constants.userEmail.toString()

        val newOtp = Constants.newOtp.toString()
        binding?.btnNext?.setOnClickListener {
            val password  = binding?.etNewPassword?.text.toString()
            val confirmPassword = binding?.etConfirmPassword?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                val result = password.compareTo(confirmPassword)
                if(result == 0 ){
                    val encodedPassword = Base64.encodeToBase64(password)
                    viewModel?.resetPassword(newOtp,email,encodedPassword)
                    binding?.progressBar?.visibility = View.VISIBLE
                }else{
                    binding?.etNewPassword?.error = getString(R.string.no_match)
                }
            }else{
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_newPassword_to_login)
            }else{
                binding?.progressBar?.visibility = View.INVISIBLE
                val message = status.toString()
               Snackbar.make(requireView(),message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupTextChangeListeners() {
        binding?.etNewPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.etNewPassword?.error = getString(R.string.invalid_password)
                    binding?.btnNext?.visibility = View.INVISIBLE
                    binding?.btnNext1?.visibility =View.VISIBLE

                } else {
                    binding?.etNewPassword?.error = null // Clear error message
                    binding?.btnNext?.visibility = View.VISIBLE
                    binding?.btnNext1?.visibility =View.INVISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPasswordBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}