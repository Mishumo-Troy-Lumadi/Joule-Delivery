package com.troy.joule

import android.app.Application
import com.troy.joule.repository.Repository
import com.troy.joule.repository.models.User

class JouleApp : Application() {

    lateinit var repository: Repository

    @Volatile
    var user: User? = null

    override fun onCreate() {
        super.onCreate()

        repository = Repository()
        repository.init(this)

    }
}