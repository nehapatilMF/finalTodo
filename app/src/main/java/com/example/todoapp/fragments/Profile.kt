package com.example.todoapp.fragments

import android.app.Dialog
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
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.databinding.LogoutConfirmationBinding
import com.example.todoapp.databinding.UserDeleteConfirmationBinding
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.ProfileViewModel

class Profile : Fragment() {
    private var binding : FragmentProfileBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
       // actionBar?.setDisplayHomeAsUpEnabled(true)
       // actionBar?.title = "Profile"

        binding?.personalInformation?.setOnClickListener {
            findNavController().navigate(R.id.navigate_to_personalInformation)
        }
        binding?.ChangePassword?.setOnClickListener {
           findNavController().navigate(R.id.navigate_to_changePassword)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigate_to_todoMain)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        //(requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        binding?.Logout?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                val customDialog = Dialog(requireContext())
                val dialogBinding = LogoutConfirmationBinding.inflate(layoutInflater)
                customDialog.setContentView(dialogBinding.root)
                customDialog.setCanceledOnTouchOutside(false)
                dialogBinding.tvYes.setOnClickListener {
                    viewModel.logout()
                    binding?.progressBar?.visibility = View.VISIBLE
                    customDialog.dismiss()
                }
                dialogBinding.tvNo.setOnClickListener {
                    customDialog.dismiss()
                }
                customDialog.show()
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
                    binding?.progressBar?.visibility = View.VISIBLE
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
                binding?.progressBar?.visibility = View.INVISIBLE
                viewModel.msg.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    Toast.makeText(requireContext(),tMsg,Toast.LENGTH_SHORT).show()
                }
              findNavController().navigate(R.id.navigate_to_intro)
                SessionManager(requireContext()).clearTokens()
                Constants.clearAccessToken()
            }else{
                val message = status.toString()
                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.deleteUserResult.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.navigate_to_intro)
                viewModel.msg.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    Toast.makeText(requireContext(),tMsg,Toast.LENGTH_SHORT).show()
                }

            }else{
                val message = status.toString()
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()

            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}