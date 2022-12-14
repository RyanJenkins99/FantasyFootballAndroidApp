package com.fantasyfootball.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class TeamMemStore : TeamStore {

    val teams = ArrayList<TeamModel>()

    override fun findAll(): List<TeamModel> {
        return teams
    }

    override fun create(team: TeamModel) {
        teams.add(team)
        logAll()
    }


    override fun update(team: TeamModel) {
        var foundTeam: TeamModel? = teams.find { p -> p.id == team.id }
        if (foundTeam != null) {
            foundTeam.name = team.name
            foundTeam.league = team.league
            foundTeam.image = team.image
            foundTeam.lat = team.lat
            foundTeam.lng = team.lng
            foundTeam.zoom = team.zoom
            logAll()
        }
    }

    override fun delete(team: TeamModel) {
        teams.remove(team)
    }

    fun logAll() {
        teams.forEach{ i("${it}") }
    }
}