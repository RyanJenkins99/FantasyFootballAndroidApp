package com.fantasyfootball.main

import android.app.Application
import com.fantasyfootball.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

//    val teams = ArrayList<TeamModel>()
    lateinit var teams: TeamMemStore
    lateinit var players: PlayerMemStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        teams = TeamMemStore()
        players = PlayerMemStore()
//        teams = TeamJSONStore(applicationContext)
        i("Team started")
    }
}