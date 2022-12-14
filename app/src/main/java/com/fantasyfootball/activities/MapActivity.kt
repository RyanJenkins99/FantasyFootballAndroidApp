package com.fantasyfootball.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import com.fantasyfootball.R
import com.fantasyfootball.databinding.ActivityMapBinding
import com.fantasyfootball.models.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions



class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private lateinit var location:LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        location = intent.extras?.getParcelable<LatLng>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val loc = location
        val options = MarkerOptions()
            .title("Team")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.setOnMarkerDragListener(this)
        map.setOnMarkerClickListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15f))
    }
//

    override fun onMarkerDrag(p0: Marker) {
        TODO("Not yet implemented")
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = location
        marker.snippet = "GPS : $loc"
        return false
    }

    override fun onMarkerDragEnd(marker: Marker) {

//        location.lat = marker.position.latitude
//        location.lng = marker.position.longitude
//        location.zoom = map.cameraPosition.zoom
    }

    override fun onMarkerDragStart(p0: Marker) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}