package com.sout.custom_apple_watch.WidgetActivity

import android.content.Context

class SharedCounter(context: Context) {


    val preference = context.getSharedPreferences("xmod2", Context.MODE_PRIVATE)

    fun setData(counterNumber:Int){
        preference.edit().putInt("data2", counterNumber).commit().toString().toInt()
    }

    fun getData() :Int{
        return preference.getString("data1",null).toString().toInt()
    }


}