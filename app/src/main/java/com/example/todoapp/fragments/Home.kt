package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.TodoAdapter
import com.example.todoapp.client.RetrofitClient
import com.example.todoapp.client.SessionManager
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.interfaces.TodoAPI
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.util.DialogUtils
import com.example.todoapp.util.NetworkUtil
import com.example.todoapp.viewModelFactory.TodoViewModelFactory
import com.example.todoapp.viewModels.TodoViewModel

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

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding?.addTask?.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_newTask)
        }

        val todoAPI: TodoAPI? = RetrofitClient.getInstance()?.create(TodoAPI::class.java)
        val todoRepository = todoAPI?.let { TodoRepository(it) }
        val viewModelFactory = todoRepository?.let { TodoViewModelFactory(it) }
        val viewModel = viewModelFactory?.let { ViewModelProvider(this, it) }?.get(TodoViewModel::class.java)
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            viewModel?.fetchTodoList()
            binding?.progressBar?.visibility = View.VISIBLE
        }else{
            val message = getString(R.string.no_internet_connection)
            DialogUtils.showAutoDismissAlertDialog(requireContext(), message)
        }

        viewModel?.status?.observe(viewLifecycleOwner) { status ->
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
                "401" ->{}



            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}