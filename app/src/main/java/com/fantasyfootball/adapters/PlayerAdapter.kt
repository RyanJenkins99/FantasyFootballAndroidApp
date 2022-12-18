package com.fantasyfootball.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fantasyfootball.databinding.CardPlayerBinding


import com.fantasyfootball.models.PlayerModel
import com.squareup.picasso.Picasso

interface PlayerListener {
    fun onPlayerClick(player: PlayerModel, position: Int)
}

class PlayerAdapter constructor(private var players: ArrayList<PlayerModel>,
                              private val listener: PlayerListener) :
    RecyclerView.Adapter<PlayerAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlayerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val player = players[holder.adapterPosition]
        holder.bind(player, listener)
    }

    override fun getItemCount(): Int = players.size

    class MainHolder(private val binding : CardPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: PlayerModel, listener: PlayerListener) {//filling in each detail for player
            binding.playerName.text = player.name
            binding.position.text = player.position
            binding.number.text = player.number.toString()
            Picasso.get().load(player.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onPlayerClick(player,adapterPosition) }
        }
    }
}