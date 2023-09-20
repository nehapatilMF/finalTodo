package com.example.todoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.todoapp.base64.Base64
import com.example.todoapp.databinding.FragmentNewPasswordBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.NewPasswordViewModel

class NewPassword : Fragment() {
    private var binding : FragmentNewPasswordBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextChangeListeners()
        binding?.btnNext?.visibility = View.INVISIBLE
        binding?.btnNext1?.visibility =View.VISIBLE
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.forgot_password)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.navigate_to_login)
                    }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigate_to_login)
                           }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }
    private fun setupTextChangeListeners() {
        binding?.etNewPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.etNewPassword?.error = "invalid password"
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
        val viewModel = ViewModelProvider(this)[NewPasswordViewModel::class.java]
        val email = Constants.userEmail.toString()

        val newOtp = Constants.newOtp.toString()
        binding?.btnNext?.setOnClickListener {
            val password  = binding?.etNewPassword?.text.toString()
            val confirmPassword = binding?.etConfirmPassword?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                        val result = password.compareTo(confirmPassword)
                        if(result == 0 ){
                            val encodedPassword = Base64.encodeToBase64(password)
                            viewModel.resetPassword(newOtp,email,encodedPassword)
                            binding?.progressBar?.visibility = View.VISIBLE
                        }else{
                            binding?.etNewPassword?.error = getString(R.string.no_match)
                       }
            }else{
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        viewModel.resetPasswordResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.navigate_from_newPassword_to_login)
            }else{
                binding?.progressBar?.visibility = View.INVISIBLE
                val message = status.toString()
                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}