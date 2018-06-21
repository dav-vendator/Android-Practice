package Utilities

import com.google.android.gms.maps.model.LatLng


fun getUrl(origin: LatLng, dest: LatLng): String {

    // Origin of route
    val str_origin = "origin=" + origin.latitude + "," + origin.longitude

    // Destination of route
    val str_dest = "destination=" + dest.latitude + "," + dest.longitude


    // Sensor enabled
    val sensor = "sensor=false"

    // Building the parameters to the web service
    val parameters = "$str_origin&$str_dest&$sensor"

    // Output format
    val output = "json"

    //API key
    val api="AIzaSyD3g8YiUorxpe5jr6KKc97DofrNzdvqi24"
    // Building the url to the web service


    return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=$api"
}

