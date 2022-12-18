package com.fantasyfootball.models

interface TeamStore {
    fun create(team: TeamModel)
    fun update(team: TeamModel)
    fun delete(team: TeamModel)
}