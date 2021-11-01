package com.troy.joule.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.troy.joule.JouleApp
import com.troy.joule.R
import com.troy.joule.databinding.FragmentHomeBinding
import com.troy.joule.ui.activities.MainActivity
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var app: JouleApp
    private lateinit var activity: MainActivity

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        app = requireActivity().application as JouleApp
        activity = requireActivity() as MainActivity

        sharedViewModel = activity.sharedViewModel

        binding.mapView.getMapAsync(this)
        sharedViewModel.getDrivers()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        sharedViewModel.drivers.observe(this){
            map.clear()
            it?.forEach { driver ->
                val latLng = LatLng(driver.latitude,driver.longitude)
                MarkerOptions().title(driver.fullName).position(latLng)
            }
        }

    }

}