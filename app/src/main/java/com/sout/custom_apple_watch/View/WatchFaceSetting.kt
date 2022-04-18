package com.sout.custom_apple_watch.View

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.*
import com.sout.custom_apple_watch.Model.WatchModel
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.Recycler.WatchAdapter
import kotlinx.android.synthetic.main.item.*

class WatchFaceSetting : AppCompatActivity() {


    private lateinit var adapter: WatchAdapter
    private lateinit var models: ArrayList<WatchModel>

    private lateinit var viewPager: ViewPager
    var sliderDotspanel: LinearLayout? = null
    private var dotscount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_face_setting)



        /// style
        window.statusBarColor = ContextCompat.getColor(this, R.color.mycolor1)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
        val colorDrawable = ColorDrawable(Color.parseColor("#29397C"))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
        setTitle("Watch Settings");
        centerTITLE()
        ////

        viewPager = findViewById(R.id.view_pager)
        sliderDotspanel = findViewById(R.id.slider_dots)

        models = ArrayList()
        models.add(WatchModel(R.drawable.facee1,"Community Face", "Weather,Battery,Clock,Battery Temp","5$",0))
        models.add(WatchModel(R.drawable.face2,"Remote Face", "Battery,Clock,Calendar","3.59$",1))
        models.add(WatchModel(R.drawable.face3,"Counter Face", "Step Counter,Battery,Kcal","4$",2))
        models.add(WatchModel(R.drawable.main3,"Comming Soon", "","0$",3))
        models.add(WatchModel(R.drawable.main3,"Comming Soon", "","0$",4))

        adapter = WatchAdapter(models, this@WatchFaceSetting)
        viewPager.adapter = adapter



        viewPager.setPadding(130, 130, 130, 130)
        dotscount = adapter.count

        val dots = arrayOfNulls<ImageView>(dotscount)

        for (i in 0 until dotscount) {
            dots[i] = ImageView(this)
            dots[i]!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(20, 20, 20, 20)
            sliderDotspanel!!.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot))



        viewPager.setOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int){}

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotscount) {
                    dots[i]?.setImageDrawable(ContextCompat.getDrawable(this@WatchFaceSetting,R.drawable.non_active_dot))
                }
                dots[position]?.setImageDrawable(ContextCompat.getDrawable(this@WatchFaceSetting,R.drawable.active_dot))
            }
        })





}


    fun centerTITLE(){
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

    fun bas1x(view: View){
       // Toast.makeText(this, "Basıldı", Toast.LENGTH_SHORT).show()
    }

}


