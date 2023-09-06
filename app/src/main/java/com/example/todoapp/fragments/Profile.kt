package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.DeleteViewModel
import com.example.todoapp.viewModels.LoginViewModel
import com.example.todoapp.viewModels.LogoutViewModel

class Profile : Fragment() {


    private var binding : FragmentProfileBinding? = null
    private val logoutViewModel: LogoutViewModel by activityViewModels()
    private val deleteViewModel: DeleteViewModel by activityViewModels()

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
             logoutViewModel.logout()
            }
        }
        binding?.deleteAccount?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                deleteViewModel.deleteUser()
            }
        }

        logoutViewModel.logoutResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                // When adding the profile fragment, give it a unique tag

                findNavController().navigate(R.id.action_profile_to_login)
            }else{
                val message = "Logout Failed"
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }

        }

        deleteViewModel.deleteUserResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                // When adding the profile fragment, give it a unique tag

                findNavController().navigate(R.id.action_profile_to_intro)
            }else{
                val message = "user not deleted."
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }

        }
        val loginViewModel: LoginViewModel by activityViewModels()

        loginViewModel.getAuthTokens().observe(viewLifecycleOwner){authTokens ->
            val accessToken = authTokens.accessToken
            Constants.accessToken = accessToken
            val refreshToken = authTokens.refreshToken
            Constants.refreshToken = refreshToken
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}