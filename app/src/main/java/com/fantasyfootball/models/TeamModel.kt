package com.fantasyfootball.models

import android.net.Uri
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class TeamModel(
    var id: UUID = UUID.randomUUID(),
    var name: String = "Bob",
    var league: String = "Prem",
    var image: Uri = Uri.EMPTY,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var loc: LatLng? = null,
    var zoom: Float = 0f,
    var formation: String = "",
    var players: MutableMap<String, PlayerModel> = mutableMapOf()
) : Parcelable
//    var formation: Formation,

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable


