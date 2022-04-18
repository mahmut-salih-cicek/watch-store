package com.sout.custom_apple_watch.Util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sout.custom_apple_watch.R

fun ImageView.gorselyap(url:String){
        val options = RequestOptions()
                .placeholder(R.color.white)
                .error(R.color.white)

        Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}