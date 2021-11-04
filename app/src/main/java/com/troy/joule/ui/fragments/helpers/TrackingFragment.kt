package com.troy.joule.ui.fragments.helpers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.troy.joule.JouleApp
import com.troy.joule.databinding.FragmentTrackingBinding
import com.troy.joule.repository.Repository
import com.troy.joule.ui.activities.MainActivity
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackingFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private val args :TrackingFragmentArgs by navArgs()

    private lateinit var app: JouleApp
    private lateinit var repository: Repository
    private lateinit var activity: MainActivity
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)

        activity = requireActivity() as MainActivity
        app = activity.application as JouleApp

        repository = app.repository
        sharedViewModel = activity.sharedViewModel

        binding.mapView3.onCreate(savedInstanceState)
        binding.mapView3.getMapAsync(this)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        binding.mapView3.onStart()
    }

    override fun onStop() {
        binding.mapView3.onStop()
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView3.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView3.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isScrollGesturesEnabled = false
        map.uiSettings.isRotateGesturesEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            sharedViewModel.drivers.collectLatest {
               val list = it.filter { driver -> driver.id === args.id }
                if(list.isNotEmpty()) {
                    val driver = list.first()

                    val latLng = LatLng(driver.latitude.toDouble(), driver.longitude.toDouble())

                    move(latLng, 17f)
                }

            }
        }

    }

    private fun move(latLng: LatLng, float: Float) {
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, float)
        map.animateCamera(cameraUpdateFactory)
    }
}