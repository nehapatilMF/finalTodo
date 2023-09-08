package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentPersonalInformationBinding

class PersonalInformation : Fragment() {
    private var binding: FragmentPersonalInformationBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  val viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Personal Information"
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_personalInformation_to_profile)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalInformationBinding.inflate(inflater,container,false)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //binding = null
    }
}