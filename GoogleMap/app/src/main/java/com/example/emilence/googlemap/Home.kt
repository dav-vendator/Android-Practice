package com.example.emilence.googlemap

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_home.*
import com.google.android.gms.maps.CameraUpdateFactory
import async_tasks.FetchUrl
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.model.PolylineOptions


class Home : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    val markerPoints: ArrayList<LatLng>? = ArrayList()
    val routeData : ArrayList<LatLng> = ArrayList()
    private var map: GoogleMap? = null
    private val PLACE_PICKER_REQUEST: Int = 452
    private var currentDestination = LatLng(0.0,0.0)
    private var currentLocation = LatLng(0.0,0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initPermission()
        setSupportActionBar(toolbar)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK)
            displayPlace(PlacePicker.getPlace(data, this));

    }

    private fun displayPlace(place: Place?) {
        if (place == null)
            Toast.makeText(applicationContext,"No place was selected",Toast.LENGTH_LONG)
                    .show()
        else if (map != null) {
            map!!.clear()
          map!!.addMarker(MarkerOptions().position(place.latLng).title("Destination")
               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
          currentDestination = place.latLng
            val origin = getCurrentLocation()
            map!!.addMarker(MarkerOptions().position(origin).title("Start")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            val dest = currentDestination
            val url = Utilities.getUrl(origin, dest)
            Log.d("onMapClick", url.toString())

            val FetchUrl = FetchUrl(this.map!!,routeData)
            FetchUrl.execute(url)
            map!!.moveCamera(CameraUpdateFactory.newLatLng(origin))
            map!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
        }
    }

    fun findLocation(view: View) {
        val builder = PlacePicker.IntentBuilder()

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            Log.d("PlacesAPI Demo", "GooglePlayServicesRepairableException thrown")
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.d("PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown")
        }
    }

    private fun initPermission() {
        Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
                                .getMapAsync(this@Home)
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        Toast.makeText(applicationContext,"This permission is required",Toast.LENGTH_LONG).show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?){
                        Toast.makeText(applicationContext,"Please activate the permission in settings",
                                Toast.LENGTH_LONG).show()
                    }

                }).check()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        this.map = p0
        map!!.setMyLocationEnabled(true)
        map!!.setOnMyLocationButtonClickListener(this)
    }



    //Permissions are being handled by Dexter in @see [initPermission]
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): LatLng{
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        val providers = locationManager.allProviders
        var bestLocation : Location? = null
        for(provider in providers){
            val location = locationManager.getLastKnownLocation(provider)
            if (location == null){
                continue
            }else if(bestLocation == null || location.accuracy > bestLocation!!.accuracy){
                bestLocation = location
            }
        }
        if (bestLocation != null) {
            val latitude = bestLocation.getLatitude();
            val longitude = bestLocation.getLongitude();
            return LatLng(latitude, longitude)
        }
        return LatLng(0.0,0.0)
    }

    fun drawLine(routesData: ArrayList<LatLng>, start : LatLng ){
        val line = PolylineOptions()
        line.addAll(routesData)
        map!!.clear()
        map!!.addPolyline(line)
    }

    override fun onMyLocationButtonClick(): Boolean {
        Log.e("ARRR","Clicked")
        if(map != null) {
            val myPosition = getCurrentLocation()
            map!!.moveCamera(CameraUpdateFactory.newLatLng(myPosition))
            Toast.makeText(this, myPosition.latitude.toString() + " : " + myPosition.longitude.toString(), Toast.LENGTH_LONG).show()
            map!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
        }
        return true
    }

    fun startTrack(view : View){
       if(routeData.isEmpty() || map == null){
           Toast.makeText(applicationContext,"Route data is empty or Map is yet to be loaded",
                   Toast.LENGTH_LONG).show()
       }else{
           map!!.setOnMyLocationChangeListener { tracker(it) }
       }
    }

    fun tracker(location : Location){
        /*
        1) compare location with route data at the top
          if distance is less then remove that point and update map
          else
             set it as new start and request for route data again
              update map
         */
        val currentLocation = getCurrentLocation()
        val deltaLat = location.latitude - routeData[0].latitude
        val deltaLong = location.longitude - routeData[0].longitude
        var newStart:LatLng? = null
        if(deltaLat > 50 || deltaLong > 50 ){
            newStart = LatLng(location.longitude,location.latitude)
            FetchUrl(map!!,routeData).execute(Utilities.getUrl(newStart,currentDestination))
        }else{
            newStart = routeData[1]
            if(routeData.size <= 1){
                Toast.makeText(applicationContext,"No more points left",Toast.LENGTH_LONG)
                        .show()
            }else {
                routeData.removeAt(0)
            }
            drawLine(routeData,newStart)
        }

    }

}
