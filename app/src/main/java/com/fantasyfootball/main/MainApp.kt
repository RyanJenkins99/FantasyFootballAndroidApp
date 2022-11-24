package com.fantasyfootball.main

import android.app.Application
import com.fantasyfootball.models.TeamMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

//    val teams = ArrayList<TeamModel>()
    val teams = TeamMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Team started")

    }
}