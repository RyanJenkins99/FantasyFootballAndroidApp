package com.fantasyfootball.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantasyfootball.R
import com.fantasyfootball.adapters.TeamAdapter
import com.fantasyfootball.adapters.TeamListener
import com.fantasyfootball.databinding.ActivityTeamListBinding
import com.fantasyfootball.main.MainApp
import com.fantasyfootball.models.TeamModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class TeamListActivity : AppCompatActivity(), TeamListener {

    val db = Firebase.firestore
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

        getTeams()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TeamAdapter(app.teams.teams,this)
        Timber.d("IM HERE AGAIN" + app.teams.teams)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun getTeams() = runBlocking {
        app.teams.teams.clear()
        db.collection("teams")//looks for teams collection
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {//make a new team object and add to teams array
                    Timber.d( "${document.id} => ${document.data}")
                    var location = document.data["loc"] as MutableMap<String, Double>
                    app.teams.teams.add( TeamModel(UUID.randomUUID(),document.data["name"].toString(),document.data["league"].toString(),"".toUri(),0.0,0.0,
                        location.get("latitude")?.let { LatLng(it, location.get("longitude")!!) },0f ,document.data["formation"].toString()

                    ))
                }
            }
            .addOnFailureListener { exception ->
                Timber.d( "Error getting documents: "+ exception)
            }
            .await()
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
                Timber.i("IM HERE" +app.teams.teams)
                db.collection("teams")//looks for teams collection
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {//make a new team object and add to teams array
                            Timber.d( "${document.id} => ${document.data}")
                            var location = document.data["loc"] as MutableMap<String, Double>
                            app.teams.teams.add( TeamModel(UUID.randomUUID(),document.data["name"].toString(),document.data["league"].toString(),"".toUri(),0.0,0.0,
                                location.get("latitude")?.let { LatLng(it, location.get("longitude")!!) },0f ,document.data["formation"].toString()
                            ))
                        }
                    }
                    .addOnFailureListener { exception ->
                        Timber.d( "Error getting documents: "+ exception)
                    }
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.teams.teams.size)
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
                notifyItemRangeChanged(0,app.teams.teams.size)
            }
            else
                binding.recyclerView.adapter = TeamAdapter(app.teams.teams,this)
            if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }

    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }
}


