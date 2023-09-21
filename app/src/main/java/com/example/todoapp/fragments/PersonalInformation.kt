package com.example.todoapp.fragments

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
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentPersonalInformationBinding
import com.example.todoapp.interfaces.TodoAPI
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.util.ValidPatterns
import com.example.todoapp.viewModelFactory.TodoViewModelFactory
import com.example.todoapp.viewModels.TodoViewModel
import com.google.android.material.snackbar.Snackbar

class PersonalInformation : Fragment() {
    private var binding: FragmentPersonalInformationBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.toolbar)

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
        binding?.progressBar?.visibility = View.VISIBLE

        binding?.buttonEdit?.setOnClickListener {
            binding?.PI?.visibility = View.INVISIBLE
            binding?.PIEdit?.visibility = View.VISIBLE
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalInformationBinding.inflate(layoutInflater,container,false)


        val sessionManager = SessionManager(requireContext())
        val todoAPI: TodoAPI? = RetrofitClient.getInstance()?.create(TodoAPI::class.java)
        val todoRepository = todoAPI?.let { TodoRepository(it) }
        val viewModelFactory = todoRepository?.let { TodoViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(TodoViewModel::class.java)
        setupTextChangeListeners()
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            viewModel?.get()
        } else {
            val message = getString(R.string.no_internet_connection)
            DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
        }
        binding?.button?.setOnClickListener {
            val name = binding?.tvName1?.text.toString()
            val mobile = binding?.tvMobile1?.text.toString()
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                viewModel?.updateUser(name,mobile)
                binding?.progressBar?.visibility = View.VISIBLE
            } else {
                val message = getString(R.string.no_internet_connection)
                DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
            }
        }

        viewModel?.status?.observe(viewLifecycleOwner){ status ->
            if(status == "200") {
                binding?.progressBar?.visibility = View.INVISIBLE
                val name = binding?.tvName1?.text.toString()

                sessionManager.saveName(name)

                viewModel.todoMessage.observe(viewLifecycleOwner) { msg ->
                    Toast.makeText(requireContext(), msg.toString(), Toast.LENGTH_SHORT).show()
                    binding?.progressBar?.visibility = View.GONE
                }

            }
        }

        viewModel?.status?.observe(viewLifecycleOwner){status ->
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

            }else {
                binding?.progressBar?.visibility = View.INVISIBLE
                Snackbar.make(requireView(),status.toString(), Snackbar.LENGTH_SHORT).show()
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


