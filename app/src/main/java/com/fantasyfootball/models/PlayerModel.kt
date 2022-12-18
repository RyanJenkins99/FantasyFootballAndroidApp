package com.fantasyfootball.models

import android.net.Uri
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class PlayerModel(
    var name: String = "",
    var position: String = "",
    val number: Int = 0,
    var image: Uri = Uri.EMPTY,
    var loc: LatLng? = null,
    var teamId:String=""
    ) : Parcelable


    @Parcelize
    data class PlayerLocation(
        var lat: Double = 0.0,
        var lng: Double = 0.0,
        var zoom: Float = 0f
    ) : Parcelable
