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
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentPersonalInformationBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModels.PersonalInformationViewModel
import com.example.todoapp.viewModels.RefreshTokenViewModel

class PersonalInformation : Fragment() {
    private var binding: FragmentPersonalInformationBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this)[PersonalInformationViewModel::class.java]

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Personal Information"
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_personalInformation_to_profile)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_personalInformation_to_profile)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding?.PI?.visibility = View.VISIBLE
        binding?.PIEdit?.visibility = View.INVISIBLE

        binding?.buttonEdit?.setOnClickListener {
            binding?.PI?.visibility = View.INVISIBLE
            binding?.PIEdit?.visibility = View.VISIBLE
        }
        binding?.button?.setOnClickListener {
            val name = binding?.tvName1?.text.toString()
            val mobile = binding?.tvMobile1?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {

                viewModel.updateUser(name,mobile)
                binding?.progressBar?.visibility = View.VISIBLE
            } else {
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPersonalInformationBinding.inflate(layoutInflater,container,false)
        setupTextChangeListeners()
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        val viewModel = ViewModelProvider(this)[PersonalInformationViewModel::class.java]
        val rViewModel = ViewModelProvider(this)[RefreshTokenViewModel::class.java]
        val sessionManager = SessionManager(requireContext())
        viewModel.get()

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.updateUserResult.observe(viewLifecycleOwner){ status ->
            if(status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.profile)
                val name = binding?.tvName1?.text.toString()

                sessionManager.saveName(name)

                viewModel.msg.observe(viewLifecycleOwner) { msg ->
                    Toast.makeText(requireContext(), msg.toString(), Toast.LENGTH_SHORT).show()
                    binding?.progressBar?.visibility = View.GONE
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
            }
        }

        viewModel.getResult.observe(viewLifecycleOwner){status ->
            if(status == "200"){
                sessionManager.clearName()
                binding?.progressBar?.visibility = View.INVISIBLE
                viewModel.name.observe(viewLifecycleOwner) { name ->
                    Constants.name = name.toString()
                    sessionManager.saveName(name)
                    binding?.tvName?.text = name.toString()

                    binding?.tvName1?.setText( binding?.tvName?.text.toString())
                }
                viewModel.email.observe(viewLifecycleOwner) { email ->
                    Constants.emailP = email.toString()
                    binding?.tvEmail?.text = email.toString()
                    binding?.tvEmail1?.text = binding?.tvEmail?.text.toString()
                }
                viewModel.mobile.observe(viewLifecycleOwner) { mobile ->
                    Constants.mobile = mobile.toString()
                    binding?.tvMobile?.text = mobile.toString()
                    binding?.tvMobile1?.setText( binding?.tvMobile?.text.toString())
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
                    Toast.makeText(requireContext(),status.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        return binding?.root
    }

    private fun setupTextChangeListeners() {
        binding?.tvMobile1?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding?.tvMobile1?.error = getString(R.string.mobile_number_required)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val mobileNumber = s.toString()
                if (!ValidPatterns.isValidNumber(mobileNumber)) {
                    binding?.tvMobile1?.error = getString(R.string.invalid_mobile_number)
                } else {
                    binding?.tvMobile1?.error = null // Clear error message
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


