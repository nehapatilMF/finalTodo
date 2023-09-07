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
import com.example.todoapp.viewModels.DeleteViewModel
import com.example.todoapp.viewModels.LogoutViewModel

class Profile : Fragment() {


    private var binding : FragmentProfileBinding? = null
    private val logoutViewModel: LogoutViewModel by activityViewModels()
    private val deleteViewModel: DeleteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)



        logoutViewModel.logoutResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){

                findNavController().popBackStack(R.id.intro, false)
               // findNavController().navigate(R.id.navigate_to_intro)
            }else{
                val message = "Logout Failed"
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        deleteViewModel.deleteUserResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){

                findNavController().popBackStack(R.id.intro, false)
               // findNavController().navigate(R.id.navigate_to_intro)
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