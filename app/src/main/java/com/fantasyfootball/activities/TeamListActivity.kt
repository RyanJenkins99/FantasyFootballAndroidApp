package com.fantasyfootball.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantasyfootball.R
import com.fantasyfootball.adapters.TeamAdapter
import com.fantasyfootball.adapters.TeamListener
import com.fantasyfootball.databinding.ActivityTeamListBinding
import com.fantasyfootball.main.MainApp
import com.fantasyfootball.models.TeamModel
import timber.log.Timber


class TeamListActivity : AppCompatActivity(), TeamListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityTeamListBinding
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TeamAdapter(app.teams.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, TeamActivity::class.java)
                getResult.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, TeamMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
}

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Timber.i("IM HERE" +app.teams.findAll())
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.teams.findAll().size)
            }
        }

    override fun onTeamClick(team: TeamModel, pos : Int) {
        val launcherIntent = Intent(this, TeamActivity::class.java)
        launcherIntent.putExtra("team_edit", team)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.teams.findAll().size)
            }
            else
                binding.recyclerView.adapter = TeamAdapter(app.teams.findAll(),this)
            if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }

    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}


