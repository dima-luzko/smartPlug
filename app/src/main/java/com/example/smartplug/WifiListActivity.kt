package com.example.smartplug

import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartplug.adapter.WifiListAdapter
import com.example.smartplug.databinding.ActivityWifiListBinding

class WifiListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWifiListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detectWifi()
    }

    private fun detectWifi() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.startScan()
        val list = wifiManager.scanResults


        val a = listOf(
            WifiList(
                ssid = list.forEach { it.SSID },
                level = 1
            )
        )
        with(binding.wifiLinkList) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = WifiListAdapter(a)
            hasFixedSize()
        }

    }

//    private fun setAdapterList() {
//        with(binding.wifiLinkList) {
//            layoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//            adapter = WifiListAdapter(list)
//            hasFixedSize()
//        }
//    }

//    private val list = listOf(
//        WifiList(
//            ssid = "wqqwqwqwqwqwq",
//            level = 1
//        ),
//        WifiList(
//            ssid = "asfasgsdgsdgsd",
//            level = 1
//        ),
//        WifiList(
//            ssid = "wqqwqwqwlasdkkfsdjk;",
//            level = 1
//        ),
//        WifiList(
//            ssid = "5456576",
//            level = 1
//        ),
//        WifiList(
//            ssid = "wqqwqfsdfs4fs54",
//            level = 1
//        )
//    )

}