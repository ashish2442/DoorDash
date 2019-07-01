package com.doordash.android.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Ashish Thirunavalli on 30 June 2019.
 */

open class RestaurantDTO(

    @SerializedName("id")
    open val restaurantId: String?,

    @SerializedName("name")
    open val  restaurantName: String?,

    @SerializedName("status")
    open val  statusOfRestaurant: String?,

    @SerializedName("status_type")
    open val  statusType: String?,

    @SerializedName("description")
    open val  description: String?,

    @SerializedName("cover_img_url")
    open val  coverImageUrl: String?
)
