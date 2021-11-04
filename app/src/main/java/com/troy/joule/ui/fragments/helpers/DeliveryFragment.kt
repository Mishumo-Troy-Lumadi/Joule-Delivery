package com.troy.joule.ui.fragments.helpers

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.troy.joule.databinding.FragmentDeliveryBinding
import com.troy.joule.repository.models.Delivery
import kotlin.math.round


class DeliveryFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDeliveryBinding? = null
    private val binding get() = _binding!!

    private val args: DeliveryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)

        val endLocation = args.endLocation
        val startLocation = args.startLocation
        val geocoder = Geocoder(requireContext())

        val address = geocoder.getFromLocation(
            endLocation.latitude.toDouble(),
            endLocation.longitude.toDouble(),
            1
        )

        // Log.d(TAG, "onCreateView: Address: ${address[0].getAddressLine(0)}")

        val d = address[0].getAddressLine(0).toString()

        binding.txtDestination.editText?.setText(d)

        binding.btnNext.setOnClickListener {
            val destination = binding.txtDestination.editText?.text.toString()
            val fullName = binding.txtFullName.editText?.text.toString()
            val cell = binding.txtCell.editText?.text.toString()
            val additionalInfo = binding.edtAdditional.text.toString()

            if (destination.isNotEmpty()) {

                if (fullName.isNotEmpty()) {

                    if (cell.isNotEmpty()) {

                        val f = FloatArray(10)
                        Location.distanceBetween(
                            startLocation.latitude.toDouble(),
                            startLocation.longitude.toDouble(),
                            endLocation.latitude.toDouble(),
                            endLocation.longitude.toDouble(),
                            f
                        )

                        val distance = f[0].toDouble()
                        //Log.d(TAG, "onCreateView: $f")

                        val rate = 0.01
                        val subTotal = round((distance * rate))

                        val delivery = Delivery(
                            distance,
                            subTotal,
                            0.0,
                            startLocation.latitude,
                            startLocation.longitude,
                            destination,
                            endLocation.latitude,
                            endLocation.longitude,
                            additionalInfo,
                            fullName,
                            cell
                        )

                        val action =
                            DeliveryFragmentDirections.actionDeliveryFragmentToScheduleFragment(
                                delivery
                            )
                        findNavController().navigate(action)

                    } else {
                        binding.txtCell.error = "Cell Required!"
                    }

                } else {
                    binding.txtFullName.error = "Name Required!"
                }

            } else {
                binding.txtDestination.error = "Destination Required!"
            }

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}