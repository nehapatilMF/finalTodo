package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.TodoAdapter
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.viewModels.GetTodoListViewModel

class Home : Fragment() {
    private var binding : FragmentHomeBinding? = null
    private val viewModel: GetTodoListViewModel by activityViewModels()
    private lateinit var todoAdapter : TodoAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchTodoList()
        binding?.addTask?.setOnClickListener {
            findNavController().navigate(R.id.navigate_from_todoMain_to_newTask)
        }

        viewModel.fetchTodoListStatus.observe(viewLifecycleOwner, Observer { status ->
            if (status == "200") {
                viewModel.todoList.observe(requireActivity()) { todoList ->
                    if (todoList != null) {

                        todoAdapter = TodoAdapter(todoList)
                        binding?.recyclerView?.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding?.recyclerView?.adapter = todoAdapter

                    }
                }
            }
            })
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