package com.fantasyfootball.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.fantasyfootball.R
import com.fantasyfootball.databinding.ActivityTeamBinding
import com.fantasyfootball.main.MainApp
import com.fantasyfootball.models.TeamModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import timber.log.Timber.i


class TeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamBinding
    var team = TeamModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        if (intent.hasExtra("placemark_edit")) {
            team = intent.extras?.getParcelable("placemark_edit")!!
            binding.teamName.setText(team.name)
            binding.league.setText(team.league)
        }

        binding.btnAdd.setOnClickListener() {
            team.name = binding.teamName.text.toString()
            team.league = binding.league.text.toString()
            if (team.name.isNotEmpty()) {
                app.teams.create(team.copy())
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it, "Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_team, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}