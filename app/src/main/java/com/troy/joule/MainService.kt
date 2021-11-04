package com.troy.joule

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.troy.joule.repository.webservice.objects.Constants.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        while (true){
            CoroutineScope(Dispatchers.IO).launch {
                val app = application as JouleApp

                if(app.user != null) {
                    app.repository.getDrivers()
                    app.repository.loadInvoices(app.user!!.id)
                    Thread.sleep(2000)
                    Log.d(TAG, "onCreate: Load in Service")
                }
            }

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)

    }
}