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
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.interfaces.UserAPI
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModelFactory.UserViewModelFactory
import com.example.todoapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class Login : Fragment() {
    private var binding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextChangeListeners()
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        binding?.buttonLogin1?.visibility = View.VISIBLE
        binding?.buttonLogin?.visibility = View.INVISIBLE

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_login_to_intro)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_login_to_intro)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val sessionManager = SessionManager(requireContext())
        val userAPI: UserAPI? = RetrofitClient.getInstance()?.create(UserAPI::class.java)
        val userRepository = userAPI?.let { UserRepository(it) }
        val viewModelFactory = userRepository?.let { UserViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(UserViewModel::class.java)


        binding?.forgotPassword?.setOnClickListener {
            when{
                !NetworkUtil.isNetworkAvailable(requireContext()) -> DialogUtils.showAutoDismissAlertDialog(requireContext(),getString(R.string.no_internet_connection))
                 else ->  findNavController().navigate(R.id.action_login_to_forgotPassword)
            }
        }

        binding?.buttonLogin?.setOnClickListener {
            val email = binding?.etEmail?.text.toString()
            val password = binding?.etPassword?.text.toString()
            val encodedPassword = Base64.encodeToBase64(password)
            when{
                !NetworkUtil.isNetworkAvailable(requireContext()) -> DialogUtils.showAutoDismissAlertDialog(requireContext(), getString(R.string.no_internet_connection))
                !ValidPatterns.isValidEmail(email) -> {
                    binding?.etEmail?.error = getString(R.string.invalid_or_empty_email_id)
                }
                !ValidPatterns.isValidPassword(password) -> {
                    binding?.etPassword?.error = getString(R.string.invalid_password)
                }
                else -> {
                    viewModel?.login(email,encodedPassword)
                    binding?.progressBar?.visibility = View.VISIBLE
                }
            }
        }

        binding?.signup?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                binding?.progressBar?.visibility = View.GONE
                findNavController().navigate(R.id.action_login_to_register)
            } else {
                binding?.progressBar?.visibility = View.GONE
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel?.name?.observe(viewLifecycleOwner){name ->
            sessionManager.saveName(name.toString())
        }

        viewModel?.getAuthTokens()?.observe(viewLifecycleOwner){ authTokens ->
            val accessToken = authTokens.accessToken
            val refreshToken = authTokens.refreshToken
            sessionManager.clearTokens()
            sessionManager.saveTokens(accessToken,refreshToken)
        }
        viewModel?.status?.observe(viewLifecycleOwner) { status ->
            if (status == "200") {
                findNavController().navigate(R.id.action_login_to_home)
                binding?.progressBar?.visibility = View.VISIBLE

            } else {
                val message = status.toString()
                Snackbar.make(requireView(),message, Snackbar.LENGTH_SHORT).show()
                binding?.progressBar?.visibility = View.GONE
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }
    private fun setupTextChangeListeners() {
        binding?.etEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!ValidPatterns.isValidEmail(email)) {
                    binding?.etEmail?.error = getString(R.string.invalid_or_empty_email_id)
                } else {
                    binding?.etEmail?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding?.etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.etPassword?.error = getString(R.string.invalid_password)
                    binding?.buttonLogin?.visibility = View.INVISIBLE
                    binding?.buttonLogin1?.visibility = View.VISIBLE

                } else {
                    binding?.etPassword?.error = null
                    binding?.buttonLogin?.visibility = View.VISIBLE
                    binding?.buttonLogin1?.visibility = View.INVISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
