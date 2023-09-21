package com.example.todoapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.databinding.LogoutConfirmationBinding
import com.example.todoapp.databinding.UserDeleteConfirmationBinding
import com.example.todoapp.interfaces.TodoAPI
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModelFactory.TodoViewModelFactory
import com.example.todoapp.viewModels.TodoViewModel
import com.google.android.material.snackbar.Snackbar

class Profile : Fragment() {
    private var binding : FragmentProfileBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       //val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
       // actionBar?.setDisplayHomeAsUpEnabled(true)
       // actionBar?.title = "Profile"
        binding?.bottomNav?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    findNavController().navigate(R.id.home)
                }

                R.id.profile -> {
                    findNavController().navigate(R.id.profile)
                }
            }
            true
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_profile_to_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding?.personalInformation?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_personalInformation)
        }
        binding?.ChangePassword?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_changePassword)
        }

    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        //(requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        val sessionManager = SessionManager(requireContext())
        val todoAPI: TodoAPI? = RetrofitClient.getInstance()?.create(TodoAPI::class.java)
        val todoRepository = todoAPI?.let { TodoRepository(it) }
        val viewModelFactory = todoRepository?.let { TodoViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(TodoViewModel::class.java)

        binding?.bottomNav?.visibility = View.VISIBLE
        binding?.bottomNav?.menu?.findItem(R.id.profile)?.isChecked = true

        binding?.Logout?.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                val customDialog = Dialog(requireContext())
                val dialogBinding = LogoutConfirmationBinding.inflate(layoutInflater)
                customDialog.setContentView(dialogBinding.root)
                customDialog.setCanceledOnTouchOutside(false)
                dialogBinding.tvYes.setOnClickListener {
                    viewModel?.logout()
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
                    viewModel?.deleteUser()
                    binding?.progressBar?.visibility = View.VISIBLE
                    customDialog.dismiss()
                }
                dialogBinding.tvNo.setOnClickListener {
                    customDialog.dismiss()
                }
                customDialog.show()
            }
        }
        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                viewModel.todoMessage.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    Snackbar.make(requireView(),tMsg,Snackbar.LENGTH_SHORT).show()
                }
                findNavController().navigate(R.id.action_profile_to_intro)
                SessionManager(requireContext()).clearTokens()

            }else {
                binding?.progressBar?.visibility = View.INVISIBLE
                Snackbar.make(requireView(),status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "200"){
                binding?.progressBar?.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_profile_to_intro)
                viewModel.todoMessage.observe(viewLifecycleOwner){ msg ->
                    val tMsg = msg.toString()
                    Snackbar.make(requireView(),tMsg,Snackbar.LENGTH_SHORT).show()
                }


            }else {
                binding?.progressBar?.visibility = View.INVISIBLE
                Snackbar.make(requireView(),status.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}