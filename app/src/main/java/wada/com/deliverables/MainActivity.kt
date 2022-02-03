package wada.com.deliverables

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest


class MainActivity : AppCompatActivity()
    ,OnMapReadyCallback
    ,GoogleMap.OnPoiClickListener
    ,OnMyLocationButtonClickListener
    ,OnMyLocationClickListener {
    private var Check = checkString()

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
    override fun onStart() {
        super.onStart()
        //ListActivityへの移行ボタン
        val listAc=findViewById<Button>(R.id.MemoryListButton)
        val intent=Intent(this,ListActivity::class.java)
        listAc.setOnClickListener { startActivity(intent) }
        //保存ボタン（ダイアログ表示ボタン）
        val saveButton=findViewById<Button>(R.id.MemorySaveButton)
        saveButton.setOnClickListener { showDialog() }

    }

    override fun onResume() {
        super.onResume()

    }




    private fun showDialog() {
        //debug
        Toast.makeText(applicationContext, "セーブボタン押されたぞ", Toast.LENGTH_LONG).show()
        val dialog=Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)
        //入力されたタイトル
        val titleEditText = dialog.findViewById<View>(R.id.titleEditText) as EditText
        /* 入力された内容 */
        val contentsEditText = dialog.findViewById<View>(R.id.contentsEditText) as EditText
        /* 戻るボタン */
        val closeDialogButton = dialog.findViewById<View>(R.id.closeButton) as Button
        closeDialogButton.setOnClickListener { dialog.cancel() }
        //保存ボタン（重要）
        val saveMemoryButton = dialog.findViewById<View>(R.id.saveButton) as Button
        saveMemoryButton.setOnClickListener {
            //不正な入力チェック
            if (Check.canDataIn(titleEditText.text.toString(), contentsEditText.text.toString())) {
                //TODO DBに挿入
            } else {
                Toast.makeText(this, "値が不正です。入力しなおしてください。", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dialog.show()
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        //MAPの初期設定
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled=true

        //お店がクリックされた時のリスナー（onPoiClickが呼び出される）
        googleMap.setOnPoiClickListener(this)
        //現在地をタップした時のリスナ（onMyLocationClickが呼びだされる）
        googleMap.setOnMyLocationButtonClickListener(this)
        //見ている場所から現在地に戻るボタン（右上の）がタップされたときのリスナ（onMyLocationButtonClickがよびだされる）
        googleMap.setOnMyLocationClickListener(this)

        //TODO DBから読み込み、データ数だけループしたい。
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )

    }

    /**
     *地図上のお店をタップした時に呼び出される
     *今は名前、ID、緯度、経度トーストで表示するようにしている。
     */
    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(
            this,
            """${poi.name}
            Place ID:${poi.placeId}
            Latitude:${poi.latLng.latitude} Longitude:${poi.latLng.longitude}""",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * 現在地に戻るボタンが押された場合の処理
     */
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(applicationContext, "現在地に戻る", Toast.LENGTH_LONG).show()
        return false
    }

    /**
     * 自分の現在位置がタップされた時に呼び出される
     * 今は自分の位置がトースト表示されるようになっている
     */
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "自分の位置を押したね:\n$p0", Toast.LENGTH_LONG)
            .show()
    }



}
