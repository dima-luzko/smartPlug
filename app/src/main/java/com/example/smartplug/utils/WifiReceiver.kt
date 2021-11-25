package com.example.smartplug.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager

class WifiReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        if(intent?.action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
////            val mScanResults: List<ScanResult> = mWifiManager.getScanResults()
//        }
    }
}