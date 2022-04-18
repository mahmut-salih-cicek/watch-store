package com.sout.custom_apple_watch.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.WidgetActivity.WidgetActivity
import com.sout.custom_apple_watch.WidgetActivity.WidgetConfigActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mapview : MapView? = null


    private val filepath = "WeatherFile"
    internal var myExternalFile: File?=null
    private val isExternalStorageReadOnly: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            true
        } else {
            false
        }
    }
    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            true
        } else{
            false
        }
    }


    companion object{
        var xEkseni = ""
        var yEkseni  = ""
        var FUUL_URL = ""
        var yandexX = "";
        var yandexY = "";

    }

    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199


    var currentLocation : Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("ac2d8f9d-afa0-4ea7-89e5-03be0720c2ac");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_maps)

        /// yandex maps

        mapview = findViewById<View>(R.id.mapview) as MapView

        val mapKit = MapKitFactory.getInstance()
        mapKit.createLocationManager().requestSingleUpdate(object: LocationListener {
            override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {
                yandexX = location.position.latitude.toString();
                yandexY = location.position.longitude.toString();
                mapview!!.getMap().move(
                    CameraPosition(Point(location.position.latitude, location.position.longitude), 18.0f, 2.0f, 2.0f), Animation(
                        Animation.Type.SMOOTH, 0F),null)

                val mapObjects = mapview!!.map.mapObjects.addCollection()
                val mark: PlacemarkMapObject = mapObjects.addPlacemark(Point(location.position.latitude, location.position.longitude))
                mark.opacity = 1f
                mark.setIcon(ImageProvider.fromResource(this@MapsActivity, R.drawable.map64))
                mark.isDraggable = true

            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {

            }

        })




        /// style
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.mycolor1)
        val colorDrawable = ColorDrawable(Color.parseColor("#29397C"))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
        setTitle("Weather Location")
        centerTitle()
        ////


        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            enableLoc()
            getLoc2()

            val mapKit = MapKitFactory.getInstance()
            mapKit.createLocationManager().requestSingleUpdate(object: LocationListener {
                override fun onLocationUpdated(location: com.yandex.mapkit.location.Location) {
                    yandexX = location.position.latitude.toString();
                    yandexY = location.position.longitude.toString();
                    mapview!!.getMap().move(
                        CameraPosition(Point(location.position.latitude, location.position.longitude), 18.0f, 2.0f, 2.0f), Animation(
                            Animation.Type.SMOOTH, 0F),null)

                    val mapObjects = mapview!!.map.mapObjects.addCollection()
                    val mark: PlacemarkMapObject = mapObjects.addPlacemark(Point(location.position.latitude, location.position.longitude))
                    mark.opacity = 1f
                    mark.setIcon(ImageProvider.fromResource(this@MapsActivity, R.drawable.map64))
                    mark.isDraggable = true

                }

                override fun onLocationStatusUpdated(p0: LocationStatus) {

                }

            })
            save_yandex();

        }

        enableLoc()
        getLoc2()



    }

    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this@MapsActivity)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        currentLocation.let {
            showLocation(currentLocation!!)
        }
        val markerOptions = MarkerOptions().position(latLng).title("I Am Here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        // googleMap.setMyLocationEnabled(true);
        // googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.addMarker(markerOptions)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun enableLoc() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {}
                override fun onConnectionSuspended(i: Int) {
                    googleApiClient?.connect()
                }
            })
            .addOnConnectionFailedListener {
            }.build()
        googleApiClient?.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                        this,
                        REQUESTLOCATION
                    )
                } catch (e: IntentSender.SendIntentException) {
                }
            }
        }
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUESTLOCATION -> when (resultCode) {
                Activity.RESULT_OK -> CoroutineScope(Dispatchers.Main).launch {
                    delay(10000)
                    getLoc()
                }
                Activity.RESULT_CANCELED -> Log.d("abc","CANCEL")
            }
        }
    }


    suspend fun getLoc(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    fun getLoc2(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    fun setLocation(view: View){
        getLoc2()

    }

    fun setManuelLocation(view: View){
        var setManuelLocationIntent = Intent(this,ManuelLocationSetActivity::class.java)
        startActivity(setManuelLocationIntent)
    }

    fun showLocation(currentLocation: Location) {
        xEkseni = currentLocation.latitude.toString()
        yEkseni = currentLocation.longitude.toString()
        var LocationText = "$xEkseni"+","+"$yEkseni"

        text1.text = "enlem = "+xEkseni + "\n boylam " + yEkseni
      // println("enlem = "+currentLocation.latitude.toString() + "\n boylam " + currentLocation.longitude)

        myExternalFile = File(getExternalFilesDir(filepath), "weather1")
        try {
            val fileOutPutStream = FileOutputStream(myExternalFile)
            fileOutPutStream.write("$LocationText".toByteArray())
            fileOutPutStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
       // Toast.makeText(applicationContext,"data save : $LocationText", Toast.LENGTH_SHORT).show()

        getData(LocationText)

    }


    fun getData(LocationText:String) {

        println(WidgetConfigActivity.weatherKordinat)
        var client = OkHttpClient()
        var BASE_URL = "https://api.weatherapi.com/v1/current.json?key=e0f61b6abc674612a25202606210110&q=${LocationText}&aqi=yes"
        println("https://api.weatherapi.com/v1/current.json?key=e0f61b6abc674612a25202606210110&q=${LocationText}&aqi=yes")
        FUUL_URL = "https://api.weatherapi.com/v1/current.json?key=e0f61b6abc674612a25202606210110&q=${LocationText}&aqi=yes"
        var request = Request.Builder()
            .url(BASE_URL)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback{
            override fun onFailure(call: Call, e: IOException) {
                println(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                    var response = client.newCall(request).execute()
                    var jsonDataString = response.body?.string()
                    var jsonArray = JSONObject(jsonDataString)
                    var data1 = jsonArray.getJSONObject("current")
                    var data1_get = data1.getInt("temp_c")
                    var data2 = data1.getJSONObject("condition")
                    var data2_get = data2.getString("icon")
                    println("burda !!!!!"+data1_get)
                    println("burda !!!!!"+data2_get)
                    WidgetActivity.weatherC = data1_get.toString()
                    WidgetActivity.weatherIconUrl = data2_get

                }
            }

        })





    }

    private fun centerTitle() {
        val textViews: ArrayList<View> = ArrayList()
        window.decorView.findViewsWithText(textViews, title, View.FIND_VIEWS_WITH_TEXT)
        if (textViews.size > 0) {
            var appCompatTextView: AppCompatTextView? = null
            if (textViews.size === 1) {
                appCompatTextView = textViews[0] as AppCompatTextView
            } else {
                for (v in textViews) {
                    if (v.parent is Toolbar) {
                        appCompatTextView = v as AppCompatTextView
                        break
                    }
                }
            }
            if (appCompatTextView != null) {
                val params = appCompatTextView.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                appCompatTextView.layoutParams = params
                appCompatTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    fun save_Location (view:View){


        var LocationYandex = "$yandexX"+","+"$yandexY"

        try {
            val fileOutPutStream = FileOutputStream(myExternalFile)
            fileOutPutStream.write("$LocationYandex".toByteArray())
            fileOutPutStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        finish()
    }


    fun save_yandex(){

        var LocationYandex = "$yandexX"+","+"$yandexY"

        try {
            val fileOutPutStream = FileOutputStream(myExternalFile)
            fileOutPutStream.write("$LocationYandex".toByteArray())
            fileOutPutStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    override fun onStop() {
        super.onStop()
        mapview!!.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview!!.onStart()
        MapKitFactory.getInstance().onStart()
    }

}