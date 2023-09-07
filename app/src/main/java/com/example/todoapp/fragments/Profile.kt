package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.ProfileViewModel

class Profile : Fragment() {
    private var binding : FragmentProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
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
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                viewModel.deleteUser()
            }
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        val viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.logoutResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){

               // findNavController().popBackStack(R.id.intro, false)
              findNavController().navigate(R.id.navigate_to_intro)
            }else{
                val message = "Logout Failed"
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel.deleteUserResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){

               // findNavController().popBackStack(R.id.intro, false)
                findNavController().navigate(R.id.navigate_to_intro)
            }else{
                val message = "user not deleted."
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