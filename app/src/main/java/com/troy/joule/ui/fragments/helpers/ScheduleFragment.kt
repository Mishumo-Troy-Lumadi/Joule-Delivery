package com.troy.joule.ui.fragments.helpers

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.troy.joule.JouleApp
import com.troy.joule.databinding.FragmentScheduleBinding
import com.troy.joule.repository.Repository
import com.troy.joule.ui.activities.MainActivity
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val args: ScheduleFragmentArgs by navArgs()

    private lateinit var app: JouleApp
    private lateinit var repository: Repository
    private lateinit var activity: MainActivity
    private lateinit var sharedViewModel: SharedViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(layoutInflater, container, false)

        activity = requireActivity() as MainActivity
        app = activity.application as JouleApp

        repository = app.repository
        sharedViewModel = activity.sharedViewModel

        val delivery = args.delivery

        binding.txtSubTotal.editText?.setText("R${delivery.subTotal}")

        binding.btnSchedule.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val invoice = repository.scheduleCollection(app.user!!.id, delivery)
                repository.loadInvoices(app.user!!.id)

                if (invoice != null) {
                    withContext(Dispatchers.Main) {
                        val action =
                            ScheduleFragmentDirections.actionScheduleFragmentToInvoiceFragment(
                                invoice.id
                            )
                        findNavController().navigate(action)
                    }

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