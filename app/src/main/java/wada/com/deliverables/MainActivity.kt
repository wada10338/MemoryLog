package wada.com.deliverables

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest

class MainActivity : AppCompatActivity()
    ,OnMapReadyCallback
    ,GoogleMap.OnPoiClickListener
    ,OnMyLocationButtonClickListener
    ,OnMyLocationClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissionunauthorized)
        if(checkPermission()) {
            setContentView(R.layout.activity_main)
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map_fragment) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

    }

    private fun  checkPermission():Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    /**
     * Mapが使用可能になった場合に呼び出されるクラス
     */
    override fun onMapReady(googleMap: GoogleMap) {
        //MAPの初期設定
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        if(checkPermission()){
            googleMap.isMyLocationEnabled = true
        }
        googleMap.setOnMyLocationButtonClickListener { onMyLocationButtonClick() }



        //TODO DBから読み込み、データ数だけループしたい。
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )

    }

    override fun onPoiClick(poi: PointOfInterest) {
//        Toast.makeText(this, """Clicked: ${poi.name}
//            Place ID:${poi.placeId}
//            Latitude:${poi.latLng.latitude} Longitude:${poi.latLng.longitude}""",
//            Toast.LENGTH_SHORT
//        ).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        val text = "onMyLocationButtonClick"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        val text = "onMyLocationClick"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }


}
