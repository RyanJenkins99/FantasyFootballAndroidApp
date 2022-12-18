package com.fantasyfootball.models

interface PlayerStore {
    fun create(team: PlayerModel)

    fun delete(team: PlayerModel)
}