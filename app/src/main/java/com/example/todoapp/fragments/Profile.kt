package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.LogoutViewModel

class Profile : Fragment() {
    private val viewModel: LogoutViewModel by activityViewModels()
private var binding : FragmentProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
            binding?.personalInformation?.setOnClickListener {
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

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        viewModel.logoutResult.observe(viewLifecycleOwner){ status ->

            if(status == "200"){
                findNavController().navigate(R.id.action_profile_to_login)
            }else{
                val message = "Logout Failed"
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }

        }


        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}