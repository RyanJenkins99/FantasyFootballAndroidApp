package com.fantasyfootball.models

import timber.log.Timber.i

//var lastId = 0L
//
//internal fun getId(): Long {
//    return lastId++
//}

class PlayerMemStore : PlayerStore {

    var players = ArrayList<PlayerModel>()


    override fun create(player: PlayerModel) {
        players.add(player)
        logAll()
    }



    override fun delete(player: PlayerModel) {
        players.remove(player)
    }

    fun logAll() {
        players.forEach{ i("${it}") }
    }
}