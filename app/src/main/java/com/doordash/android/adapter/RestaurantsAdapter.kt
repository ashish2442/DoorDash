package com.doordash.android.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doordash.android.R
import com.doordash.android.network.dto.RestaurantDTO
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_item_row.view.*

/**
 * Created by Ashish Thirunavalli on 30 June 2019.
 *
 * Adapter for filling up list of restaurants
 */
class RestaurantsAdapter: RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>() {

    var restaurants: List<RestaurantDTO> = listOf()


    fun updateAdapter(restaurants: List<RestaurantDTO>){
        this.restaurants = restaurants
        notifyDataSetChanged()
    }

    fun getAdapterItems(): List<RestaurantDTO> {
        return restaurants
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item_row, parent, false)
        return RestaurantsViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
       holder.bindView(restaurants[position])
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    /**
     * custom view holder class
     */
    inner class RestaurantsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(restaurant: RestaurantDTO){
            itemView.name.text = restaurant.restaurantName
            itemView.description.text = restaurant.description
            itemView.status.text = restaurant.statusOfRestaurant

            if(!restaurant.coverImageUrl.isNullOrEmpty()) {
                Picasso.get().load(restaurant.coverImageUrl)
                    .resize(200, 105)
                    .onlyScaleDown() // the image will only be resized if it's bigger than resize pixels.
                    .into(itemView.logo_image)
            }
        }
    }
}