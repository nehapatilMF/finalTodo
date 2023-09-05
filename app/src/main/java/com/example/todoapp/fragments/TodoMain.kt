package com.example.todoapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoMainBinding


class TodoMain : Fragment() {
    private var binding :FragmentTodoMainBinding? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.addTask?.setOnClickListener {
            findNavController().navigate(R.id.navigate_from_todoMain_to_newTask)
        }
        binding?.bottomNav?.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.nav_home -> {
                    findNavController().navigate(R.id.todoMain)

                    true
                }
                R.id.nav_profile ->{
                    findNavController().navigate(R.id.profile)


                    true
                }else -> {false}

            }
        }


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoMainBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

