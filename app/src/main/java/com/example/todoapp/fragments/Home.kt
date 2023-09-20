package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Constants
import com.example.todoapp.R
import com.example.todoapp.adapter.TodoAdapter
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModels.HomeViewModel
import com.example.todoapp.viewModels.RefreshTokenViewModel

class Home : Fragment() {
    private var binding : FragmentHomeBinding? = null
    private lateinit var todoAdapter : TodoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.bottomNav?.visibility = View.VISIBLE
        binding?.bottomNav?.menu?.findItem(R.id.home)?.isChecked = true
        val sessionManager = SessionManager(requireContext())
        val name1 = sessionManager.getName().toString()
        val name = "Welcome, $name1"
        binding?.welcome?.text = name
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

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.fetchTodoList()
        binding?.addTask?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_newTask)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            viewModel.fetchTodoList()
            binding?.progressBar?.visibility = View.VISIBLE
        }else{
            val message = getString(R.string.no_internet_connection)
            DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        val sessionManager = SessionManager(requireContext())
        viewModel.fetchTodoListStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                "200" -> {
                    viewModel.todoList.observe(requireActivity()) { todoList ->
                        if (todoList != null) {
                            todoAdapter = TodoAdapter(todoList)
                            binding?.recyclerView?.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            binding?.recyclerView?.adapter = todoAdapter
                            binding?.progressBar?.visibility = View.GONE
                        }
                    }
                }
                "404" -> {
                    binding?.progressBar?.visibility = View.GONE
                }
                "401" -> {
                    val rViewModel = ViewModelProvider(this)[RefreshTokenViewModel::class.java]
                    val refreshToken1 = sessionManager.getRefreshToken()!!
                        rViewModel.refreshToken(refreshToken1)
                    rViewModel.result.observe(viewLifecycleOwner){status1 ->
                        Toast.makeText(requireContext(),status1,Toast.LENGTH_SHORT).show()
                        if(status1 == "200"){
                            binding?.progressBar?.visibility = View.GONE
                            rViewModel.getAuthTokens().observe(viewLifecycleOwner){ authTokens ->
                                sessionManager.clearTokens()
                                Constants.clearRefreshToken()
                                Constants.clearRefreshToken()
                                val accessToken = authTokens.accessToken
                                Constants.accessToken = accessToken
                                val refreshToken = authTokens.refreshToken
                                Constants.refreshToken = refreshToken
                                sessionManager.saveTokens(accessToken,refreshToken)
                                viewModel.fetchTodoList()
                            }
                        }else{
                            binding?.progressBar?.visibility = View.GONE
                            DialogUtils.showAutoDismissAlertDialog(requireContext(),
                                getString(R.string.your_session_has_expired))
                            sessionManager.clearTokens()
                            Constants.clearAccessToken()
                            Constants.clearRefreshToken()
                            findNavController().navigate(R.id.navigate_to_intro)
                            binding?.progressBar?.visibility = View.GONE
                          //  Toast.makeText(requireContext(),status1.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}