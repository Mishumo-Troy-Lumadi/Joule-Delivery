package com.troy.joule

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.properties.Delegates

object Methods {
    fun isConnected(activity: Activity): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            if (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }

        return false
    }

    fun generateQRCode(url: String, size: Int): Bitmap? {
        try {

            val multiFormatWriter = MultiFormatWriter()

            val bitMatrix =
                multiFormatWriter.encode(
                    url,
                    BarcodeFormat.QR_CODE,
                    size,
                    size
                )

            val barcodeEncode = BarcodeEncoder()

            return barcodeEncode.createBitmap(bitMatrix)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun assignColor(context: Context, status: String): Int {

        var color by Delegates.notNull<Int>()

        when (status.lowercase()) {
            "pending", "cancelled" -> {

                color = context.resources.getColor(
                    R.color.danger,
                    context.resources.newTheme()
                )

            }
            "assigned" -> {

                color = context.resources.getColor(
                    R.color.warn,
                    context.resources.newTheme()
                )


            }
            "collected", "in transit" -> {

                color = context.resources.getColor(
                    R.color.info,
                    context.resources.newTheme()
                )


            }
            "delivered" -> {
                color = context.resources.getColor(
                    R.color.success,
                    context.resources.newTheme()
                )

            }
        }
        return color
    }

    fun bitmapDescriptorFromVector(context: Context,vectorResId:Int): BitmapDescriptor?{
        return ContextCompat.getDrawable(context,vectorResId)?.run {
            setBounds(0,0, this.intrinsicWidth,this.intrinsicHeight)
            val bitmap = Bitmap.createBitmap(this.intrinsicWidth,this.intrinsicHeight,Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}