package com.sout.custom_apple_watch.WidgetActivity

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.RemoteViews
import com.squareup.picasso.Picasso
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.Recycler.WatchAdapter
import com.sout.custom_apple_watch.Recycler.WatchAdapter.Companion.Watch3Chechk
import com.sout.custom_apple_watch.View.MainActivity
import com.sout.custom_apple_watch.View.MapsActivity
import com.sout.custom_apple_watch.View.WatchColorSettings
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.COLOR_BATTERY_TEXT_C
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.COLOR_TIME_C
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.COLOR_TIME_MINUTES_C
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active1
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active2
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active3
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active4
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active5
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active6
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active7
import com.sout.custom_apple_watch.View.WatchColorSettings.Companion.active8
import com.sout.custom_apple_watch.View.WatchFaceSetting
import com.sout.custom_apple_watch.WidgetActivity.WidgetActivity.Companion.sonIcon
import com.sout.custom_apple_watch.WidgetActivity.WidgetActivity.Companion.sonWeatherC
import kotlinx.android.synthetic.main.activity_widget_config.*
import kotlinx.coroutines.*
import java.io.*
import java.lang.Runnable
import java.util.*






class WidgetConfigActivity : AppCompatActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID


    companion object{
        var weatherKordinat = ""
        var watchFace = ""
        const val SHARED_PREFS = "prefs"
        const val KEY_BUTTON_TEXT = "keyButtonText"
    }


    private val filepath = "WeatherFile"
    private val filepath2 = "Color"
    private val filepath3 = "WatchFace"

    internal var myExternalFile: File?=null

    private val isExternalStorageReadOnly: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)
    }
    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(extStorageState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        println("bbbbbbb"+ WatchAdapter.Watch3Chechk)

        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)


        val configIntent = intent
        val extras = configIntent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }







        }


        if (fileExists(yourFile,"weather1")){
            //// file islemi başlangıç
            myExternalFile = File(getExternalFilesDir(filepath), "weather1")
            val filename = "weather1"
            myExternalFile = File(getExternalFilesDir(filepath),filename)
            if(filename.toString()!= null && filename.toString().trim()!=""){
                var fileInputStream = FileInputStream(myExternalFile)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
                fileInputStream.close()
                //Displaying data on EditText
          //      Toast.makeText(applicationContext,stringBuilder.toString(), Toast.LENGTH_SHORT).show()
                weatherKordinat = stringBuilder.toString()

            }
            ///////////file islemi bitis

                location_text.text = "Location:\n$weatherKordinat"
                check1.setBackgroundResource(R.drawable.check2)

        }else{
            location_text.text = "Location Error Please\nClick The Set Location"
        }












        /// FACE İCİN
        //// file islemi başlangıç


        val yourFile2 = File("storage/emulated/0/Android/data/com.sout.custom_apple_watch/files/WatchFace")


        if (fileExists(yourFile2,"face")){
            myExternalFile = File(getExternalFilesDir(filepath3), "face")

            val filename3 = "face"

            myExternalFile = File(getExternalFilesDir(filepath3),filename3)

            if(filename3.toString()!=null && filename3.toString().trim()!=""){
                var fileInputStream = FileInputStream(myExternalFile)
                var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }
                fileInputStream.close()
                //Displaying data on EditText
             //   Toast.makeText(applicationContext,stringBuilder.toString(), Toast.LENGTH_SHORT).show()
                watchFace = stringBuilder.toString()
            }
            ///////////file islemi bitis

            if (Watch3Chechk == "Community Face"){
                watch_png.setImageResource(R.drawable.facee1)
                check2.setBackgroundResource(R.drawable.check2)
            }
            if (Watch3Chechk == "Remote Face"){
                watch_png.setImageResource(R.drawable.face2)
                check2.setBackgroundResource(R.drawable.check2)
            }

            if (WatchAdapter.Watch3Chechk == "Counter Face"){
                watch_png.setImageResource(R.drawable.face3)
                check2.setBackgroundResource(R.drawable.check2)
            }
            if (WatchAdapter.Watch3Chechk==""){
                face_back.visibility = View.GONE
                check2.setImageResource(R.drawable.ic_baseline_block_24)
                face_Error.text = "Face Error Please\n" +
                "Click The Set Face"
            }

        }else{
            face_back.visibility = View.GONE
            face_Error.text = "Face Error Please\n" +
                    "Click The Set Face"
        }





        if (active1 || active2 || active3 || active4 || active5 || active6 || active7 || active8){
            check3.setBackgroundResource(R.drawable.check2)
            watchColor.text = "Succesful"
        }else{
            watchColor.text = "Color Error Please\n" +
                    "Click The Set Color"
        }


        cardView.setOnClickListener {
            val intent = Intent ( this ,MapsActivity::class.java)
            startActivity(intent)
            finish()
        }

        card2.setOnClickListener {
            val intent = Intent ( this ,WatchFaceSetting::class.java)
            startActivity(intent)
            finish()
        }


        cardView3.setOnClickListener {
            val intent = Intent ( this ,WatchColorSettings::class.java)
            startActivity(intent)
            finish()
        }


    }




    fun config_Set_Button(view: android.view.View) {

        start = true


        progress.visibility = View.VISIBLE


  /*
    //// file islemi başlangıç
        myExternalFile = File(getExternalFilesDir(filepath), "weather1")

        val filename = "weather1"

        myExternalFile = File(getExternalFilesDir(filepath),filename)
        if(filename.toString()!=null && filename.toString().trim()!=""){
            var fileInputStream = FileInputStream(myExternalFile)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            fileInputStream.close()
            //Displaying data on EditText
            Toast.makeText(applicationContext,stringBuilder.toString(), Toast.LENGTH_SHORT).show()
            weatherKordinat = stringBuilder.toString()
        }
        ///////////file islemi bitis
   */


        //// file islemi başlangıç

        /*
          myExternalFile = File(getExternalFilesDir(filepath2), "color1")

        val filename2 = "color1"

        myExternalFile = File(getExternalFilesDir(filepath2),filename2)
        if(filename2.toString()!=null && filename2.toString().trim()!=""){
            var fileInputStream = FileInputStream(myExternalFile)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            fileInputStream.close()
            //Displaying data on EditText
            Toast.makeText(applicationContext,stringBuilder.toString(), Toast.LENGTH_SHORT).show()
            watchColor = stringBuilder.toString()
        }
         */


        ///////////file islemi bitis



        //// file islemi başlangıç
        myExternalFile = File(getExternalFilesDir(filepath3), "face")

        val filename3 = "face"

        myExternalFile = File(getExternalFilesDir(filepath3),filename3)
        if(filename3.toString()!=null && filename3.toString().trim()!=""){
            var fileInputStream = FileInputStream(myExternalFile)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            fileInputStream.close()
            //Displaying data on EditText
           // Toast.makeText(applicationContext,stringBuilder.toString(), Toast.LENGTH_SHORT).show()
            watchFace = stringBuilder.toString()
        }
        ///////////file islemi bitis




        val appWidgetManager = AppWidgetManager.getInstance(this)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val buttonText = ""
        val views = RemoteViews(this.packageName, R.layout.widget_activity)

        views.setTextViewText(R.id.appwidget_text, "${sonWeatherC}°C")
        Picasso.get().load("https:${sonIcon}").into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                views.setImageViewBitmap(R.id.weather_icon,bitmap)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })


        ///////////face1 color///////////////
        if (watchFace == "Community Face"){
            if (WatchColorSettings.active1){
                views.setTextColor(R.id.COLOR_TIME_C, COLOR_TIME_C)

            }
            if (WatchColorSettings.active2){
                views.setTextColor(R.id.COLOR_TIME_MINUTES_C,
                    COLOR_TIME_MINUTES_C
                )

            }
            if (WatchColorSettings.active3){
                views.setTextColor(R.id.COLOR_MOON_C, WatchColorSettings.COLOR_MOON_C)
            }
            if (WatchColorSettings.active4){
                views.setTextColor(R.id.COLOR_WEEK_C, WatchColorSettings.COLOR_WEEK_C)
            }
            if (WatchColorSettings.active5){
                views.setTextColor(R.id.COLOR_DAY_C, WatchColorSettings.COLOR_DAY_C)

            }
            if (WatchColorSettings.active6){
                views.setTextColor(R.id.battery_text, WatchColorSettings.COLOR_BATTERY_TEXT_C)
            }
            if (WatchColorSettings.active7){
                views.setTextColor(R.id.battery_Temp,
                    WatchColorSettings.COLOR_BATTERY_TEMP_TEXT_C
                )
            }
            if (WatchColorSettings.active8){
                views.setTextColor(R.id.appwidget_text, WatchColorSettings.COLOR_WEATHER_C)
            }
        }


        if (watchFace == "Remote Face"){
            if (WatchColorSettings.active1){
                views.setTextColor(R.id.face2_clock_hour, COLOR_TIME_C)

            }
            if (WatchColorSettings.active2){
                views.setTextColor(R.id.face2_battery_level,
                    COLOR_BATTERY_TEXT_C
                )

            }
            if (WatchColorSettings.active3){
                views.setTextColor(R.id.face2_clock_minutes, WatchColorSettings.COLOR_TIME_MINUTES_C)
            }
            if (WatchColorSettings.active4){
                views.setTextColor(R.id.face2_calendar_moon, WatchColorSettings.COLOR_MOON_C)
            }
            if (WatchColorSettings.active5){
                views.setTextColor(R.id.face2_calendar_day, WatchColorSettings.COLOR_DAY_C)

            }

        }


        ////////////////////////////////////






            runBlocking {
                launch {
                    delay(500)

                    println("2.....")
                    /*
                     if (watchFace == "Community Face"){
                        HTTPReqTask(FUUL_URL)
                        //// Weather text
                    }

                     */

                    //  getData(this@WidgetConfigActivity)
                    WidgetActivity.getBatter(this@WidgetConfigActivity)
                    WidgetActivity.batteryTemperature(this@WidgetConfigActivity)
                    //    WidgetActivity.getData(this@WidgetConfigActivity)


                }
            }


            runBlocking {
                launch {
                    delay(500)

                    //////////////////////////////////////////FACE 1 SETTİNGS////////////////////////////////////////////////////////
                    if (Watch3Chechk == "Community Face"){

                        views.setViewVisibility(R.id.Face1_Main,View.VISIBLE)

                        //////////////////////////////////////////FACE 1 SETTİNGS/////////////////////////////////////////////////////////////////////////////////
                        //// Battery Icon
                        if(WidgetActivity.batteryLevel == 100){
                            views.setViewVisibility(R.id.battery_cell_1, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_2, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_3, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_4, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_5, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_6, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_7, View.VISIBLE)
                        }else if (WidgetActivity.batteryLevel >= 94 && WidgetActivity.batteryLevel != 100){
                            views.setViewVisibility(R.id.battery_cell_1, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_2, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_3, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_4, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_5, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_6, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_7, View.GONE)
                        }else if (WidgetActivity.batteryLevel >= 80 && WidgetActivity.batteryLevel < 94) {
                            views.setViewVisibility(R.id.battery_cell_1, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_2, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_3, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_4, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_5, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_6, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_7, View.GONE)
                        }else if (WidgetActivity.batteryLevel >= 68 && WidgetActivity.batteryLevel < 80){
                            views.setViewVisibility(R.id.battery_cell_1, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_2, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_3, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_4, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_5, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_6, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_7, View.GONE)
                        }else if (WidgetActivity.batteryLevel >= 42 && WidgetActivity.batteryLevel < 68){
                            views.setViewVisibility(R.id.battery_cell_1, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_2, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_3, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_4, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_5, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_6, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_7, View.GONE)
                        }else if (WidgetActivity.batteryLevel >= 28 && WidgetActivity.batteryLevel <42){
                            views.setViewVisibility(R.id.battery_cell_1, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_2, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_3, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_4, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_5, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_6, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_7, View.GONE)
                        }else if (WidgetActivity.batteryLevel >= 14 && WidgetActivity.batteryLevel <28){
                            views.setViewVisibility(R.id.battery_cell_1, View.VISIBLE)
                            views.setViewVisibility(R.id.battery_cell_2, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_3, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_4, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_5, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_6, View.GONE)
                            views.setViewVisibility(R.id.battery_cell_7, View.GONE)
                        }

                        /// Battery text
                        views.setTextViewText(R.id.battery_text, "%"+ WidgetActivity.batteryLevel.toString())

                        ///Battery Temp Text
                        views.setTextViewText(R.id.battery_Temp, "${WidgetActivity.batteryTemp}°C")

                        

                        ////////////////////////////////////////FACE 1 SETTİNGS BİTİS/////////////////////////////////////////////////////////////////////////////////
                    }
                    //////////////////////////////////////////FACE 1 SETTİNGS////////////////////////////////////////////////////////

                    //////////////////////////////////////////FACE 2 SETTİNGS/////////////////////////////////////////////////////////////////////////////////
                    if (Watch3Chechk == "Remote Face"){
                        views.setViewVisibility(R.id.Face2_Main,View.VISIBLE)
                        val calendar = Calendar.getInstance()
                        val day = calendar[Calendar.DAY_OF_WEEK]

                        when(day){
                            Calendar.MONDAY -> views.setViewVisibility(R.id.face2_day1,View.VISIBLE)
                            Calendar.TUESDAY -> views.setViewVisibility(R.id.face2_day2,View.VISIBLE)
                            Calendar.WEDNESDAY -> views.setViewVisibility(R.id.face2_day3,View.VISIBLE)
                            Calendar.THURSDAY -> views.setViewVisibility(R.id.face2_day4,View.VISIBLE)
                            Calendar.FRIDAY -> views.setViewVisibility(R.id.face2_day5,View.VISIBLE)
                            Calendar.SATURDAY -> views.setViewVisibility(R.id.face2_day6,View.VISIBLE)
                            Calendar.SUNDAY -> views.setViewVisibility(R.id.face2_day7,View.VISIBLE)
                        }

                        views.setTextViewText(R.id.face2_battery_level, WidgetActivity.batteryLevel.toString())
                        views.setProgressBar(R.id.circularProgressbar,100, WidgetActivity.batteryLevel,false)
                    }
                    ///////////////////////////////////////////////FACE 2 SETTİNGS BİTİS /////////////////////////////////////////////////////////////////////

                    //////////////////////////////////////////FACE 3 SETTİNGS////////////////////////////////////////////////////////////////////////////

                    if (Watch3Chechk == "Counter Face"){
                        views.setViewVisibility(R.id.Face3_Main,View.VISIBLE)
                    }
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    if (li == "true"){
                        views.setViewVisibility(R.id.lisanas,View.GONE)
                    }



                }
            }



        views.setOnClickPendingIntent(R.id.iv_sync, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
        val prefs = getSharedPreferences(WidgetConfigActivity.SHARED_PREFS, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(WidgetConfigActivity.KEY_BUTTON_TEXT + appWidgetId, buttonText)
        editor.apply()
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()


    }




    fun fileExists(directory: File?, fileName: String?): Boolean {
        val file = File(directory, fileName)
        return file.exists()
    }




}