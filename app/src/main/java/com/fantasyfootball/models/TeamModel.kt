package com.fantasyfootball.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamModel(
    var name: String = "",
    var league: String = "") : Parcelable
//    var formation: Formation,




enum class Formation {
    TwoTwoOne,
    TwoOneTwo,
    OneTwoTwo
}