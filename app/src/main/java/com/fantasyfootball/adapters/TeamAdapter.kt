package com.fantasyfootball.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fantasyfootball.databinding.CardTeamBinding
import com.fantasyfootball.models.TeamModel

interface TeamListener {
    fun onTeamClick(team: TeamModel)
}

class TeamAdapter constructor(private var teams: List<TeamModel>,
                              private val listener: TeamListener) :
    RecyclerView.Adapter<TeamAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTeamBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val team = teams[holder.adapterPosition]
        holder.bind(team, listener)
    }

    override fun getItemCount(): Int = teams.size

    class MainHolder(private val binding : CardTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(team: TeamModel, listener: TeamListener) {
            binding.teamName.text = team.name
            binding.league.text = team.league
            binding.root.setOnClickListener { listener.onTeamClick(team) }
        }
    }
}