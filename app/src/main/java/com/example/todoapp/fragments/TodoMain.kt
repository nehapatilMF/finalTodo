package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoMainBinding

class TodoMain : Fragment() {
    private var binding :FragmentTodoMainBinding? = null
   private var fragmentManager : FragmentManager ? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           binding?.bottomNav?.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.home -> loadFragment(Home())
                R.id.profile -> loadFragment(Profile())
            }
            true
        }
    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding?.bottomNav?.menu?.findItem(R.id.home)?.isChecked = true
            fragmentManager = activity?.supportFragmentManager
            loadFragment(Home())
        binding = FragmentTodoMainBinding.inflate(layoutInflater, container, false)
            return binding?.root
    }
    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            val transaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()
            return true
        }
        return false
    }
override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

}

