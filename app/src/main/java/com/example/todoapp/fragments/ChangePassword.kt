package com.example.todoapp.fragments

import android.annotation.SuppressLint
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
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentChangePasswordBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.ChangePasswordViewModel
import com.example.todoapp.viewModels.RefreshTokenViewModel
import com.google.android.material.snackbar.Snackbar

class ChangePassword : Fragment() {
    private var binding : FragmentChangePasswordBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Change Password"

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.navigate_to_profile)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigate_to_profile)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding?.btnNext?.visibility = View.INVISIBLE
        binding?.btnNext1?.visibility = View.VISIBLE
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(layoutInflater,container,false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        setupTextChangeListeners()

        val viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        val rViewModel = ViewModelProvider(this)[RefreshTokenViewModel::class.java]
        val sessionManager = SessionManager(requireContext())

        binding?.btnNext?.setOnClickListener {
            val oldPassword = binding?.etOldPassword?.text.toString()
            val newPassword = binding?.etNewPassword?.text.toString()
            val confirmPassword = binding?.etConfirmPassword?.text.toString()
            val encodedOldPassword = Base64.encodeToBase64(oldPassword)
            val encodedNewPassword = Base64.encodeToBase64(newPassword)
            when            {
                !NetworkUtil.isNetworkAvailable(requireContext()) -> {
                    showErrorDialog(getString(R.string.no_internet_connection))
                    binding?.progressBar?.visibility = View.INVISIBLE
                }
                newPassword != confirmPassword -> binding?.etNewPassword?.error = "Password don't match"
                else ->  {viewModel.changePassword(encodedOldPassword,encodedNewPassword)
                binding?.progressBar?.visibility = View.VISIBLE
                    binding?.changePassword?.visibility = View.INVISIBLE
                }

            }
        }

        viewModel.result.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                findNavController().navigate(R.id.action_changePassword_to_login)
                viewModel.msg.observe(viewLifecycleOwner) {msg->
                    val message = msg.toString()
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    SessionManager(requireContext()).clearTokens()
                    Constants.clearAccessToken()
                    binding?.progressBar?.visibility = View.INVISIBLE
                    binding?.changePassword?.visibility = View.VISIBLE

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
                    this.findNavController().navigate(R.id.navigate_to_intro)
                }
                }
            }else {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.changePassword?.visibility = View.VISIBLE
                Snackbar.make(requireView(),status.toString(), Snackbar.LENGTH_SHORT).show()


            }

        }
        return binding?.root
    }
    private fun setupTextChangeListeners() {
        binding?.etNewPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.etNewPassword?.error = "invalid password."
                    binding?.btnNext?.visibility = View.INVISIBLE
                    binding?.btnNext1?.visibility = View.VISIBLE

                } else {
                    binding?.etNewPassword?.error = null // Clear error message
                    binding?.btnNext?.visibility = View.VISIBLE
                    binding?.btnNext1?.visibility = View.INVISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
        binding?.etOldPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.etNewPassword?.error = "invalid password."
                } else {
                    binding?.etOldPassword?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
    }
    private fun showErrorDialog(message: String) {
        DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}


