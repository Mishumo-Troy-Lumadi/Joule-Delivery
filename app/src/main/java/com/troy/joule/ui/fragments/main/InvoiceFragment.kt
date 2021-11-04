package com.troy.joule.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.troy.joule.JouleApp
import com.troy.joule.Methods
import com.troy.joule.databinding.FragmentInvoiceBinding
import com.troy.joule.repository.Repository
import com.troy.joule.ui.activities.MainActivity
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel

class InvoiceFragment : Fragment(), OnMapReadyCallback , SwipeRefreshLayout.OnRefreshListener{

    private var _binding: FragmentInvoiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    private lateinit var app: JouleApp
    private lateinit var repository: Repository
    private lateinit var activity: MainActivity
    private lateinit var sharedViewModel: SharedViewModel

    private val  args:InvoiceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceBinding.inflate(inflater, container, false)
        binding.mapView2.onCreate(savedInstanceState)

        activity = requireActivity() as MainActivity
        app = activity.application as JouleApp

        repository = app.repository
        sharedViewModel = activity.sharedViewModel

        binding.mapView2.getMapAsync(this)
        binding.invoiceRefresh.setOnRefreshListener(this)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isScrollGesturesEnabled = false
        map.uiSettings.isRotateGesturesEnabled = false

        load()

    }

    private fun load(){
        repository.getInvoice(args.id).observe(viewLifecycleOwner){ invoice->

            if(invoice != null) {
                binding.txtDate.editText!!.setText(invoice.createdAt)
                binding.txtId.text = invoice.id
                binding.txtStatus.text = invoice.status
                binding.txtStatus.setTextColor(Methods.assignColor(requireContext(), invoice.status))
                binding.imgQr.setImageBitmap(
                    Methods.generateQRCode(
                        "https://jouleio.herokuapp.com/api/v1/invoices/${invoice.id}",
                        200
                    )
                )

                val destination = LatLng(invoice.endLatitude.toDouble(), invoice.endLongitude.toDouble())

                map.addMarker(MarkerOptions().position(destination))

                val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(destination, 14f)
                map.animateCamera(cameraUpdateFactory)

                binding.btnTrack.setOnClickListener {
                    val action =
                        InvoiceFragmentDirections.actionInvoiceFragmentToTrackingFragment(invoice.driver!!)
                    findNavController().navigate(action)
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView2.onStart()
    }

    override fun onStop() {
        binding.mapView2.onStop()
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
    }

    override fun onRefresh() {
        load()
    }

}