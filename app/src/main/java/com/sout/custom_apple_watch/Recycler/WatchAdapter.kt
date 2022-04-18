package com.sout.custom_apple_watch.Recycler

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.sout.custom_apple_watch.Model.WatchModel
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.View.MainActivity


import kotlinx.android.synthetic.main.item.view.*
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList

class WatchAdapter (private val models: ArrayList<WatchModel>, private val context: Context) : PagerAdapter()  {




    private val filepath = "WatchFace"
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
        var Watch3Chechk = ""
    }

    //    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return models.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        layoutInflater = LayoutInflater.from(context)
//        val view = layoutInflater.inflate(R.layout.item, container, false)
        val mView = LayoutInflater.from(context).inflate(R.layout.item,container,false)
        val imageView: ImageView
        val title: TextView
        val desc: TextView
        var price: TextView
        imageView = mView.findViewById(R.id.image)
        title = mView.findViewById(R.id.title)
        desc = mView.findViewById(R.id.txt_des)
        price =mView.findViewById(R.id.price)

        /*
         imageView.setOnClickListener{
            Toast.makeText(context, "${models[position].title}", Toast.LENGTH_SHORT).show()
            ////ONEMLİ YER BURDA !!!!!
        }
         */


        mView.btn_start.setOnClickListener {
           // Toast.makeText(context, "suan bastın", Toast.LENGTH_SHORT).show()
            var selectWatchFace = models[position].title

            Watch3Chechk = selectWatchFace

            myExternalFile = File(context.getExternalFilesDir(filepath), "face")
                val fileOutPutStream = FileOutputStream(myExternalFile)
                fileOutPutStream.write("$selectWatchFace".toByteArray())
                fileOutPutStream.close()


             var intent = Intent(context, MainActivity::class.java)
             context.startActivity(intent)

        }


        imageView.setImageResource(models[position].image)
        title.text = models[position].title
        desc.text = models[position].desc
        price.text = models[position].price.toString()
        container.addView(mView, 0)
        return mView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}