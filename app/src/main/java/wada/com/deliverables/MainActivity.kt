package wada.com.deliverables

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity()
    ,OnMapReadyCallback
    ,GoogleMap.OnPoiClickListener
    ,OnMyLocationButtonClickListener
    ,OnMyLocationClickListener
    ,LocationListener {
    private var Check = CheckData()
    private lateinit var db:MemoryDatabase
    private lateinit var dao:MemoryDao
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1234
    }
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var setClient: SettingsClient
    private lateinit var locSetReq: LocationSettingsRequest
    private lateinit var locCallback: LocationCallback
    private lateinit var locReq:LocationRequest
    private lateinit var gMap:GoogleMap




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //DBの実装
        this.db = Room.databaseBuilder(
            applicationContext,
            MemoryDatabase::class.java,
            "Memory.db"
        ).build()
        this.dao=this.db.MemoryDao()

        //位置情報が許可されていない場合は許可願いの画面。
        if(Check.isLocationAvailable(applicationContext)) {
            start()
        }else{
            setContentView(R.layout.activity_permissionunauthorized)
            requestPermission()
            if(Check.isLocationAvailable(applicationContext)){
                start()
            }

        }

    }
    fun start(){
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //ListActivityへの移行ボタン
        val listAc=findViewById<Button>(R.id.MemoryListButton)
        val intent=Intent(this,ListActivity::class.java)
        listAc.setOnClickListener { startActivity(intent) }
        //保存ボタン（ダイアログ表示ボタン）
        val saveButton=findViewById<Button>(R.id.MemorySaveButton)
        saveButton.setOnClickListener { showDialog() }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        setClient=LocationServices.getSettingsClient(this)

        //  位置情報取得時の処理の準備
        locCallback= object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val loc = locationResult.lastLocation
                gMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(loc.latitude,loc.longitude),16f
                    )
                )

            }
        }

        //位置リクエストの生成
        locReq=LocationRequest.create().apply {
            interval=5000
            fastestInterval=1000
            priority=LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        //位置情報に関する設定リクエスト情報を生成
        val builder=LocationSettingsRequest.Builder()
        locSetReq=builder.addLocationRequest(locReq).build()

        //  位置情報の監視
        startWatchLocation()
    }

    private fun startWatchLocation() {
        //位置情報の設定の確認
        setClient.checkLocationSettings(locSetReq)
            .addOnSuccessListener(this)
                succ@{
                    //permission確認　なぜ何度も書かなあかんのや...
                    if(ActivityCompat.checkSelfPermission(
                            this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                        return@succ
                    }
                    //位置情報の取得開始
                    fusedClient.requestLocationUpdates(
                        locReq,locCallback, Looper.getMainLooper()
                    )
                }
            .addOnFailureListener(
                this
            ){e ->Log.d("MapMyLocation",e.message!!)}
    }

    override fun onStart() {
        super.onStart()


    }

    override fun onPause() {
        super.onPause()
        fusedClient.removeLocationUpdates(locCallback)
    }

    override fun onResume() {
        super.onResume()
        startWatchLocation()
    }


    /**
     * ダイアログが押された場合
     * DBへの挿入などを行う
     */

    @OptIn(DelicateCoroutinesApi::class)
    private fun showDialog() {
        //debug
        val sdf = SimpleDateFormat("yyyy年MM月dd日　HH:mm", Locale.JAPANESE)
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
                //TODO DB挿入　
                GlobalScope.launch {
                    withContext(Dispatchers.IO){
                        val memory=Memory(
                            id=0
                            ,titleEditText.text.toString()
                            , contentsEditText.text.toString()
                            ,35.326
                            , 37.5643
                            ,sdf.format(Date())
                        )
                        dao.insert(memory)
                        Log.d("insertDBbLog",memory.toString())
                    }
                    withContext(Dispatchers.Main){
                        Toast.makeText(applicationContext,"正常！",Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                Toast.makeText(this, "値が不正です。入力しなおしてください。", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dialog.show()
    }





    /**
     * Mapが使用可能になった場合に呼び出されるクラス
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        gMap=googleMap
        //MAPの初期設定
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled=true
       // googleMap.moveCamera(CameraUpdateFactory.newLatLng(緯度　経度))

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

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }



    private fun requestPermission() {
        val permissionAccessCoarseLocationApproved =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
            val backgroundLocationPermissionApproved = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED

            if (backgroundLocationPermissionApproved) {
                // フォアグラウンドとバックグランドのバーミッションがある
            } else {
                // フォアグラウンドのみOKなので、バックグラウンドの許可を求める
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            }
        } else {
            // 位置情報の権限が無いため、許可を求める
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        }

    }




}
