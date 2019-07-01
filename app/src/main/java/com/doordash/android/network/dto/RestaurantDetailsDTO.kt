package com.doordash.android.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashish Thirunavalli on 30 June 2019.
 */
data class RestaurantDetailsDTO (
    val id: String?,

    @SerializedName("phone_number")
    val phoneNumber: String?,

    @SerializedName("cover_img_url")
    val coverImageURL: String?,

    @SerializedName("name")
    val restaurantName: String?,

    @SerializedName("yelp_rating")
    val yelpRating: String?
)