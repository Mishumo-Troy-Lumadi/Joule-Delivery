package com.troy.joule.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.troy.joule.JouleApp
import com.troy.joule.Methods.isConnected
import com.troy.joule.databinding.FragmentInvoicesBinding
import com.troy.joule.repository.Repository
import com.troy.joule.ui.activities.MainActivity
import com.troy.joule.ui.adapters.ComponentInvoiceAdapter
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InvoicesFragment : Fragment() {

    private var _binding: FragmentInvoicesBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ComponentInvoiceAdapter(this) }

    private lateinit var app: JouleApp
    private lateinit var repository: Repository
    private lateinit var activity: MainActivity
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInvoicesBinding.inflate(inflater, container, false)

        activity = requireActivity() as MainActivity
        app = activity.application as JouleApp

        repository = app.repository
        sharedViewModel = activity.sharedViewModel

        binding.list.adapter = adapter
        binding.list.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)

        binding.invoicesSwipe.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (isConnected(requireActivity())) {
                    repository.loadInvoices(app.user!!.id)
                }
            }
            binding.invoicesSwipe.isRefreshing = false
        }

        if (isConnected(requireActivity())) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.loadInvoices(app.user!!.id)
            }
        }

        lifecycleScope.launchWhenResumed {

            repository.database!!.invoiceDao().getInvoices().observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.invoicesMessage.visibility = View.GONE
                    adapter.submitList(it)
                } else {
                    binding.invoicesMessage.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}