package com.example.smartplug.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartplug.WifiList
import com.example.smartplug.databinding.WifiListItemBinding

class WifiListAdapter(private val wifiList: List<WifiList>) : RecyclerView.Adapter<WifiListAdapter.ViewHolder>() {

    class ViewHolder(val binding: WifiListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WifiListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = wifiList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list : WifiList = wifiList[position]
        with(holder){
            with(binding) {
                ssidName.text = list.ssid.toString()
            }
        }
    }



}