package com.sout.custom_apple_watch.WidgetActivity

import android.content.Context
import com.sout.custom_apple_watch.View.MapsActivity.Companion.FUUL_URL
import com.sout.custom_apple_watch.WidgetActivity.WidgetConfigActivity.Companion.watchFace

class Shared(context: Context) {

    val preference = context.getSharedPreferences("xmod", Context.MODE_PRIVATE)

    fun setData(){
      preference.edit().putString("data1",FUUL_URL).commit().toString()
    }

    fun getData() :String{
        return preference.getString("data1","").toString()
    }


    fun setData2(num:Int){
        preference.edit().putInt("data2",num).commit()
    }

    fun getData2() : Int{
        return preference.getInt("data2",0).toInt()
    }


    fun setData3(){
        preference.edit().putString("data3", watchFace).commit().toString()
    }

    fun getData3() :String{
        return preference.getString("data3",null).toString()
    }






    fun setface1Data(myBool:Boolean){
        preference.edit().putBoolean("face1Data", myBool).commit()
    }

    fun getFace1Data():Boolean{
        return preference.getBoolean("face1Data",false)
    }

    fun setface2Data(myBool:Boolean){
        preference.edit().putBoolean("face2Data", myBool).commit()
    }

    fun getFace2Data():Boolean{
        return preference.getBoolean("face2Data",false)
    }

    fun setface3Data(myBool:Boolean){
        preference.edit().putBoolean("face3Data", myBool).commit()
    }

    fun getFace3Data():Boolean{
        return preference.getBoolean("face3Data",false)
    }




}





















