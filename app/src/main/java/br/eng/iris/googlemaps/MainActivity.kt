package br.eng.iris.googlemaps

import android.location.Location
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.eng.iris.googlemaps.entity.FullParameters
import br.eng.iris.googlemaps.entity.HttpResponse
import br.eng.iris.googlemaps.entity.LocEntity
import br.eng.iris.googlemaps.infra.OperationMethod
import br.eng.iris.googlemaps.repository.BaseRepository
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var location = LocEntity("", "", "0.0", "0.0", "")
        class GetWeatherTask() : AsyncTask<Unit, Unit, String>() {


            override fun doInBackground(vararg params: Unit?): String? {
                val url = URL("http://www.iris.eng.br/~diogo/bustracking/get.php")
                val httpClient = url.openConnection() as HttpURLConnection
                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                    try {
                        val stream = BufferedInputStream(httpClient.inputStream)
                        return readStream(inputStream = stream)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        httpClient.disconnect()
                    }
                } else {
                    println("ERROR ${httpClient.responseCode}")
                }
                return null
            }

            fun readStream(inputStream: BufferedInputStream): String {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                bufferedReader.forEachLine { stringBuilder.append(it) }
                return stringBuilder.toString()
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                location = Gson().fromJson<LocEntity>(result, object : TypeToken<LocEntity>() {}.type)

            }
        }
        GetWeatherTask().execute()

        setContentView(R.layout.activity_main)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            val location1 = LatLng(location.latitude.toDouble(), location.longitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(location1).title("Ligeirão"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1, 3f))
            } )
    }
}