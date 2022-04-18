package com.sout.custom_apple_watch.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.View.MapsActivity
import com.sout.custom_apple_watch.View.WatchColorSettings
import com.sout.custom_apple_watch.View.WatchFaceSetting

class MainFragment : Fragment() {



    @SuppressLint("FragmentBackPressedCallback")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var rootView = inflater.inflate(R.layout.fragment_main, container, false)

        var weather_lociton_button = rootView.findViewById<Button>(R.id.weather_loction_button)
        var watch_face = rootView.findViewById<Button>(R.id.watch_face_button)
        var buy_face = rootView.findViewById<Button>(R.id.buy_face_button)
        var color_settings = rootView.findViewById<Button>(R.id.color_settings_button)
        var recyclerId = rootView.findViewById<RecyclerView>(R.id.recycler5)

        weather_lociton_button.setOnClickListener {
            val intent = Intent(activity, MapsActivity::class.java)
            startActivity(intent)
        }

        watch_face.setOnClickListener {
            val intent = Intent(activity,WatchFaceSetting::class.java)
            startActivity(intent)
        }

        buy_face.setOnClickListener {
           /// BURDA GONE YAPICAZ VE SATIN ALMA YERİNİ ONE CIKARCAZ
            var id = requireActivity().findViewById<FrameLayout>(R.id.fragmentContainerView)
            var id2 = requireActivity().findViewById<RecyclerView>(R.id.recyclerView)
            id.visibility = View.GONE
            id2.visibility = View.GONE
        }

        color_settings.setOnClickListener {
            val intent = Intent(activity,WatchColorSettings::class.java)
            startActivity(intent)
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                var id = requireActivity().findViewById<FrameLayout>(R.id.fragmentContainerView)
                var id2 = requireActivity().findViewById<RecyclerView>(R.id.recyclerView)
                id.visibility = View.VISIBLE
                id2.visibility = View.VISIBLE
            }})

        return rootView


    }





}