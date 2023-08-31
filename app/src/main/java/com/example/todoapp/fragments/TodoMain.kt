package com.example.todoapp.fragments

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoMainBinding


class TodoMain : Fragment() {
   private var binding :FragmentTodoMainBinding? = null
      @RequiresApi(Build.VERSION_CODES.O)
      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          val callback = object : OnBackPressedCallback(true) {
              override fun handleOnBackPressed() {
                  requireActivity().finishAffinity()
              }
          }
          requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
          binding?.addTask?.setOnClickListener {
              findNavController().navigate(R.id.navigate_from_todoMain_to_newTask)
          }

      }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoMainBinding.inflate(layoutInflater, container, false)
         return binding?.root
    }
}

