package async_tasks

import Utilities.DataParser
import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import java.nio.file.Files.size
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.GoogleMap


class ParserTask(val mMap: GoogleMap,val route : ArrayList<LatLng>) : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {


    override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {

        val jObject: JSONObject
        var routes: List<List<HashMap<String, String>>>? = null

        try {
            jObject = JSONObject(jsonData[0])
            Log.d("ParserTask", jsonData[0])
            val parser : DataParser = DataParser()
            Log.d("ParserTask", parser.toString())
            // Starts parsing data
            routes = parser.parse(jObject)
            Log.d("ParserTask", "Executing routes")
            Log.d("ParserTask", routes!!.toString())

        } catch (e: Exception) {
            Log.d("ParserTask", e.toString())
            e.printStackTrace()
        }

        return routes
    }

    // Executes in UI thread, after the parsing process
    override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
        var lineOptions: PolylineOptions? = null

        // Traversing through all the routes
        for (i in result.indices) {
            lineOptions = PolylineOptions()

            // Fetching i-th route
            val path = result[i]

            // Fetching all the points in i-th route
            for (j in path.indices) {
                val point = path[j]

                val lat = java.lang.Double.parseDouble(point["lat"])
                val lng = java.lang.Double.parseDouble(point["lng"])
                val position = LatLng(lat, lng)

                route.add(position)
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(route)
            lineOptions.width(10f)
            lineOptions.color(Color.RED)

            Log.d("onPostExecute", "onPostExecute lineoptions decoded")

        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            mMap.addPolyline(lineOptions)
        } else {
            Log.d("onPostExecute", "without Polylines drawn")
        }
    }
}