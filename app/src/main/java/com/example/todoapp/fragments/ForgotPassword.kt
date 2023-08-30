package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentForgotPasswordBinding
import com.example.todoapp.util.NetworkUtil


class ForgotPassword : Fragment() {
   private var binding:FragmentForgotPasswordBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     binding?.btnBack?.setOnClickListener{
         findNavController().navigate(R.id.navigate_from_forgot_password_to_login)
     }
        binding?.btnResetPassword?.setOnClickListener{
            if (NetworkUtil.isNetworkAvailable(requireContext())) {
                findNavController().navigate(R.id.navigate_to_forgotPasswordOtp)
            }    else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding?.root
    }


}