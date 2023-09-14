package com.example.todoapp.fragments

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
import com.example.todoapp.databinding.FragmentPersonalInformationBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.viewModels.PersonalInformationViewModel
import com.example.todoapp.viewModels.RefreshTokenViewModel

class PersonalInformation : Fragment() {
    private var binding: FragmentPersonalInformationBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Personal Information"
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_personalInformation_to_todoMain)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_personalInformation_to_todoMain)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPersonalInformationBinding.inflate(layoutInflater,container,false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        val viewModel = ViewModelProvider(this)[PersonalInformationViewModel::class.java]
        val rViewModel = ViewModelProvider(this)[RefreshTokenViewModel::class.java]
        val sessionManager = SessionManager(requireContext())
        viewModel.get()
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.getResult.observe(viewLifecycleOwner){status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                viewModel.name.observe(viewLifecycleOwner) { name ->
                    Constants.name = name.toString()
                    binding?.tvName?.text = name.toString()
                }
                viewModel.email.observe(viewLifecycleOwner) { email ->
                    Constants.emailP = email.toString()
                    binding?.tvEmail?.text = email.toString()
                }
                viewModel.mobile.observe(viewLifecycleOwner) { mobile ->
                    Constants.mobile = mobile.toString()
                    binding?.tvMobile?.text = mobile.toString()
                }
            }else if(status.toString() == "Unauthenticated.") {
                 val refreshToken1 = SessionManager(requireContext()).getRefreshToken()!!

                    rViewModel.refreshToken(refreshToken1)

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
                    Toast.makeText(requireContext(),status.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}


