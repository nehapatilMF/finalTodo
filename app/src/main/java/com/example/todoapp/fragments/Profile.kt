package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.LoginViewModel
import com.example.todoapp.viewModels.LogoutViewModel

class Profile : Fragment() {
    private val viewModel: LogoutViewModel by activityViewModels()

    private var binding : FragmentProfileBinding? = null

    private var accessToken : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)


        binding?.personalInformation?.setOnClickListener {
            Toast.makeText(requireContext(),"personalInfoClicked", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profile_to_personalInformation)
        }
        binding?.ChangePassword?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_changePassword)
        }
        binding?.Logout?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {

             viewModel.logout()
            }
        }
        binding?.deleteAccount?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_deleteAccount)
        }

        viewModel.logoutResult.observe(viewLifecycleOwner){ status ->

            if(status == "200"){
                findNavController().navigate(R.id.action_profile_to_login)
            }else{
                val message = "Logout Failed"
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }

        }
        val loginViewModel: LoginViewModel by activityViewModels()

        loginViewModel.getAuthTokens().observe(viewLifecycleOwner){authTokens ->
            accessToken = authTokens.accessToken
            val refreshToken = authTokens.refreshToken
        }
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}