package com.troy.joule.ui.fragments.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.troy.joule.JouleApp
import com.troy.joule.Methods.bitmapDescriptorFromVector
import com.troy.joule.Methods.isConnected
import com.troy.joule.R
import com.troy.joule.databinding.FragmentHomeBinding
import com.troy.joule.repository.models.JouleLocation
import com.troy.joule.ui.activities.MainActivity
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var app: JouleApp
    private lateinit var activity: MainActivity
    private var second = false


    private lateinit var sharedViewModel: SharedViewModel
    private var location: Location? = null

    private var marker: Marker? = null

    @SuppressLint("VisibleForTests")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        app = requireActivity().application as JouleApp
        activity = requireActivity() as MainActivity

        sharedViewModel = activity.sharedViewModel

        binding.mapView.getMapAsync(this)


        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false

        lifecycleScope.launchWhenStarted {
            if (isConnected(requireActivity())) {
                sharedViewModel.getDrivers()
            }
            collect()
        }

        map.setOnMapClickListener {
            binding.floatingActionButton.visibility = View.GONE
            lifecycleScope.launch {
                if (isConnected(requireActivity())) {
                    sharedViewModel.getDrivers()
                }
                collect()
                second = false
            }
            second = false
        }

        var first = true
        sharedViewModel.location.observe(viewLifecycleOwner) {

            location = it
            if (isConnected(requireActivity())) {
                sharedViewModel.updateLocation(app.user!!.id, location!!)
            }

            val latLng = LatLng(it.latitude, it.longitude)

            if (first) {
                move(latLng, 20f, false)
                first = false
            }

            setLocationMarker(it)

        }


        map.setOnMapLongClickListener { value ->
            lifecycleScope.launchWhenStarted {
                collect()
            }

            map.addMarker(
                MarkerOptions().position(value).icon(
                    bitmapDescriptorFromVector(requireContext(), R.drawable.ic_pin)
                )
            )


            binding.floatingActionButton.visibility = View.VISIBLE
            move(value, 17f, true)

            binding.floatingActionButton.setOnClickListener {

                if (isConnected(requireActivity())) {


                    val currentLocation =
                        JouleLocation(
                            location!!.latitude.toString(),
                            location!!.longitude.toString()
                        )
                    val destinationLocation =
                        JouleLocation(value.latitude.toString(), value.longitude.toString())

                    val action =
                        HomeFragmentDirections.actionNavigationHomeToDeliveryFragment(
                            currentLocation,
                            destinationLocation
                        )
                    findNavController().navigate(action)
                }

            }
        }

    }

    private var accuracy: Circle? = null

    private fun setLocationMarker(l: Location) {
        val latLng = LatLng(l.latitude, l.longitude)

        if (marker == null) {
            //Create new Marker
            val options = MarkerOptions()
                .title("Current Location")
                .position(latLng)
                .icon(
                    bitmapDescriptorFromVector(requireContext(), R.drawable.ic_ic_shopping_box)
                )
                //.rotation(l.bearing)
                .anchor(0.5f, 0.5f)
            marker = map.addMarker(options)
            //move(latLng, 17f, true)
        } else {
            marker!!.position = latLng
            // marker!!.rotation = l.bearing
            //move(latLng, 17f, false)
        }

        if (accuracy == null) {
            val options = CircleOptions()
                .center(latLng)
                .strokeWidth(4f)
                .strokeColor(Color.argb(255, 0, 0, 25))
                .fillColor(Color.argb(32, 0, 0, 25))
                .radius(l.accuracy.toDouble())
            accuracy = map.addCircle(options)

        } else {
            accuracy!!.center = latLng
            accuracy!!.radius = l.accuracy.toDouble()
        }

    }

    private suspend fun collect() {
        sharedViewModel.drivers.collectLatest {
            map.clear()
            marker = null
            accuracy = null
            if (location != null) {
                setLocationMarker(location!!)
            }
            it.forEach { driver ->
                val latLng = LatLng(driver.latitude.toDouble(), driver.longitude.toDouble())
                val options = MarkerOptions().title(driver.fullName).position(latLng).icon(
                    bitmapDescriptorFromVector(requireContext(), R.drawable.ic_courier)
                )

                map.addMarker(options)
            }
        }
    }

    private fun move(latLng: LatLng, float: Float, animate: Boolean) {
        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, float)

        if (animate) {
            map.animateCamera(cameraUpdateFactory)
        } else {
            map.moveCamera(cameraUpdateFactory)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
}