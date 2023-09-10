package com.example.todoapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.databinding.UserDeleteConfirmationBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.ProfileViewModel

class Profile : Fragment() {
    private var binding : FragmentProfileBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Profile"
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.navigate_to_todoMain)
        }

        binding?.personalInformation?.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_personalInformation)
        }
        binding?.ChangePassword?.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_changePassword)
        }

    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        binding?.Logout?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                viewModel.logout()
            }
        }
        binding?.deleteAccount?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                val customDialog = Dialog(requireContext())
                val dialogBinding = UserDeleteConfirmationBinding.inflate(layoutInflater)
                customDialog.setContentView(dialogBinding.root)
                customDialog.setCanceledOnTouchOutside(false)
                dialogBinding.tvYes.setOnClickListener {
                    viewModel.deleteUser()
                    customDialog.dismiss()
                }
                dialogBinding.tvNo.setOnClickListener {
                    customDialog.dismiss()
                }
                customDialog.show()
            }
        }
        viewModel.logoutResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                viewModel.msg.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    DialogUtils.showAutoDismissAlertDialog(requireContext(), tMsg)
                }
              findNavController().navigate(R.id.navigate_to_intro)

            }else{
                val message = status.toString()
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel.deleteUserResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                findNavController().navigate(R.id.navigate_to_intro)
                viewModel.msg.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    DialogUtils.showAutoDismissAlertDialog(requireContext(),tMsg)
                }

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