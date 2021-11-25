package com.example.smartplug


import android.Manifest
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartplug.databinding.WifiActivityBinding
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Runnable


class WifiActivity : AppCompatActivity() {

    private lateinit var binding: WifiActivityBinding
    private lateinit var request: Request
    private val client = OkHttpClient()
    var ssid: String? = null
    private var isClick1 = true
    private var isClick2 = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WifiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickSaveIp()
        getIp()

        val snackbarPermissionListener: PermissionListener =
            SnackbarOnDeniedPermissionListener.Builder
                .with(binding.root, "Camera access is needed to take pictures of your dog")
                .withOpenSettingsButton("Settings")
                .withCallback(object : Snackbar.Callback() {
                    override fun onShown(snackbar: Snackbar) {
                        // Event handler for when the given Snackbar is visible
                    }

                    override fun onDismissed(snackbar: Snackbar, event: Int) {
                        // Event handler for when the given Snackbar has been dismissed
                    }
                }).build()

        val dialogPermissionListener: PermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(this)
            .withTitle("Location permission")
            .withMessage("Location permission is needed to connect smart plug in wifi")
            .withButtonText("ok")
            .build()

        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(snackbarPermissionListener)
            .check()


        val connManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (networkInfo != null) {
            if (networkInfo.isConnected) {
                val wifiManager =
                    this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val connectionInfo = wifiManager.connectionInfo
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.ssid)) {
                    Log.d("ssidd", connectionInfo.ssid)
                }
            } else {
                Log.d("ssidd", "No Connection")
            }
        }

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
            var ssid = wifiInfo.ssid
            val level = WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
            Log.d("request", "ssid: $ssid, level: $level")
        }

        reload()


        binding.apply {
            bLed1.setOnClickListener(onClickListener())
            bLed2.setOnClickListener(onClickListener())
        }
    }


    private fun onClickListener(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.bLed1 -> {
                    post2()

                }
                R.id.bLed2 -> {
                    post3()
                }
            }
        }
    }

    private fun reload() {
        val handler = Handler()
        handler.postDelayed(
            {
                // Do something after 5min = 50000ms
                reload()
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO){
                        val response = WifiService(this@WifiActivity).getResponse("666")
                        val resultText = response?.power
                        withContext(Dispatchers.Main) {
                            binding.powerValue1.text = resultText.toString() + " Bт"
                            if (response?.led2 == true){
                                binding.bLed1.text = "led green on"
                                binding.bLed1.setBackgroundColor(Color.GREEN)
                            } else {
                                binding.bLed1.text = "led green off"
                                binding.bLed1.setBackgroundColor(Color.RED)
                            }

                            if (response?.led1 == true){
                                binding.bLed2.text = "led yellow on"
                                binding.bLed2.setBackgroundColor(Color.GREEN)
                            } else {
                                binding.bLed2.text = "led yellow off"
                                binding.bLed2.setBackgroundColor(Color.RED)
                            }
                            Log.d("request", "response: $resultText")
                        }
                    }
                }
            }, 5000
        )
    }


    private fun getIp() = with(binding) {
        val ip = PreferencesManager.getInstance(this@WifiActivity)
            .getString(PreferencesManager.PREF_IP_ADDRESS, "")
        if (ip != null) {
            if (ip.isNotEmpty()) {
                edIp.setText(ip)
            }
        }
    }

    private fun onClickSaveIp() = with(binding) {
        bSave.setOnClickListener {
            if (edIp.text.isNotEmpty()) saveIp(edIp.text.toString())
            Toast.makeText(this@WifiActivity, "Ip address save!", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveIp(ip: String) {
        PreferencesManager.getInstance(this).putString(PreferencesManager.PREF_IP_ADDRESS, ip)
    }


    private fun post1(post: String) {
        GlobalScope.launch(Dispatchers.Main) {
            if (isClick1) {
                withContext(Dispatchers.IO) {
                    val response = WifiService(this@WifiActivity).getResponse(post)
                    withContext(Dispatchers.Main) {
                        binding.bLed2.text = "led yellow on"
                        binding.bLed2.setBackgroundColor(Color.GREEN)
                    }
                }
            }
            withContext(Dispatchers.IO) {
                val response = WifiService(this@WifiActivity).getResponse(post)
                val resultText = response?.power
                withContext(Dispatchers.Main) {
                    binding.powerValue1.text = resultText.toString() + " Bт"
                    Log.d("request", "response: $resultText")
                }
            }

        }
    }

    private fun post2() {
        GlobalScope.launch(Dispatchers.Main) {
            if (isClick1) {
                withContext(Dispatchers.IO) {
                   val response =  WifiService(this@WifiActivity).getResponse("3")
                    withContext(Dispatchers.Main) {
                        if (response?.led2 == true){
                            binding.bLed1.text = "led green on"
                            binding.bLed1.setBackgroundColor(Color.GREEN)
                        } else {
                            binding.bLed1.text = "led green off"
                            binding.bLed1.setBackgroundColor(Color.RED)
                        }
                    }
                }
                isClick1 = false
            } else {
                withContext(Dispatchers.IO) {
                    val response =  WifiService(this@WifiActivity).getResponse("4")
                    withContext(Dispatchers.Main) {
                        if (response?.led2 == true){
                            binding.bLed1.text = "led green on"
                            binding.bLed1.setBackgroundColor(Color.GREEN)
                        } else {
                            binding.bLed1.text = "led green off"
                            binding.bLed1.setBackgroundColor(Color.RED)
                        }
                    }
                }
                isClick1 = true
            }
        }
    }

    private fun post3() {
        GlobalScope.launch(Dispatchers.Main) {
            if (isClick2) {
                withContext(Dispatchers.IO) {
                    val response =  WifiService(this@WifiActivity).getResponse("1")
                    withContext(Dispatchers.Main) {
                        if (response?.led1 == true){
                            binding.bLed2.text = "led yellow on"
                            binding.bLed2.setBackgroundColor(Color.GREEN)
                        } else {
                            binding.bLed2.text = "led yellow off"
                            binding.bLed2.setBackgroundColor(Color.RED)
                        }
                    }
                }
                isClick2 = false
            } else {
                withContext(Dispatchers.IO) {
                    val response =  WifiService(this@WifiActivity).getResponse("2")
                    withContext(Dispatchers.Main) {
                        if (response?.led1 == true){
                            binding.bLed2.text = "led yellow on"
                            binding.bLed2.setBackgroundColor(Color.GREEN)
                        } else {
                            binding.bLed2.text = "led yellow off"
                            binding.bLed2.setBackgroundColor(Color.RED)
                        }
                    }
                }
                isClick2 = true
            }
        }
    }


}
