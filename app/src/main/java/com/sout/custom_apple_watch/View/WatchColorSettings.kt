package com.sout.custom_apple_watch.View

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.sout.custom_apple_watch.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_watch_color_settings.*
import top.defaults.colorpicker.ColorPickerPopup
import java.io.*


class WatchColorSettings : AppCompatActivity() {

    private var isTouch = false

    private val filepath = "Color"
    private val filepath3 = "WatchFace"

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

    private var selectedWatchTitle = ""



    companion object{
        var color55 = 0
        var basılan = 0

        var COLOR_TIME_C = 0
        var COLOR_TIME_MINUTES_C = 0
        var COLOR_MOON_C = 0
        var COLOR_DAY_C = 0
        var COLOR_BATTERY_TEXT_C = 0
        var COLOR_BATTERY_TEMP_TEXT_C = 0
        var COLOR_WEATHER_C = 0
        var COLOR_WEEK_C = 0

        var active1 = false
        var active2 = false
        var active3 = false
        var active4 = false
        var active5 = false
        var active6 = false
        var active7 = false
        var active8 = false

        var Id1 = false
        var Id2 = false
        var Id3 = false
        var Id4 = false
        var Id5 = false
        var Id6 = false
        var Id7 = false
        var Id8 = false


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_color_settings)

        /// style

