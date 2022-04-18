package com.sout.custom_apple_watch.WidgetActivity

import android.annotation.SuppressLint
import android.app.*
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.squareup.picasso.Picasso
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.Recycler.WatchAdapter
import com.sout.custom_apple_watch.Recycler.WatchAdapter.Companion.Watch3Chechk
import com.sout.custom_apple_watch.View.MapsActivity.Companion.FUUL_URL
import com.sout.custom_apple_watch.WidgetActivity.WidgetConfigActivity.Companion.start
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.*
import java.lang.Runnable
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*









const val WIDGET_SYNC = "WIDGET_SYNC"


class WidgetActivity : AppWidgetProvider() {


    private fun startService(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(Intent(context, MyService::class.java))
        } else {
            context.startService(Intent(context, MyService::class.java))
        }
    }

    private fun startService2(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(Intent(context, MyService2::class.java))
        } else {
            context.startService(Intent(context, MyService2::class.java))
        }
    }

    private fun startService3(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(Intent(context, MyService3::class.java))
        } else {
            context.startService(Intent(context, MyService3::class.java))
        }
    }


    lateinit var preference: MyPreference

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {


        if (!::preference.isInitialized){
            preference = MyPreference(context)

        }

        val ids = preference.getWidgetIds()
        for (appWidgetId in appWidgetIds) {
            ids.add(appWidgetId.toString())



            updateAppWidget(context, appWidgetManager, appWidgetId)

        }
        preference.updateWidgetIds(ids)
    }

    override fun onEnabled(context: Context) {



        if (WatchAdapter.Watch3Chechk == "Counter Face"){
            startService(context)
        }

        if (WatchAdapter.Watch3Chechk == "Community Face"){
            startService2(context)
        }


        if (WatchAdapter.Watch3Chechk == "Remote Face"){
            startService3(context)
        }




        val intent = Intent(context, WidgetActivity::class.java)
        intent.action = WIDGET_SYNC
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val now = Calendar.getInstance()
        now.set(Calendar.SECOND, 0)
        now.add(Calendar.MINUTE, 1)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC, now.timeInMillis, 60000, pendingIntent)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        context.stopService(Intent(context, MyService::class.java))
        context.stopService(Intent(context, MyService2::class.java))
        context.stopService(Intent(context, MyService3::class.java))
    }



    class MyService : Service(),SensorEventListener{



        companion object{
            var currentSteps2 = 0.0
            var currentSteps3 = 0


            var ListDelay : Long = 1000
            var ListCount = 0
        }

        private val TAG = MyService::class.java.simpleName
        private val mHandler = Handler()
        private var mScreenFlag = true
        var gStarted = false

        // we have assigned sensorManger to nullable
        private var sensorManager: SensorManager? = null

        // Creating a variable which will give the running status
        // and initially given the boolean value as false
        private var running = false

        // Creating a variable which will counts total steps
        // and it has been given the value of 0 float
        private var totalSteps = 0f

        // Creating a variable  which counts previous total
        // steps and it has also been given the value of 0 float
        private var previousTotalSteps = 0f


        private fun updateWidget() {

            println("-----services 1 is start------")

            if (start){


                // Adding a context of SENSOR_SERVICE aas Sensor Manager
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

                running = true
                val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
                sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)



                getBatter(this)





                ///////////////////////////////REMOTE VİEV INITIALİZLARI////////////////////////////
                val mViews = RemoteViews(packageName, R.layout.widget_activity)
                val mThisWidget = ComponentName(this, WidgetActivity::class.java)

                if (face3Sayac <5){
                    if(WatchAdapter.Watch3Chechk == "Counter Face"){
                        mViews.setViewVisibility(R.id.Face3_Main,View.VISIBLE)
                    }
                    face3Sayac= face3Sayac+1;
                }


                mViews.setTextViewText(R.id.face3_battery,batteryLevel.toString())
                mViews.setTextViewText(R.id.face3_kcal, (currentSteps3*0.05).toString())
                mViews.setTextViewText(R.id.face3_step,currentSteps3.toString())






                MyService.ListDelay = 1000




                println("-------LİST COUNT------>>>>>>>"+ MyService.ListCount)


                    if(!Shared(this).getFace3Data()){
                        mViews.setViewVisibility(R.id.lisanas,View.VISIBLE)
                    }



                ///////Son Guncelleme /////kodu
                val mManager = AppWidgetManager.getInstance(this@MyService)
                mManager.updateAppWidget(mThisWidget, mViews)
                ///////////////////////////////////////////////////


            };

        };








        override fun onSensorChanged(event: SensorEvent?) {

            // Calling the TextView that we made in activity_main.xml
            // by the id given to that TextView
            if (running) {
                totalSteps = event!!.values[0]
                // Current steps are calculated by taking the difference of total steps
                // and previous steps
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                currentSteps2 = currentSteps.toDouble()
                val decimal = BigDecimal(currentSteps2).setScale(0, RoundingMode.HALF_EVEN)
                currentSteps3 = decimal.toInt()
            }
        }


        private fun saveData() {

            // Shared Preferences will allow us to save
            // and retrieve data in the form of key,value pair.
            // In this function we will save data
            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()
            editor.putFloat("key1", previousTotalSteps)
            editor.apply()
        }

        private fun loadData() {

            // In this function we will retrieve data
            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val savedNumber = sharedPreferences.getFloat("key1", 0f)

            // Log.d is used for debugging purposes
            Log.d("MainActivity", "$savedNumber")

            previousTotalSteps = savedNumber
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // We do not have to write anything in this function for this app
        }











        private val r = object : Runnable {
            override fun run() {
                updateWidget()
                if (mScreenFlag) {
                    val now = System.currentTimeMillis()
                    val delay = MyService.ListDelay - (now % 1000)
                    mHandler.postDelayed(this, delay)
                }
            }
        }

        private fun update() {
            mHandler.post(r)
        }


        @SuppressLint("WrongConstant")
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            super.onStartCommand(intent, flags, startId)
            Log.d(TAG, "onStart")
            if (gStarted) {
                Log.d(TAG, "already started")
                return Service.START_NOT_STICKY
            }
            Log.d(TAG, "start")
            gStarted = true

            if (Build.VERSION.SDK_INT >= 26) {
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    "channel_1",
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_NONE
                )
                ///   notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
                channel.lightColor = Color.GREEN
                channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                manager.createNotificationChannel(channel)
                val notification = NotificationCompat.Builder(this, "channel_1")
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(getString(R.string.notification_text))
                    .build()

                startForeground(1, notification)

            }

            val bcr = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.d(TAG, "onReceive context=${context} intent=${intent}")
                    if (intent?.action.equals(Intent.ACTION_SCREEN_ON)) {
                        Log.d(TAG, "onReceive ACTION_SCREEN_ON")
                        mScreenFlag = true
                        update()
                    } else if (intent?.action.equals(Intent.ACTION_SCREEN_OFF)) {
                        Log.d(TAG, "onReceive ACTION_SCREEN_OFF")
                        mScreenFlag = false
                    }
                }
            }

            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            applicationContext.registerReceiver(bcr, filter);

            update()

            return Service.START_STICKY
        }

        override fun onCreate() {
            super.onCreate()
            Log.d(TAG, "onCreate")
        }

        override fun onDestroy() {
            super.onDestroy()
            mHandler.removeCallbacks(r)
            gStarted = false
            Log.d(TAG, "onDestroy")
        }

        override fun onBind(intent: Intent): IBinder? {
            return null
        }












    }


    class MyService2 : Service(){



        companion object{
            var ListDelay : Long = 1000
            var ListCount = 0


        }

        private val TAG = MyService::class.java.simpleName
        private val mHandler = Handler()
        private var mScreenFlag = true
        var gStarted = false



        private fun updateWidget() {

            if (start){



                println("-----services 2 is start--- ")


                ///////////////////////////////REMOTE VİEV INITIALİZLARI////////////////////////////
                val views2 = RemoteViews(packageName, R.layout.widget_activity)
                val mThisWidget = ComponentName(this, WidgetActivity::class.java)

                if (face3Sayac <5){
                    if(WatchAdapter.Watch3Chechk == "Community Face"){
                        views2.setViewVisibility(R.id.Face1_Main,View.VISIBLE)
                    }
                    face3Sayac= face3Sayac+1;
                }



                //////////////// GET DATA////////////////////
                var myUrl = Shared(this).getData()
                getData(this,myUrl)
                getBatter(this)
                batteryTemperature(this)
/////////////////////////////////////////////



///// face1 theme

//// Battery Icon
                if(batteryLevel == 100){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.VISIBLE)
                }else if (batteryLevel >= 94 && batteryLevel != 100){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 80 && batteryLevel < 94) {
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 68 && batteryLevel < 80){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 42 && batteryLevel < 68){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 28 && batteryLevel<42){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 14 && batteryLevel<28){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }

/// Battery text
                views2.setTextViewText(R.id.battery_text, "%"+batteryLevel.toString())

///Battery Temp Text
                views2.setTextViewText(R.id.battery_Temp, "$batteryTemp°C")

///weather icon weather c

                views2.setTextViewText(R.id.appwidget_text, "$sonWeatherC°C")

                Picasso.get().load("https:$sonIcon").into(object : com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        views2.setImageViewBitmap(R.id.weather_icon,bitmap)
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                })


////////////////////////////////////////////////////////////////////



                println("my url =  $myUrl \n\n son icon = $sonIcon \n\n sonweatherc = $sonWeatherC \n ")


                if (ListCount >= 2){
                    ListDelay = 2000

                }
                ListCount++

                if (ListCount >= 5){
                    ListDelay = 60000

                }
                println("-------LİST COUNT------>>>>>>>"+ListCount)

                if (ListCount>=6){
                    if(!Shared(this).getFace1Data()){
                        views2.setViewVisibility(R.id.lisanas,View.VISIBLE)

                    }

                }




                ///////Son Guncelleme /////kodu
                val mManager = AppWidgetManager.getInstance(this@MyService2)
                mManager.updateAppWidget(mThisWidget, views2)
                ///////////////////////////////////////////////////


            };



        };




        private val r = object : Runnable {
            override fun run() {
                updateWidget()
                if (mScreenFlag) {
                    val now = System.currentTimeMillis()
                    val delay = ListDelay - (now % 1000)
                    mHandler.postDelayed(this, delay)
                }
            }
        }

        private fun update() {
            mHandler.post(r)
        }


        @SuppressLint("WrongConstant")
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            super.onStartCommand(intent, flags, startId)
            Log.d(TAG, "onStart")
            if (gStarted) {
                Log.d(TAG, "already started")
                return Service.START_NOT_STICKY
            }
            Log.d(TAG, "start")
            gStarted = true

            if (Build.VERSION.SDK_INT >= 26) {
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    "channel_1",
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_NONE
                )
                ///   notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
                channel.lightColor = Color.GREEN
                channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                manager.createNotificationChannel(channel)
                val notification = NotificationCompat.Builder(this, "channel_1")
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(getString(R.string.notification_text))
                    .build()

                startForeground(1, notification)

            }

            val bcr = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.d(TAG, "onReceive context=${context} intent=${intent}")
                    if (intent?.action.equals(Intent.ACTION_SCREEN_ON)) {
                        Log.d(TAG, "onReceive ACTION_SCREEN_ON")
                        mScreenFlag = true
                        update()
                    } else if (intent?.action.equals(Intent.ACTION_SCREEN_OFF)) {
                        Log.d(TAG, "onReceive ACTION_SCREEN_OFF")
                        mScreenFlag = false
                    }
                }
            }

            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            applicationContext.registerReceiver(bcr, filter);

            update()

            return Service.START_STICKY
        }

        override fun onCreate() {
            super.onCreate()
            Log.d(TAG, "onCreate")
        }

        override fun onDestroy() {
            super.onDestroy()
            mHandler.removeCallbacks(r)
            gStarted = false
            Log.d(TAG, "onDestroy")
        }

        override fun onBind(intent: Intent): IBinder? {
            return null
        }












    }


    class MyService3 : Service(){



        companion object{
            var ListDelay : Long = 1000
            var ListCount = 0
        }

        private val TAG = MyService::class.java.simpleName
        private val mHandler = Handler()
        private var mScreenFlag = true
        var gStarted = false



        private fun updateWidget() {


            if (start){
                println("-----services 3 is start-----")
                val views2 = RemoteViews(packageName, R.layout.widget_activity)
                val mThisWidget = ComponentName(this, WidgetActivity::class.java)



                if (face3Sayac <5){
                    if(WatchAdapter.Watch3Chechk == "Remote Face"){
                        views2.setViewVisibility(R.id.Face2_Main,View.VISIBLE)
                    }
                    face3Sayac= face3Sayac+1;
                }



                //// face 2 theme//////////
                getBatter(this)
                val calendar = Calendar.getInstance()
                val day = calendar[Calendar.DAY_OF_WEEK]


                when(day){
                    Calendar.MONDAY -> views2.setViewVisibility(R.id.face2_day1,View.VISIBLE)
                    Calendar.TUESDAY -> views2.setViewVisibility(R.id.face2_day2,View.VISIBLE)
                    Calendar.WEDNESDAY -> views2.setViewVisibility(R.id.face2_day3,View.VISIBLE)
                    Calendar.THURSDAY -> views2.setViewVisibility(R.id.face2_day4,View.VISIBLE)
                    Calendar.FRIDAY -> views2.setViewVisibility(R.id.face2_day5,View.VISIBLE)
                    Calendar.SATURDAY -> views2.setViewVisibility(R.id.face2_day6,View.VISIBLE)
                    Calendar.SUNDAY -> views2.setViewVisibility(R.id.face2_day7,View.VISIBLE)
                }

                views2.setTextViewText(R.id.face2_battery_level,batteryLevel.toString())
                views2.setProgressBar(R.id.circularProgressbar,100,batteryLevel,false)

                ////////////////////////////



                if (MyService3.ListCount >= 2){
                    MyService3.ListDelay = 2000

                }
                MyService3.ListCount++

                if (MyService3.ListCount >= 5){
                    MyService3.ListDelay = 60000

                }
                println("-------LİST COUNT------>>>>>>>"+ MyService3.ListCount)

                if (MyService3.ListCount >=6){
                    if(!Shared(this).getFace2Data()){
                        views2.setViewVisibility(R.id.lisanas,View.VISIBLE)

                    }

                }



                ///////Son Guncelleme /////kodu
                val mManager = AppWidgetManager.getInstance(this@MyService3)
                mManager.updateAppWidget(mThisWidget, views2)
                ///////////////////////////////////////////////////

            }














        };




        private val r = object : Runnable {
            override fun run() {
                updateWidget()
                if (mScreenFlag) {
                    val now = System.currentTimeMillis()
                    val delay = MyService3.ListDelay - (now % 1000)
                    mHandler.postDelayed(this, delay)
                }
            }
        }

        private fun update() {
            mHandler.post(r)
        }


        @SuppressLint("WrongConstant")
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            super.onStartCommand(intent, flags, startId)
            Log.d(TAG, "onStart")
            if (gStarted) {
                Log.d(TAG, "already started")
                return Service.START_NOT_STICKY
            }
            Log.d(TAG, "start")
            gStarted = true

            if (Build.VERSION.SDK_INT >= 26) {
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    "channel_1",
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_NONE
                )
                ///   notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
                channel.lightColor = Color.GREEN
                channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                manager.createNotificationChannel(channel)
                val notification = NotificationCompat.Builder(this, "channel_1")
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(getString(R.string.notification_text))
                    .build()

                startForeground(1, notification)

            }

            val bcr = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    Log.d(TAG, "onReceive context=${context} intent=${intent}")
                    if (intent?.action.equals(Intent.ACTION_SCREEN_ON)) {
                        Log.d(TAG, "onReceive ACTION_SCREEN_ON")
                        mScreenFlag = true
                        update()
                    } else if (intent?.action.equals(Intent.ACTION_SCREEN_OFF)) {
                        Log.d(TAG, "onReceive ACTION_SCREEN_OFF")
                        mScreenFlag = false
                    }
                }
            }

            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            applicationContext.registerReceiver(bcr, filter);

            update()

            return Service.START_STICKY
        }

        override fun onCreate() {
            super.onCreate()
            Log.d(TAG, "onCreate")
        }

        override fun onDestroy() {
            super.onDestroy()
            mHandler.removeCallbacks(r)
            gStarted = false
            Log.d(TAG, "onDestroy")
        }

        override fun onBind(intent: Intent): IBinder? {
            return null
        }





    }







    override fun onReceive(context: Context, intent: Intent?) {

        println("-------------------ON RECEİVE IS CALL------------------")

        if (WIDGET_SYNC == intent?.action) {
            if (!::preference.isInitialized){
                preference = MyPreference(context)

            }


            val ids = preference.getWidgetIds()
            for (id in ids)
                updateAppWidget(context, AppWidgetManager.getInstance(context), id.toInt())
        }
        super.onReceive(context, intent)
    }


    companion object {
        var weatherC = ""
        var weatherIconUrl = ""
        var batteryLevel = 0
        var batteryTemp  = ""

        var sonWeatherC = ""
        var sonIcon = ""
        var face3Sayac = 0





        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {


            println("--------------UPDATE APP WıDGET IS CALL---------------------")
            val intent2 = Intent(context, WidgetActivity::class.java)
            intent2.action = WIDGET_SYNC
            val pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, 0)
            val views2 = RemoteViews(context.packageName, R.layout.widget_activity)





            if ( FUUL_URL != ""){
            //    Toast.makeText(context, "SUAN FULL URL VARRRRRRRRRRRRRR", Toast.LENGTH_SHORT).show()
                Shared(context).setData()
              //  HTTPReqTask(FUUL_URL).execute()
                getData(context,FUUL_URL)
                Shared(context).setData2(0)
                Shared(context).setData3()


            }

            if (FUUL_URL == ""){
                var myUrl = Shared(context).getData()
              //  HTTPReqTask(myUrl).execute()
                getData(context,myUrl)
              //  Toast.makeText(context, "SUAN FUUL URL YOOOOOOOKKKKKKK", Toast.LENGTH_SHORT).show()

                var Num = Shared(context).getData2()

                println(Num)
                if (Num>=1){
                    views2.setTextViewText(R.id.appwidget_text, "$sonWeatherC°C")
                    Picasso.get().load("https:$sonIcon").into(object : com.squareup.picasso.Target {
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            views2.setImageViewBitmap(R.id.weather_icon,bitmap)
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                    })
                    Num = Num + 1
                    Shared(context).setData2(Num)
                    println(Num)
                }
                Num = Num + 1
                Shared(context).setData2(Num)




            }



           // getData(context)
            getBatter(context)
            batteryTemperature(context)


            /////////theme 2 settings //////////////////////////////////////////////////////////////
            if (Watch3Chechk == "Remote Face"){
                views2.setViewVisibility(R.id.Face2_Main,View.VISIBLE)
                val calendar = Calendar.getInstance()
                val day = calendar[Calendar.DAY_OF_WEEK]



                when(day){
                    Calendar.MONDAY -> views2.setViewVisibility(R.id.face2_day1,View.VISIBLE)
                    Calendar.TUESDAY -> views2.setViewVisibility(R.id.face2_day2,View.VISIBLE)
                    Calendar.WEDNESDAY -> views2.setViewVisibility(R.id.face2_day3,View.VISIBLE)
                    Calendar.THURSDAY -> views2.setViewVisibility(R.id.face2_day4,View.VISIBLE)
                    Calendar.FRIDAY -> views2.setViewVisibility(R.id.face2_day5,View.VISIBLE)
                    Calendar.SATURDAY -> views2.setViewVisibility(R.id.face2_day6,View.VISIBLE)
                    Calendar.SUNDAY -> views2.setViewVisibility(R.id.face2_day7,View.VISIBLE)
                }


                views2.setTextViewText(R.id.face2_battery_level,batteryLevel.toString())
                views2.setProgressBar(R.id.circularProgressbar,100,batteryLevel,false)
            }
            ////////////////////////////////////////////////////////////////////////////////////////

            ////////////////////theme 1 settings //////////////////////////////////////////////////////////////
            if (Watch3Chechk == "Community Face"){


                //// Battery Icon
                if(batteryLevel == 100){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.VISIBLE)
                }else if (batteryLevel >= 94 && batteryLevel != 100){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 80 && batteryLevel < 94) {
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 68 && batteryLevel < 80){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 42 && batteryLevel < 68){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 28 && batteryLevel<42){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }else if (batteryLevel >= 14 && batteryLevel<28){
                    views2.setViewVisibility(R.id.battery_cell_1,View.VISIBLE)
                    views2.setViewVisibility(R.id.battery_cell_2,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_3,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_4,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_5,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_6,View.GONE)
                    views2.setViewVisibility(R.id.battery_cell_7,View.GONE)
                }

                /// Battery text
                views2.setTextViewText(R.id.battery_text, "%"+batteryLevel.toString())

                ///Battery Temp Text
                views2.setTextViewText(R.id.battery_Temp, "$batteryTemp°C")


            }
            //////////////////////////////////////////////////////////////////////////////////////////////////


            ////////////////////Theme 3 settings //////////////////////////////////////////////////////////////


            ///////////////////////////////////////////////////////////////////////////////////////////////////


            views2.setOnClickPendingIntent(R.id.iv_sync, pendingIntent2)
            appWidgetManager.updateAppWidget(appWidgetId, views2)


        }


        fun getData(context: Context,URL:String){

            if(URL != ""){
                var client = OkHttpClient()
                /////https://api.weatherapi.com/v1/current.json?key=e0f61b6abc674612a25202606210110&q=London&aqi=yes
                var BASE_URL = " ${URL.toString()}"
                //println("WİDGET ACTİVİTY URL ====   ${FUUL_URL.toString()}")
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
                            // WidgetActivity.weatherC = data1_get.toString()
                            // WidgetActivity.weatherIconUrl = data2_get

                            sonWeatherC = data1_get.toString()
                            sonIcon = data2_get




                            /*
                             /// Weather Icon
                            Picasso.get().load("https:$icon").into(object : com.squareup.picasso.Target {
                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                    views.setImageViewBitmap(R.id.weather_icon,bitmap)
                                }

                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                            })

                             */





                        }
                    }

                })

            }



        }

        fun getBatter(context: Context){
            //// battery data///
            val iFilter: IntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus: Intent? = context.registerReceiver(null, iFilter)

            val level: Int = if (batteryStatus != null) batteryStatus.getIntExtra(
                BatteryManager.EXTRA_LEVEL,
                -1
            ) else -1
            val scale: Int = if (batteryStatus != null) batteryStatus.getIntExtra(
                BatteryManager.EXTRA_SCALE,
                -1
            ) else -1

            val batteryPct: Float = level / scale.toFloat()
            var batlevel = (batteryPct * 100)
            ////
            batteryLevel = batlevel.toInt()

        }

        fun batteryTemperature(context: Context){
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val temp = intent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toFloat() / 10
            batteryTemp = temp.toString()
            println("battery temp is "+batteryTemp)
        }



    }




}

