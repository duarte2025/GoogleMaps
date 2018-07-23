package br.eng.iris.googlemaps

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


class MainActivity : AppCompatActivity() {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private val fullParameters: FullParameters = FullParameters("http://www.iris.eng.br/~diogo/bustracking/get.php", OperationMethod.GET )
    private val response: HttpResponse = BaseRepository().execute(fullParameters)
    val location: LocEntity = Gson().fromJson<LocEntity>(response.jsonResponse, object : TypeToken<LocEntity>() {}.type)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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