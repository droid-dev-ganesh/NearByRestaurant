package com.ganesh.nearbyrestaurant.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ganesh.nearbyrestaurant.R
import com.ganesh.nearbyrestaurant.models.NewLocationModel
import com.ganesh.nearbyrestaurant.views.homepage.HomePageActivity

class LocationAdapter (val arrayList : ArrayList<NewLocationModel>, val context: Context) : RecyclerView.Adapter<LocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_popup_location, parent, false)
        return LocationViewHolder(v)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {

        val item = arrayList[position]

        holder.textViewTitle.text = item.title
        holder.textViewCountyName.text = item.countryName

        holder.textViewTitle.setOnClickListener {
            try {
                (context as HomePageActivity).onLocationClicked(item)
            } catch (e: Exception) {
            }

        }



        holder.parentConstraintLay.setOnClickListener {
            try {
                (context as HomePageActivity).onLocationClicked(item)
            } catch (e: Exception) {
            }

        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}

class LocationViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
    val textViewCountyName = view.findViewById<TextView>(R.id.textViewCountyName)
    val parentConstraintLay = view.findViewById<ConstraintLayout>(R.id.parentConstraintLay)
}