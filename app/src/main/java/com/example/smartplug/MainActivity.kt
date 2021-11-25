package com.example.smartplug

import android.R
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartplug.databinding.ActivityMainBinding
import java.io.DataOutputStream
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val MAC_ADDRESS_ESP_32 = "9C:9C:1F:E2:44:96"
        private const val REQUEST_ENABLE_BT = 1
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var btArray: Array<BluetoothDevice?>
    private var socket: BluetoothSocket? = null
    private var isClick1 = true
    private var isClick2 = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //checkBluetoothEnable()

//        val blueManager = getSystemService(BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
//        val bluetoothAdapter = blueManager.adapter
//
//        binding.buttonConnect.setOnClickListener {
//            if (!bluetoothAdapter.isDiscovering) {
//                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
//                startActivityForResult(intent,0)
//            }
//        }
//
//        binding.buttonList.setOnClickListener {
//            val bt: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
//
//            val strings = arrayOfNulls<String>(bt.size)
//            btArray = arrayOfNulls(bt.size)
//            var index = 0
//
//            if (bt.isNotEmpty()) {
//                for (device in bt) {
//                    btArray[index] = device
//                    strings[index] = device.name
//                    index++
//                }
//                val arrayAdapter = ArrayAdapter(
//                    applicationContext, R.layout.simple_list_item_1, strings
//                )
//                binding.list.adapter = arrayAdapter
//            }
//        }
//
////        binding.list.setBackgroundColor(Color.LTGRAY)
//        binding.list.onItemClickListener = OnItemClickListener { _, _, i, _ ->
//            val device = btArray[i] as BluetoothDevice
//            val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//            try {
//                socket = device.createRfcommSocketToServiceRecord(uuid)
//                socket?.connect()
//                if (socket?.isConnected == true){
//                    Toast.makeText(this,"Device connected", Toast.LENGTH_LONG).show()
//                }
//                else {
//                    Toast.makeText(this,"Device disconnected", Toast.LENGTH_LONG).show()
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//
//        binding.buttonOn.setOnClickListener {
//            if (isClick1){
//                val test = charArrayOf(
//                    0xFF.toChar(),
//                    0x2B.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x10.toChar(),
//                    0x10.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0xA1.toChar(),
//                    0xFF.toChar()
//                )
//                write(test)
//                binding.buttonOn.text = "ON1"
//                binding.buttonOn.setBackgroundColor(Color.GREEN)
//                isClick1 = false
//            } else {
//                val test = charArrayOf(
//                    0xFF.toChar(),
//                    0x2B.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x10.toChar(),
//                    0x20.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0xA1.toChar(),
//                    0xFF.toChar()
//                )
//                write(test)
//                binding.buttonOn.text = "OFF1"
//                binding.buttonOn.setBackgroundColor(Color.RED)
//                isClick1 = true
//            }
//        }
//
//        binding.buttonOn2.setOnClickListener {
//            if (isClick2) {
//                val test = charArrayOf(
//                    0xFF.toChar(),
//                    0x2B.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x10.toChar(),
//                    0x01.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0xA1.toChar(),
//                    0xFF.toChar()
//                )
//                write(test)
//                binding.buttonOn2.text = "ON2"
//                binding.buttonOn2.setBackgroundColor(Color.GREEN)
//                isClick2 = false
//            } else {
//                val test = charArrayOf(
//                    0xFF.toChar(),
//                    0x2B.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x10.toChar(),
//                    0x02.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0x00.toChar(),
//                    0xA1.toChar(),
//                    0xFF.toChar()
//                )
//                write(test)
//                binding.buttonOn2.text = "OFF2"
//                binding.buttonOn2.setBackgroundColor(Color.RED)
//                isClick2 = true
//            }
//
//        }
//
////        fixedRateTimer("timer",false,0,5000){
////            val test = charArrayOf(
////                0xFF.toChar(),
////                0x2B.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x10.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0x00.toChar(),
////                0xA1.toChar(),
////                0xFF.toChar()
////            )
////            if (socket?.isConnected == true) {
////                this@MainActivity.runOnUiThread {
////                    Toast.makeText(this@MainActivity, "$test", Toast.LENGTH_SHORT).show()
////                    write(test)
////                }
////            }
////        }
//
//    }
//
//    private fun write(ch: CharArray) {
//        try {
//            for (k in ch.indices) {
//                DataOutputStream(socket!!.outputStream).writeByte(ch[k].toInt())
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun checkBluetoothEnable() {
//        val blueManager = getSystemService(BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager
//        val bluetoothAdapter = blueManager.adapter
//
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "Bluetooth not available.", Toast.LENGTH_LONG).show()
//            finish()
//        }
//
//        if (!bluetoothAdapter.isEnabled) {
//            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        checkBluetoothEnable()
//    }
}}