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
import com.example.todoapp.R
import com.example.todoapp.base64.Base64
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentChangePasswordBinding
import com.example.todoapp.interfaces.TodoAPI
import com.example.todoapp.interfaces.UserAPI
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModelFactory.TodoViewModelFactory
import com.example.todoapp.viewModelFactory.UserViewModelFactory
import com.example.todoapp.viewModels.TodoViewModel
import com.example.todoapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class ChangePassword : Fragment() {
    private var binding : FragmentChangePasswordBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextChangeListeners()

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Change Password"

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_changePassword_to_profile)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_changePassword_to_profile)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding?.btnNext?.visibility = View.INVISIBLE
        binding?.btnNext1?.visibility = View.VISIBLE

        val todoAPI: TodoAPI? = RetrofitClient.getInstance()?.create(TodoAPI::class.java)
        val todoRepository = todoAPI?.let { TodoRepository(it) }
        val viewModelFactory = todoRepository?.let { TodoViewModelFactory(it) }
        val viewModel =
            viewModelFactory?.let { ViewModelProvider(this, it) }?.get(TodoViewModel::class.java)

        binding?.btnNext?.setOnClickListener {
            val oldPassword = binding?.etOldPassword?.text.toString()
            val newPassword = binding?.etNewPassword?.text.toString()
            val confirmPassword = binding?.etConfirmPassword?.text.toString()
            val encodedOldPassword = Base64.encodeToBase64(oldPassword)
            val encodedNewPassword = Base64.encodeToBase64(newPassword)
            when {
                !NetworkUtil.isNetworkAvailable(requireContext()) -> {
                    showErrorDialog(getString(R.string.no_internet_connection))
                    binding?.progressBar?.visibility = View.INVISIBLE
                }

                newPassword != confirmPassword -> binding?.etNewPassword?.error =
                    getString(R.string.password_don_t_match)

                else -> {
                    viewModel?.changePassword(encodedOldPassword, encodedNewPassword)
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.changePassword?.visibility = View.INVISIBLE
                }
            }
        }

        binding?.btnNext?.setOnClickListener {
            val oldPassword = binding?.etOldPassword?.text.toString()
            val newPassword = binding?.etNewPassword?.text.toString()
            val confirmPassword = binding?.etConfirmPassword?.text.toString()
            val encodedOldPassword = Base64.encodeToBase64(oldPassword)
            val encodedNewPassword = Base64.encodeToBase64(newPassword)
            when {
                !NetworkUtil.isNetworkAvailable(requireContext()) -> {
                    showErrorDialog(getString(R.string.no_internet_connection))
                    binding?.progressBar?.visibility = View.INVISIBLE
                }
                newPassword != confirmPassword -> binding?.etNewPassword?.error = getString(R.string.password_don_t_match)
                else -> {
                    viewModel?.changePassword(encodedOldPassword, encodedNewPassword)
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.changePassword?.visibility = View.INVISIBLE
                }
            }
        }

        viewModel?.status?.observe(viewLifecycleOwner) { status ->
            if (status == "200") {
                findNavController().navigate(R.id.action_changePassword_to_login)
                viewModel.todoMessage.observe(viewLifecycleOwner) { msg ->
                    val message = msg.toString()
                    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                    binding?.progressBar?.visibility = View.INVISIBLE
                    binding?.changePassword?.visibility = View.VISIBLE

                }
                //    } else if (status.toString() == "Unauthenticated.") {
            } else {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.changePassword?.visibility = View.VISIBLE
                Snackbar.make(requireView(), status.toString(), Snackbar.LENGTH_SHORT).show()
            }
       //     refreshToken()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }
    private fun refreshToken() {
        val userAPI: UserAPI? = RetrofitClient.getInstance()?.create(UserAPI::class.java)
        val userRepository = userAPI?.let { UserRepository(it) }
        val userViewModelFactory = userRepository?.let { UserViewModelFactory(it) }
        val refreshViewModel = userViewModelFactory?.let { ViewModelProvider(this, it) }?.get(
            UserViewModel::class.java
        )
        val sessionManager = SessionManager(requireContext())
        val refreshToken1 = SessionManager(requireContext()).getRefreshToken()!!
        refreshViewModel?.refreshToken(refreshToken1)
        binding?.progressBar?.visibility = View.INVISIBLE

        refreshViewModel?.status?.observe(viewLifecycleOwner) { status1 ->
            if (status1 == "200") {
                sessionManager.clearTokens()
                refreshViewModel.getAuthTokens().observe(viewLifecycleOwner) { authTokens ->
                    val accessToken = authTokens.accessToken
                    val refreshToken = authTokens.refreshToken
                    sessionManager.saveTokens(accessToken, refreshToken)
                    //  val oldPassword = binding?.etOldPassword?.text.toString()
                    // val newPassword = binding?.etNewPassword?.text.toString()
                    //val encodedOldPassword = Base64.encodeToBase64(oldPassword)
                    //val encodedNewPassword = Base64.encodeToBase64(newPassword)
                    //   viewModel.changePassword(encodedOldPassword,encodedNewPassword)
                }
            } else {
                DialogUtils.showAutoDismissAlertDialog(
                    requireContext(),
                    getString(R.string.your_session_has_expired)
                )
                sessionManager.clearTokens()
                //this.findNavController().navigate(R.id.action_changePassword_to_login)
            }
        }
    }
    private fun setupTextChangeListeners() {
        binding?.etNewPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
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


