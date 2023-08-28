package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.util.NetworkUtil

class Login : Fragment() {
    private var binding : FragmentLoginBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.back_to_intro)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


            binding?.signup?.setOnClickListener {
                if(NetworkUtil.isNetworkAvailable(requireContext())){
                findNavController().navigate(R.id.navigate_from_login_to_register)
            }else{
                    Toast.makeText(requireContext(),"No internet connection.",Toast.LENGTH_SHORT).show()
                }
            }
            binding?.buttonNext?.setOnClickListener {
                if(NetworkUtil.isNetworkAvailable(requireContext())){
                findNavController().navigate(R.id.navigate_from_login_to_todoMain)
            }else{
                    Toast.makeText(requireContext(),"No internet connection.",Toast.LENGTH_SHORT).show()

            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }


}