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
import com.fantasyfootball.R
import com.fantasyfootball.databinding.ActivityPlayerBinding
import com.fantasyfootball.helpers.showImagePicker
import com.fantasyfootball.main.MainApp
import com.fantasyfootball.models.Location
import com.fantasyfootball.models.PlayerModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import timber.log.Timber
import timber.log.Timber.i
import java.io.IOException
import java.util.*



class PlayerActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false
    var player = PlayerModel()
    //    var location = Location(52.245696, -7.139102, 15f)
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var locationFromAddress:LatLng
        edit = false

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        // setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        i("Player Activity started...")



        if (intent.hasExtra("player_edit")) {
            edit = true
            player = intent.extras?.getParcelable("player_edit")!!
            binding.playerName.setText(player.name)
            binding.position.setText(player.position)
            binding.playerLocation.setText(player.loc.toString())
            binding.btnAdd.setText(R.string.save_player)

            Picasso.get()
                .load(player.image)
                .into(binding.playerImage)
            if (player.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_player_image)
            }
        }

        binding.btnAdd.setOnClickListener() {

            locationFromAddress = getLocationFromAddress(binding.locationName.text.toString())!!
            player.loc=locationFromAddress

            player.name = binding.playerName.text.toString()
            player.position = binding.position.text.toString()
            player.teamId= intent.getStringExtra("teamName").toString()
//            player.number = number


            i("TEST:edit $edit")
            if (player.name.isEmpty()) {
                Snackbar.make(it,R.string.enter_player_name, Snackbar.LENGTH_LONG)
                    .show()
            } else {


                    i("TEST:edit $player")
                    app.players.create(player.copy())
                    db.collection("players")
                        .add(player)
                        .addOnSuccessListener { documentReference ->
                            Timber.i("DocumentSnapshot written with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Timber.i("Error adding document" +e)
                        }


            }
            i("Test: $player")
            setResult(RESULT_OK)
            finish()
        }


        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, this)
        }

        binding.playerLocation.setOnClickListener {
            val location = player.loc

            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_player, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.players.delete(player)
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
                            player.image = image

                            Picasso.get()
                                .load(player.image)
                                .into(binding.playerImage)
                            binding.chooseImage.setText(R.string.change_player_image)
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

}