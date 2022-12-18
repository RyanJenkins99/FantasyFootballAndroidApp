package com.fantasyfootball.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.fantasyfootball.R
import com.fantasyfootball.adapters.PlayerAdapter
import com.fantasyfootball.adapters.PlayerListener
import com.fantasyfootball.adapters.TeamAdapter
import com.fantasyfootball.databinding.ActivityTeamBinding

import com.fantasyfootball.helpers.showImagePicker
import com.fantasyfootball.main.MainApp
import com.fantasyfootball.models.Location
import com.fantasyfootball.models.PlayerModel
import com.fantasyfootball.models.TeamModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import timber.log.Timber.i
import java.io.IOException
import java.util.*



class TeamActivity : AppCompatActivity() , PlayerListener{

    val db = Firebase.firestore
    private lateinit var binding: ActivityTeamBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var formation = ""
    var edit = false
    var team = TeamModel()
//    var location = Location(52.245696, -7.139102, 15f)
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var locationFromAddress:LatLng
        edit = false

        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
       // setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        i("Team Activity started...")
        var spinner = binding.spinner
        var arrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.formations,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                formation = parent.getItemAtPosition(position).toString()
                i("IM HERE")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do something when nothing is selected
            }
        }
        if (intent.hasExtra("team_edit")) {
            edit = true
            team = intent.extras?.getParcelable("team_edit")!!
            binding.teamName.setText(team.name)
            binding.league.setText(team.league)
            binding.locationName.setText(team.loc.toString())
//            binding.btnAddPlayer.setText(team)
            //binding.locationName.setText(team.loc)
            binding.btnAdd.setText(R.string.save_team)


            Picasso.get()
                .load(team.image)
                .into(binding.teamImage)
            if (team.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_team_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
//            team.id = UUID.randomUUID()
            locationFromAddress = getLocationFromAddress(binding.locationName.text.toString())!!
            team.loc=locationFromAddress
//            team.loc(getLocationFromAddress(binding.locationName.text.toString()))
            team.name = binding.teamName.text.toString()
            team.league = binding.league.text.toString()
            team.formation = formation

            i("TEST:edit $edit")
            if (team.name.isEmpty()) {
                Snackbar.make(it,R.string.enter_team_name, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) app.teams.update(team.copy())
                 else {

                    i("TEST:edit $team")
                    app.teams.create(team.copy())
                    db.collection("teams")
                        .add(team)
                        .addOnSuccessListener { documentReference ->
                            Timber.i("DocumentSnapshot written with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error adding document" +e)
                        }

                }
            }
            i("Test: $team")
            setResult(RESULT_OK)
            finish()
        }

        binding.btnAddPlayer.setOnClickListener() {
            val launcherIntent = Intent(this, PlayerActivity::class.java)
            launcherIntent.putExtra("teamName",team.name)
            startActivity(launcherIntent)
                }



        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, this)
        }

        binding.teamLocation.setOnClickListener {//finds teams address
            val location = team.loc
            if (team.zoom != 0f) {

               // location.zoom = team.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
        app.players.players.clear()
        viewPlayers()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPlayers.layoutManager = layoutManager
        binding.recyclerViewPlayers.adapter = PlayerAdapter(app.players.players,this)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_team, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.teams.delete(team)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            team.image = image

                            Picasso.get()
                                .load(team.image)
                                .into(binding.teamImage)
                            binding.chooseImage.setText(R.string.change_team_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<LatLng>("location")!!
                            i("Location == $location")
//                            team.lat = location.lat
//                            team.lng = location.lng
//                            team.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    fun getLocationFromAddress(strAddress: String?): LatLng? {
        val coder = Geocoder(this)
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = coder.getFromLocationName(strAddress!!, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            location.getLatitude()
            location.getLongitude()

            return LatLng(location.getLatitude(),location.getLongitude())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPlayerClick(player: PlayerModel, position: Int) {//finds player address start
        val launcherIntent = Intent(this, MapActivity::class.java)
            .putExtra("location", player.loc)
        mapIntentLauncher.launch(launcherIntent)

    }



    fun viewPlayers() = runBlocking {

        db.collection("players")//looks for teams collection
            .whereEqualTo("teamId", binding.teamName.text.toString())

            .get()
            .addOnSuccessListener { result ->
                for (document in result) {//make a new team object and add to teams array
                    Timber.d( "${document.id} => ${document.data}")
                    var location = document.data["loc"] as MutableMap<String, Double>
                    app.players.players.add(
                        PlayerModel(document.data["name"].toString(),document.data["position"].toString(),
                            document.data["number"].toString().toInt(),"".toUri(),
                            location.get("latitude")?.let { LatLng(it, location.get("longitude")!!) },""
                        ))

                }
            }
            .addOnFailureListener { exception ->
                Timber.d( "Error getting documents: "+ exception)
            }
            .await()
    }

}