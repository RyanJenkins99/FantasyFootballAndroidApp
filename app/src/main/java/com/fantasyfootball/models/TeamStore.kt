package com.fantasyfootball.models

interface TeamStore {
    fun findAll(): List<TeamModel>
    fun create(team: TeamModel)
    fun update(team: TeamModel)
}