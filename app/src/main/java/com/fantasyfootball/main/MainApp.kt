package com.fantasyfootball.main

import android.app.Application
import com.fantasyfootball.models.TeamJSONStore
import com.fantasyfootball.models.TeamMemStore
import com.fantasyfootball.models.TeamModel
import com.fantasyfootball.models.TeamStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

//    val teams = ArrayList<TeamModel>()
    lateinit var teams: TeamMemStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
         teams = TeamMemStore()
//        teams = TeamJSONStore(applicationContext)
        i("Team started")
    }
}