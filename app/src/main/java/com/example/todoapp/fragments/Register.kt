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
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.interfaces.UserAPI
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModelFactory.UserViewModelFactory
import com.example.todoapp.viewModels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class Register : Fragment() {
    private var binding: FragmentRegisterBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = ""

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_register_to_intro)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_register_to_intro)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setupTextChangeListeners()

        binding?.buttonNext?.visibility = View.INVISIBLE
        binding?.buttonNext1?.visibility = View.VISIBLE

        val userAPI: UserAPI? = RetrofitClient.getInstance()?.create(UserAPI::class.java)
        val userRepository = userAPI?.let { UserRepository(it) }
        val viewModelFactory = userRepository?.let { UserViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(UserViewModel::class.java)
        binding?.login?.setOnClickListener {
            handleLogin()
        }
        binding?.buttonNext?.setOnClickListener {
            val userName = binding?.editTextUserName?.text.toString()
            val mobile = binding?.editTextMobileNumber?.text.toString()
            val email = binding?.editTextEmail?.text.toString()
            val password  = binding?.editTextPassword?.text.toString()
            val confirmPassword = binding?.editTextConfirmPassword?.text.toString()
            Constants.userEmail = email
            when {
                !NetworkUtil.isNetworkAvailable(requireContext()) -> showErrorDialog(getString(R.string.no_internet_connection))
                !ValidPatterns.isValidPassword(password) -> {
                    Snackbar.make(requireView(),
                        getString(R.string.invalid_password), Snackbar.LENGTH_SHORT).show()
                }
                !ValidPatterns.isValidNumber(mobile) -> {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.mobile_number_must_be_10_digit),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                password != confirmPassword -> {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.no_match),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val encodedPassword = Base64.encodeToBase64(password)
                    viewModel?.signup(userName, mobile, email, encodedPassword)
                    binding?.progressBar?.visibility = View.VISIBLE
                    binding?.register?.visibility = View.INVISIBLE
                }
            }

        }

        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                binding?.register?.visibility = View.VISIBLE
                findNavController().navigate(R.id.action_register_to_otp )
                viewModel.msg.observe(viewLifecycleOwner){msg ->
                    val message = msg.toString()
                    Toast.makeText(
                        requireContext(),
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{  binding?.progressBar?.visibility = View.INVISIBLE
                binding?.register?.visibility = View.VISIBLE
                val message = status.toString()
                Snackbar.make(requireView(),message,Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel?.otpResult?.observe(viewLifecycleOwner){ otp ->
            val otp1  =otp.toString()
            Constants.userOtp = otp1
        }
    }

    private fun showErrorDialog(message: String) {
        DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
    }
    private fun setupTextChangeListeners() {
        binding?.editTextMobileNumber?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding?.editTextMobileNumber?.error = getString(R.string.mobile_number_required)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val mobileNumber = s.toString()
                if (!ValidPatterns.isValidNumber(mobileNumber)) {
                    binding?.buttonNext?.visibility = View.INVISIBLE
                    binding?.buttonNext1?.visibility = View.VISIBLE
                } else {
                    binding?.editTextMobileNumber?.error = null // Clear error message
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding?.editTextEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!ValidPatterns.isValidEmail(email)) {
                    //  binding?.editTextEmail?.error = getString(R.string.invalid_or_empty_email_id)
                    binding?.buttonNext?.visibility = View.INVISIBLE
                    binding?.buttonNext1?.visibility = View.VISIBLE
                } else {
                    binding?.editTextEmail?.error = null // Clear error message
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding?.editTextPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (!ValidPatterns.isValidPassword(password)) {
                    binding?.editTextPassword?.error =getString(R.string.invalid_password)
                    binding?.buttonNext?.visibility = View.INVISIBLE
                    binding?.buttonNext1?.visibility = View.VISIBLE
                } else {
                    binding?.editTextPassword?.error = null // Clear error message
                    binding?.buttonNext?.visibility = View.VISIBLE
                    binding?.buttonNext1?.visibility = View.INVISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun handleLogin() {
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            findNavController().navigate(R.id.action_register_to_login)
        } else {
            val message = getString(R.string.no_internet_connection)
            DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}
