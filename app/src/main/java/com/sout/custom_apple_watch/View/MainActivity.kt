package com.sout.custom_apple_watch.View


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.SupportMapFragment
import com.sout.custom_apple_watch.API.API
import com.sout.custom_apple_watch.Model.PaymentModel
import com.sout.custom_apple_watch.Model.StoreModel
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.Recycler.PaymentRecycler
import com.sout.custom_apple_watch.Recycler.StoreRecycler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity(),PurchasesUpdatedListener,PaymentRecycler.Listener,StoreRecycler.Listener {


    private var BASE_URL = "https://raw.githubusercontent.com/"
    lateinit var compositeDisposable: CompositeDisposable

    val REQUEST_CODE = 101
    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199


    private lateinit var models: ArrayList<PaymentModel>

    var arrayAdapter: ArrayAdapter<String>? = null
    var listView: ListView? = null
    private var billingClient: BillingClient? = null






    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /// style
        window.statusBarColor = ContextCompat.getColor(this, R.color.mycolor1)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.color4)
        }
        val colorDrawable = ColorDrawable(Color.parseColor("#29397C"))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
        title = "Apple Watch Widget Store"
        centerTitle()
        ////


        sharedPreferences = this.getSharedPreferences("howTo", Context.MODE_PRIVATE)
        val cdd = CustomDialogClass(this@MainActivity)
        cdd.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var startData = sharedPreferences.getBoolean("howTo",true)
        if (startData == false){

        }else{
            cdd.show()
        }


                 ///// file işlmeleri
        // create path
        val path = this.getExternalFilesDir(null)
        ///create folder
        var letDirectorty = File(path,"752")
        letDirectorty.mkdir()




        enableLoc()
        fetchLocation()



        listView = findViewById<View>(R.id.listview3) as ListView

        // Establish connection to billing client
        //check purchase status from google play store cache on every app start
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases().setListener(this).build()

        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryPurchase = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
                    val queryPurchases = queryPurchase.purchasesList
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        handlePurchases(queryPurchases)
                    }

                    val purchaseFound = java.util.ArrayList<Int>()
                    if (queryPurchases != null && queryPurchases.size > 0)
                    {
                        //check item in purchase list
                        for (p in queryPurchases) {
                            val index = purchaseItemIDs.indexOf(p.sku)
                            //if purchase found
                            if (index > -1) {
                                purchaseFound.add(index)
                                if (p.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                    savePurchaseItemValueToPref(purchaseItemIDs[index], true)
                                } else {
                                    savePurchaseItemValueToPref(purchaseItemIDs[index], false)
                                }
                            }
                        }
                        //items that are not found in purchase list mark false
                        //indexOf returns -1 when item is not in foundlist
                        for (i in purchaseItemIDs.indices) {
                            if (purchaseFound.indexOf(i) == -1) {
                                savePurchaseItemValueToPref(purchaseItemIDs[i], false)
                            }
                        }
                    }
                    else {
                        for (purchaseItem in purchaseItemIDs) {
                            savePurchaseItemValueToPref(purchaseItem, false)
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, purchaseItemDisplay)
        listView!!.adapter = arrayAdapter
        notifyList()



        models = ArrayList()
        models.add(PaymentModel(R.drawable.facee1,"Community Face","Weather,Battery,Clock,Battery Temp","5$","face1"))
        models.add(PaymentModel(R.drawable.face2,"Remote Face","Battery,Clock,Calendar","3.5$","face2"))
        models.add(PaymentModel(R.drawable.face3,"Counter Face","Step Counter,Battery,Kcal","4$","face3"))


        recycler5.layoutManager = LinearLayoutManager(this)
        recycler5.adapter = PaymentRecycler(models,this@MainActivity)



       /* listView!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->


            }*/







       compositeDisposable = CompositeDisposable()
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        if (isNetworkAvailbale()){
            dataGet()
        }


    }


    fun  isNetworkAvailbale():Boolean{
        val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo =conManager.activeNetworkInfo
        return internetInfo!=null && internetInfo.isConnected
    }


    fun dataGet(){
        var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(API::class.java)

        compositeDisposable.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::DataHandler)
        )
    }

    fun DataHandler(storeModel: List<StoreModel>){
        storeModel.let {
            recyclerView.adapter = StoreRecycler(it as ArrayList<StoreModel>,this@MainActivity)
        }
    }

    override fun OnItemClickListener(storeModel: StoreModel) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(storeModel.link))
        startActivity(browserIntent)
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













     private fun notifyList() {
        purchaseItemDisplay.clear()
        for (p in purchaseItemIDs) {
            purchaseItemDisplay.add("Purchase Status of " + p + " = " + getPurchaseItemValueFromPref(p))
        }
        arrayAdapter!!.notifyDataSetChanged()
    }

    private val preferenceObject: SharedPreferences
        private get() = applicationContext.getSharedPreferences(PREF_FILE, 0)
    private val preferenceEditObject: SharedPreferences.Editor
        private get() {
            val pref = applicationContext.getSharedPreferences(PREF_FILE, 0)
            return pref.edit()
        }

    private fun getPurchaseItemValueFromPref(PURCHASE_KEY: String): Boolean {
        return preferenceObject.getBoolean(PURCHASE_KEY, false)
    }

    private fun savePurchaseItemValueToPref(PURCHASE_KEY: String, value: Boolean) {
        preferenceEditObject.putBoolean(PURCHASE_KEY, value).commit()
    }

    private fun initiatePurchase(PRODUCT_ID: String) {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient!!.querySkuDetailsAsync(params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList[0])
                        .build()
                    billingClient!!.launchBillingFlow(this@MainActivity, flowParams)
                }
                else {
                    //try to add item/product id "p1" "p2" "p3" inside managed product in google play console
                    Toast.makeText(applicationContext, "Purchase Item $PRODUCT_ID not Found", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(applicationContext,
                    " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        //if item newly purchased
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
            val alreadyPurchases = queryAlreadyPurchasesResult.purchasesList
            alreadyPurchases?.let { handlePurchases(it) }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(applicationContext, "Purchase Canceled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            val index = purchaseItemIDs.indexOf(purchase.sku)
            //purchase found
            if (index > -1) {

                //if item is purchased
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                        // Invalid purchase
                        // show error to user
                        Toast.makeText(applicationContext, "Error : Invalid Purchase", Toast.LENGTH_SHORT).show()
                        continue  //skip current iteration only because other items in purchase list must be checked if present
                    }
                    // else purchase is valid
                    //if item is purchased and not  Acknowledged
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()
                        billingClient!!.acknowledgePurchase(acknowledgePurchaseParams
                        ) { billingResult ->
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                //if purchase is acknowledged
                                //then saved value in preference
                                savePurchaseItemValueToPref(purchaseItemIDs[index], true)

                                // create path
                                val path = this.getExternalFilesDir(null)
                                /// create folder
                                val letDirectory = File(path, "752")
                                letDirectory.mkdirs()
                                /// create TXT
                                val file = File(letDirectory, "752.txt")
                                /// write txt
                                file.appendText("${purchaseItemIDs[index]},")

                                Toast.makeText(applicationContext, purchaseItemIDs[index] + " Item Purchased", Toast.LENGTH_SHORT).show()
                                notifyList()
                            }
                        }
                    }
                    else {
                        // Grant entitlement to the user on item purchase
                        if (!getPurchaseItemValueFromPref(purchaseItemIDs[index])) {
                            savePurchaseItemValueToPref(purchaseItemIDs[index], true)
                            // create path
                            val path = this.getExternalFilesDir(null)
                            /// create folder
                            val letDirectory = File(path, "752")
                            letDirectory.mkdirs()
                            /// create TXT
                            val file = File(letDirectory, "752.txt")
                            /// write txt
                            file.appendText("${purchaseItemIDs[index]},")

                            Toast.makeText(applicationContext, purchaseItemIDs[index] + " Item Purchased.", Toast.LENGTH_SHORT).show()
                            notifyList()
                        }
                    }
                }
                else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    Toast.makeText(applicationContext, purchaseItemIDs[index] + " Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show()
                }
                else if (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                    //mark purchase false in case of UNSPECIFIED_STATE
                    savePurchaseItemValueToPref(purchaseItemIDs[index], false)
                    Toast.makeText(applicationContext, purchaseItemIDs[index] + " Purchase Status Unknown", Toast.LENGTH_SHORT).show()
                    notifyList()
                }
            }
        }
    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     *
     * Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     *
     */
    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            val base64Key = ""
            com.sout.custom_apple_watch.View.Security.verifyPurchase(base64Key, signedData, signature)

        } catch (e: IOException) {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null) {
            billingClient!!.endConnection()
        }
    }

    companion object {
        const val PREF_FILE = "MyPref"
        lateinit var sharedPreferences: SharedPreferences

        //note add unique product ids
        //use same id for preference key
        private val purchaseItemIDs: ArrayList<String> = object : ArrayList<String>() {
            init {
                add("face1")
                add("face2")
                add("face3")
            }
        }
        private val purchaseItemDisplay = ArrayList<String>()

    }

    override fun onItemClick(paymentModel: PaymentModel) {
        if (getPurchaseItemValueFromPref(paymentModel.purchaseItemIDs)) {
            Toast.makeText(
                applicationContext,
                paymentModel.purchaseItemIDs + " is Already Purchased",
                Toast.LENGTH_SHORT
            ).show()
            //selected item is already purchased
            //return@OnItemClickListener
        }
        //initiate purchase on selected product item click
        //check if service is already connected
        if (billingClient!!.isReady) {
            initiatePurchase(paymentModel.purchaseItemIDs)
        } else {
            billingClient =
                BillingClient.newBuilder(this@MainActivity).enablePendingPurchases()
                    .setListener(this@MainActivity).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(paymentModel.purchaseItemIDs)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error " + billingResult.debugMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
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


    private fun fetchLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }



    }









}