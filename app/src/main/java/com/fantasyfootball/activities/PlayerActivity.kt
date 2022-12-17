//package com.fantasyfootball.activities
//
//import android.content.Intent
//import android.location.Address
//import android.location.Geocoder
//import android.net.Uri
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.fantasyfootball.R
//import com.fantasyfootball.databinding.ActivityTeamBinding
//import com.fantasyfootball.helpers.showImagePicker
//import com.fantasyfootball.main.MainApp
//import com.fantasyfootball.models.Location
//import com.fantasyfootball.models.PlayerModel
//import com.fantasyfootball.models.TeamModel
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.material.snackbar.Snackbar
//import com.google.firebase.firestore.GeoPoint
//import com.squareup.picasso.Picasso
//import timber.log.Timber
//import timber.log.Timber.i
//import java.io.IOException
//import java.util.*
//
//
//
//class PlayerActivity : AppCompatActivity() {
//
//
//    private lateinit var binding: ActivityTeamBinding
//    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
//    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
//
//    var edit = false
//    var player = PlayerModel()
//    //    var location = Location(52.245696, -7.139102, 15f)
//    lateinit var app: MainApp
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lateinit var locationFromAddress:LatLng
//        edit = false
//
//        binding = ActivityTeamBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.toolbarAdd.title = title
//        // setSupportActionBar(binding.toolbarAdd)
//        app = application as MainApp
//
//
//        if (intent.hasExtra("team_edit")) {
//            edit = true
//            player = intent.extras?.getParcelable("team_edit")!!
//            binding.playerName.setText(player.name)
//            binding.position.setText(player.position)
//            binding.locationName.setText(player.loc.toString())
//            //binding.locationName.setText(team.loc)
//            binding.btnAdd.setText(R.string.save_team)
//
//            Picasso.get()
//                .load(team.image)
//                .into(binding.teamImage)
//            if (team.image != Uri.EMPTY) {
//                binding.chooseImage.setText(R.string.change_team_image)
//            }
//        }
//
//        binding.btnAdd.setOnClickListener() {
////            team.id = UUID.randomUUID()
//            locationFromAddress = getLocationFromAddress(binding.locationName.text.toString())!!
//            team.loc=locationFromAddress
////            team.loc(getLocationFromAddress(binding.locationName.text.toString()))
//            team.name = binding.teamName.text.toString()
//            team.league = binding.league.text.toString()
//            team.formation = formation
//            i("TEST:edit $edit")
//            if (team.name.isEmpty()) {
//                Snackbar.make(it,R.string.enter_team_name, Snackbar.LENGTH_LONG)
//                    .show()
//            } else {
//                if (edit) app.teams.update(team.copy())
//                else {
//                    i("TEST:edit $team")
//                    app.teams.create(team.copy())
//                }
//            }
//            i("Test: $team")
//            setResult(RESULT_OK)
//            finish()
//        }
//
//
//        binding.chooseImage.setOnClickListener {
//            showImagePicker(imageIntentLauncher, this)
//        }
//
//        binding.teamLocation.setOnClickListener {
//            val location = team.loc
//            if (team.zoom != 0f) {
//
//                // location.zoom = team.zoom
//            }
//            val launcherIntent = Intent(this, MapActivity::class.java)
//                .putExtra("location", location)
//            mapIntentLauncher.launch(launcherIntent)
//        }
//
//        registerImagePickerCallback()
//        registerMapCallback()
//
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_team, menu)
//        if (edit) menu.getItem(0).isVisible = true
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.item_delete -> {
//                setResult(99)
//                app.teams.delete(team)
//                finish()
//            }
//            R.id.item_cancel -> {
//                finish()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//
//    private fun registerImagePickerCallback() {
//        imageIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { result ->
//                when(result.resultCode){
//                    RESULT_OK -> {
//                        if (result.data != null) {
//                            i("Got Result ${result.data!!.data}")
//                            val image = result.data!!.data!!
//                            contentResolver.takePersistableUriPermission(image,
//                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                            player.image = image
//
//                            Picasso.get()
//                                .load(player.image)
//                                .into(binding.playerImage)
//                            binding.chooseImage.setText(R.string.change_team_image)
//                        } // end of if
//                    }
//                    RESULT_CANCELED -> { } else -> { }
//                }
//            }
//    }
//
//    private fun registerMapCallback() {
//        mapIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { result ->
//                when (result.resultCode) {
//                    RESULT_OK -> {
//                        if (result.data != null) {
//                            i("Got Location ${result.data.toString()}")
//                            val location = result.data!!.extras?.getParcelable<LatLng>("location")!!
//                            i("Location == $location")
////                            team.lat = location.lat
////                            team.lng = location.lng
////                            team.zoom = location.zoom
//                        } // end of if
//                    }
//                    RESULT_CANCELED -> { } else -> { }
//                }
//            }
//    }
//
//    fun getLocationFromAddress(strAddress: String?): LatLng? {
//        val coder = Geocoder(this)
//        val address: List<Address>?
//        var p1: GeoPoint? = null
//        try {
//            address = coder.getFromLocationName(strAddress!!, 5)
//            if (address == null) {
//                return null
//            }
//            val location: Address = address[0]
//            location.getLatitude()
//            location.getLongitude()
//
//            return LatLng(location.getLatitude(),location.getLongitude())
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//}