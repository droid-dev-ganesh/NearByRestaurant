package com.ganesh.nearbyrestaurant.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ganesh.nearbyrestaurant.R
import com.ganesh.nearbyrestaurant.models.RestaurantModel
import com.ganesh.nearbyrestaurant.views.homepage.HomePageActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class RestaurantAdapter (val arrayList : ArrayList<RestaurantModel>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_restaurant_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = arrayList[position]
        if (item.thumbImageUrl.isNotEmpty()){
            Picasso.with(context)
                .load(item.thumbImageUrl)
                .placeholder(R.drawable.placeholder_product)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.imageView,object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError() {
                        Picasso.with(context)
                            .load(item.thumbImageUrl)
                            .placeholder(R.drawable.placeholder_product)
                            .error(R.drawable.placeholder_product)
                            .into(holder.imageView)
                    }
                })
        }else{
            holder.imageView.setImageResource(R.drawable.placeholder_product)
        }


        holder.textViewName.text = "Name: ${item.name}"

        holder.textViewCuisine.text = "Cuisines : ${item.cuisines}"

        holder.textViewAvgTwo.text = "${item.avgCostForTwo}  ${item.currency}"

        holder.textViewRating.text = "${item.aggregateRating}★  ${item.ratingText}"

        holder.textViewAddress.text = item.address

        if (item.phoneNumbers.isNotEmpty()){
            holder.textViewPhone.visibility =  View.VISIBLE
            holder.textViewPhone.text = "Contact: ${item.phoneNumbers}"
        }else{
            holder.textViewPhone.visibility = View.GONE
        }

        holder.parentConstraintLay.setOnClickListener{
            try {
                (context as HomePageActivity).callWebViewActivity(item.redirectUrl)
            } catch (e: Exception) {
            }
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val imageView = view.findViewById<ImageView>(R.id.imageView)
    val textViewName = view.findViewById<TextView>(R.id.textViewName)
    val textViewCuisine = view.findViewById<TextView>(R.id.textViewCuisine)
    val textViewAvgTwo = view.findViewById<TextView>(R.id.textViewAvgTwo)
    val textViewAddress = view.findViewById<TextView>(R.id.textViewAddress)
    val textViewPhone = view.findViewById<TextView>(R.id.textViewPhone)
    val textViewRating = view.findViewById<TextView>(R.id.textViewRating)
    val parentConstraintLay = view.findViewById<ConstraintLayout>(R.id.parentConstraintLay)

}