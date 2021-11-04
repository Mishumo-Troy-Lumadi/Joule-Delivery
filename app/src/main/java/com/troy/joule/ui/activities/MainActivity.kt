package com.troy.joule.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.troy.joule.JouleApp
import com.troy.joule.R
import com.troy.joule.databinding.ActivityMainBinding
import com.troy.joule.repository.Repository
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var sharedViewModel: SharedViewModel
    private var backToast = true
    private lateinit var app: JouleApp
    private lateinit var repository: Repository

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest


    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        app = (application as JouleApp)
        repository = app.repository

        val viewModelFactory = SharedViewModelFactory()
        sharedViewModel = ViewModelProvider(this, viewModelFactory).get(
            SharedViewModel::class.java
        )

        sharedViewModel.init(repository)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        initLocationRequest()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navContainer)
        val navController = navHostFragment?.findNavController()

        navController!!.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.label?.equals("NoMenu") == true) {
                binding.navBottom.visibility = View.GONE
                backToast = true
            } else {
                backToast = false
                binding.navBottom.visibility = View.VISIBLE
            }
        }

        binding.navBottom.setupWithNavController(navController)

    }

    private var doubleBack: Boolean = false
    override fun onBackPressed() {
        if (doubleBack || backToast) {
            super.onBackPressed()
        } else {
            doubleBack = true
            Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ doubleBack = false }, 3000)
        }
    }

    private fun initLocationRequest() {
        locationRequest = LocationRequest.create()

        locationRequest.interval = 500
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    private fun checkSettingsAndStartLocationUpdates() {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()

        val client = LocationServices.getSettingsClient(this)

        val task = client.checkLocationSettings(request)

        task.addOnSuccessListener {
            startLocationUpdates()
        }

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                val apiE = it
                apiE.startResolutionForResult(this, 1001)
            }
        }

    }

    private var callback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            if (result == null) {
                return
            }

            val locations = result.locations

            if (locations.isNotEmpty()) {
                val location = locations.first()
                sharedViewModel.location.value = location
            }
        }
    }

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermission()
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, mainLooper)

    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(callback)
    }

    override fun onStop() {
        stopLocationUpdates()
        super.onStop()
    }


    override fun onStart() {
        super.onStart()

        try {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
            } else {
                checkSettingsAndStartLocationUpdates()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun requestPermission() {

        try {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                AlertDialog.Builder(this).setMessage("Location Access is required")
                    .setCancelable(false)
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_CODE
                        )
                    }.show()

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {

                AlertDialog.Builder(this).setMessage("Location Access is required")
                    .setCancelable(false)
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                            REQUEST_CODE
                        )
                    }.show()

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_CODE
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    AlertDialog.Builder(this).setMessage("Location Access has been blocked")
                        .setPositiveButton("OK") { _, _ ->
                            gotoAppSettings()
                        }
                        .setNegativeButton("Not Now", null)
                        .setCancelable(false)
                        .show()
                }
            } else {
                checkSettingsAndStartLocationUpdates()
            }
        }

    }

    private fun gotoAppSettings() {
        try {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val REQUEST_CODE: Int = 4889
    }

}