        window.statusBarColor = ContextCompat.getColor(this, R.color.mycolor1)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.color4));
        }

        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)


        val colorDrawable = ColorDrawable(Color.parseColor("#29397C"))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
        supportActionBar!!.hide()
        setTitle("Watch Color Settings")
        centerTitle()
        ////


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
          //  Toast.makeText(applicationContext,stringBuilder.toString(), Toast.LENGTH_SHORT).show()
            selectedWatchTitle = stringBuilder.toString()
        }
        ///////////file islemi bitis



        if (selectedWatchTitle == "Community Face"){
            face1_color_activity.visibility = View.VISIBLE
        }
        if (selectedWatchTitle == "Remote Face"){
            face2_color_activity.visibility = View.VISIBLE
        }


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













    fun button_Set_Color(view: android.view.View) {

      //  Toast.makeText(this, "Basıldı", Toast.LENGTH_SHORT).show()

        if (selectedWatchTitle == "Community Face"){

            if (Id1 == true){
                active1 = true
                println("1 $active1")
            }

            if (Id2 == true){

                active2 = true
                println("3 $active2")
            }

            if (Id3 == true){

                active3 = true
                println("3 $active3")
            }

            if (Id4 == true){
                active4 = true
            }

            if (Id5 == true){

                active5 = true
            }

            if (Id6 == true){

                active6 = true
            }

            if (Id7 == true){

                active7 = true
            }

            if (Id8 == true){

                active8 = true
            }
        }

        if (selectedWatchTitle == "Remote Face"){

            if (Id1 == true){
                active1 = true
                println("1 $active1")
            }

            if (Id2 == true){

                active2 = true
                println("3 $active2")
            }

            if (Id3 == true){

                active3 = true
                println("3 $active3")
            }

            if (Id4 == true){
                active4 = true
            }

            if (Id5 == true){

                active5 = true
            }

        }


        var i = Intent(this,MainActivity::class.java)
        startActivity(i)



    }


    //// face 1
    fun COLOR_TIME(view: android.view.View){
        Id1 = true



        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_TIME.setTextColor(color)
                    COLOR_TIME_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })


        Snackbar.make(getWindow().getDecorView().getRootView(), "Clock Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Clock"


    }
    fun COLOR_TIME_MINUTES(view: android.view.View) {

         Id2 = true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_TIME_MINUTES.setTextColor(color)
                    COLOR_TIME_MINUTES_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })




        Snackbar.make(getWindow().getDecorView().getRootView(), "Clock MINUTES Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Clock MINUTES"
    }
    fun COLOR_MOON(view: android.view.View) {
        Id3 =true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_MOON.setTextColor(color)
                    COLOR_MOON_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })




        Snackbar.make(getWindow().getDecorView().getRootView(), "Moon Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Moon"
    }
    fun COLOR_DAY(view: android.view.View) {

        Id4 = true


        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_DAY.setTextColor(color)
                    COLOR_DAY_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })


        Snackbar.make(getWindow().getDecorView().getRootView(), "Day Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Day"
    }
    fun COLOR_BATTERY_TEXT(view: android.view.View) {

        Id5 = true




        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_BATTERY_TEXT.setTextColor(color)
                    COLOR_BATTERY_TEXT_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })



        Snackbar.make(getWindow().getDecorView().getRootView(), "Battery LevelSelected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Battery Level"
    }
    fun COLOR_BATTERY_TEMP_TEXT(view: android.view.View) {
        Id6 = true



        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_BATTERY_TEMP_TEXT.setTextColor(color)
                    COLOR_BATTERY_TEMP_TEXT_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })



        Snackbar.make(getWindow().getDecorView().getRootView(), "Battery Temp Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Battery Temp"
    }
    fun COLOR_WEATHER(view: android.view.View) {

        Id7 = true


        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_WEATHER.setTextColor(color)
                    COLOR_WEATHER_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })



        Snackbar.make(getWindow().getDecorView().getRootView(), "Weather Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Weather"
    }
    fun COLOR_WEEK(view: android.view.View) {

        Id8 = true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    COLOR_WEEK.setTextColor(color)
                    COLOR_WEEK_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })





        Snackbar.make(getWindow().getDecorView().getRootView(), "Week Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Week"
    }




    /// face2
    fun face2_clock_hour(view: android.view.View) {

        Id1 = true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    face2_clock_hour.setTextColor(color)
                    COLOR_TIME_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })


        Snackbar.make(getWindow().getDecorView().getRootView(), "Clock Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Clock"

    }
    fun face2_battery_level(view: android.view.View) {
        Id2 = true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    face2_battery_level.setTextColor(color)
                    COLOR_BATTERY_TEXT_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })


        Snackbar.make(getWindow().getDecorView().getRootView(), "Clock Selected", Snackbar.LENGTH_LONG).show();
        selected_text.text = "Selected Item : Clock"
    }
    fun face2_clock_minutes(view: android.view.View) {
        Id3 = true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    face2_clock_minutes.setTextColor(color)
                    COLOR_TIME_MINUTES_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })
    }
    fun face2_calendar_moon(view: android.view.View) {
        Id4 = true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    face2_calendar_moon.setTextColor(color)
                    COLOR_MOON_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })
    }
    fun face2_calendar_day(view: android.view.View) {
        Id5 = true

        ColorPickerPopup.Builder(this)
            .initialColor(Color.RED) // Set initial color
            .enableBrightness(true) // Enable brightness slider or not
            .enableAlpha(true) // Enable alpha slider or not
            .okTitle("Choose")
            .cancelTitle("Cancel")
            .showIndicator(true)
            .showValue(true)
            .build()
            .show(object : ColorPickerPopup.ColorPickerObserver {
                override fun onColorPicked(color: Int) {
                    face2_calendar_moon.setTextColor(color)
                    COLOR_DAY_C = color
                }

                override fun onColor(color: Int, fromUser: Boolean) {

                }

            })

    }








/*
 override fun onTouchEvent(event: MotionEvent): Boolean {
        val X = event.x.toInt()
        val Y = event.y.toInt()
        val eventaction = event.action
        when (eventaction) {
            MotionEvent.ACTION_DOWN -> {
                Toast.makeText(this, "ACTION_DOWN AT COORDS X: $X Y: $Y", Toast.LENGTH_SHORT).show()
                isTouch = true
            }
            MotionEvent.ACTION_MOVE -> Toast.makeText(this, "MOVE X: $X Y: $Y", Toast.LENGTH_SHORT)
                .show()
            MotionEvent.ACTION_UP -> Toast.makeText(
                this,
                "ACTION_UP X: $X Y: $Y",
                Toast.LENGTH_SHORT
            ).show()
        }
        return true
    }
 */




}