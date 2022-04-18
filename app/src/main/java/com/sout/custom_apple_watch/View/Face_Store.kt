package com.sout.custom_apple_watch.View

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.android.billingclient.api.*
import com.sout.custom_apple_watch.R
import java.io.IOException
import kotlinx.android.synthetic.main.activity_face_store.*
import kotlinx.android.synthetic.main.item.*
import java.io.File
import java.io.FileOutputStream


class Face_Store : AppCompatActivity() , PurchasesUpdatedListener{

    private var billingClient: BillingClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_store)


        /// style
        window.statusBarColor = ContextCompat.getColor(this, R.color.mycolor1)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
        val colorDrawable = ColorDrawable(Color.parseColor("#29397C"))
        supportActionBar!!.setBackgroundDrawable(colorDrawable)
        setTitle("Buy Face")
        centerTitle()
        ////


        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases().setListener(this).build()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryPurchase = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
                    val queryPurchases: List<Purchase>? = queryPurchase.purchasesList
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        handlePurchases(queryPurchases)
                    }
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
                    else{
                        savePurchaseValueToPref(false);
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })

        //item Purchased
        if (purchaseValueFromPref) {
            button_satın_Al!!.visibility = View.GONE
            purchase_status1!!.text = "Purchase Status : Purchased"
            checking = true
            setFolder()
            strli = "Purchase Status : Purchased"

        }
        //item not Purchased
        else {
            button_satın_Al!!.visibility = View.VISIBLE
            purchase_status1!!.text = "Purchase Status : Not Purchased"

        }



    }




    private val preferenceObject: SharedPreferences
        get() = applicationContext.getSharedPreferences(PREF_FILE, 0)

    private val preferenceEditObject: SharedPreferences.Editor
        get() {
            val pref: SharedPreferences = applicationContext.getSharedPreferences(PREF_FILE, 0)
            return pref.edit()
        }

    private val purchaseValueFromPref: Boolean
        get() = preferenceObject.getBoolean(PURCHASE_KEY, false)

    private fun savePurchaseValueToPref(value: Boolean) {
        preferenceEditObject.putBoolean(PURCHASE_KEY, value).commit()
    }



    fun satınal(view:View){

         PURCHASE_KEY = "face1"
        PRODUCT_ID = "face1"

        if (billingClient!!.isReady) {
            initiatePurchase()
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }


    fun button_satın_Al1(view: View){
        PURCHASE_KEY = "face2"
        PRODUCT_ID = "face2"
        if (billingClient!!.isReady) {
            initiatePurchase()
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }







    fun button_satın_Al2(view: View){
        PURCHASE_KEY = "face3"
        PRODUCT_ID = "face3"
        if (billingClient!!.isReady) {
            initiatePurchase()
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }






    //initiate purchase on button click
    fun purchase(view: View?) {
        //check if service is already connected

        if (billingClient!!.isReady) {
            initiatePurchase()
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }


    private fun initiatePurchase() {

        val skuList: MutableList<String> = java.util.ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient!!.querySkuDetailsAsync(params.build())
        { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList[0])
                        .build()
                    billingClient!!.launchBillingFlow(this@Face_Store, flowParams)
                } else {
                    //try to add item/product id "purchase" inside managed product in google play console

                    Toast.makeText(applicationContext, "Purchase Item not Found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext,
                    " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        //if item newly purchased

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
            val alreadyPurchases: List<Purchase>? = queryAlreadyPurchasesResult.purchasesList
            if (alreadyPurchases != null) {
                handlePurchases(alreadyPurchases)
            }
        }
        //if purchase cancelled
        else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(applicationContext, "Purchase Canceled", Toast.LENGTH_SHORT).show()
        }
        // Handle any other error msgs
        else {
            Toast.makeText(applicationContext, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            //if item is purchased

            if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                    // Invalid purchase
                    // show error to user

                    Toast.makeText(applicationContext, "Error : Invalid Purchase", Toast.LENGTH_SHORT).show()
                    return
                }
                // else purchase is valid
                //if item is purchased and not acknowledged


                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient!!.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase)
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity

                    if (!purchaseValueFromPref) {
                        savePurchaseValueToPref(true)
                        strli = "Purchase Status : Purchased"
                        Toast.makeText(applicationContext, "Item Purchased >> ${strli}", Toast.LENGTH_SHORT).show()
                        checking = true
                        setFolder()
                        recreate()
                    }
                }
            }
            //if purchase is pending
            else if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                Toast.makeText(applicationContext,
                    "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show()
            }
            //if purchase is refunded or unknown
            else if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false)
                purchase_status1!!.text = "Purchase Status : Not Purchased"
                button_satın_Al!!.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "Purchase Status Unknown", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var ackPurchase = AcknowledgePurchaseResponseListener { billingResult ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            //if purchase is acknowledged
            // Grant entitlement to the user. and restart activity

            savePurchaseValueToPref(true)
            strli = "Purchase Status : Purchased"
            Toast.makeText(applicationContext, "Item Purchased >> ${strli}", Toast.LENGTH_SHORT).show()
            checking = true
            setFolder()
            recreate()
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
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.

            val base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnmq7pDc70D1VYvF+EXx3DuvMBpUb719KW7yCkuxSRysbgvzCnXmJT8ORISdg16FZFiHR5U1Z1b6DjOu5xgXDlnUpKYKfm8auB1eXGQSCAZkw1zdq2w29QVvoZp6CJc2t4ZWOWsrBTLdeR4fNA4wwXX+F6LPzBHw+97D0noJHwAb52PaexSb6SLvMu9J1Rerko33WSuL4tiETDy+h33Q+xjJIDMdPFvk2hjl3WfDy8VBahRJwZmMzsNwoTspPCnPNc3obiH0o/0ePbmwYEYxxm/jMZ3HOrk6Bj6rnmItAusaDO1J2tYy6Hv2kaOBjgav+zWTw27ViTIrr+BIHGWDwgQIDAQAB"
            ///Security.verifyPurchase(base64Key, signedData, signature)
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
        var PURCHASE_KEY = ""
        var PRODUCT_ID = ""
        var checking = false
        var strli = ""
        var check1 = false
    }


    fun setFolder(){
        val path = this.getExternalFilesDir(null)
        val letDirectory = File(path, "752")
        val file = File(letDirectory, "752.txt")
        FileOutputStream(file).use {
            it.write("${checking.toString()}".toByteArray())
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





}