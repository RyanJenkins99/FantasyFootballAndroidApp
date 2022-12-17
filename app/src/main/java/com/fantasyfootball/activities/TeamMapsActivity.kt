package com.fantasyfootball.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.fantasyfootball.databinding.ActivityTeamMapsBinding


class TeamMapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityTeamMapsBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


    }
}