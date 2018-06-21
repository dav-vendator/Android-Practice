package async_tasks

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class FetchUrl(val mMap:GoogleMap,val route:ArrayList<LatLng>) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg url: String): String {

        // For storing data from web service
        var data = ""

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0])
            Log.d("Background Task data", data)
        } catch (e: Exception) {
            Log.d("Background Task", e.toString())
        }

        return data
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        val parserTask = ParserTask(mMap,route)
        // Invokes the thread for parsing the JSON data
        parserTask.execute(result)
    }

    fun getRoutes():ArrayList<LatLng>? = if (route.size == 0) null else route
}


@Throws(IOException::class)
private fun downloadUrl(strUrl: String): String {
    var data = ""
    var iStream: InputStream? = null
    var urlConnection: HttpURLConnection? = null
    try {
        val url = URL(strUrl)

        // Creating an http connection to communicate with url
        urlConnection = url.openConnection() as HttpURLConnection

        // Connecting to url
        urlConnection!!.connect()

        // Reading data from url
        iStream = urlConnection!!.getInputStream()

        val br = BufferedReader(InputStreamReader(iStream))

        val sb = StringBuffer()

        var line = br.readLine()
        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }

        data = sb.toString()
        Log.d("downloadUrl", data)
        br.close()

    } catch (e: Exception) {
        Log.d("Exception", e.toString())
    } finally {
        iStream!!.close()
        urlConnection!!.disconnect()
    }
    return data
}