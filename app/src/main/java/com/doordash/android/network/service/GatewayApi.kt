package com.doordash.android.network.service

import com.doordash.android.network.dto.RestaurantDTO
import com.doordash.android.network.dto.RestaurantDetailsDTO
import com.doordash.android.utils.RESTAURANT_DETAILS_END_POINT
import com.doordash.android.utils.RESTAURANT_ID
import com.doordash.android.utils.RESTAURANT_LIST_END_POINT
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Ashish Thirunavalli on 30 June 2019.
 */
interface GatewayApi {

    /**
     * get list of resturents for giving lat long.
     *
     * @param latitude :latitude of place near which we need restaurant list.
     * @param longitude :longitude of place near which we need restaurant list.
     * @param offset : offset of place near which we need restaurant list.
     * @param limit :limit of restaurants.
     */
    @GET(RESTAURANT_LIST_END_POINT)
    fun getRestaurants(
        @Query("lat") latitude: String,
        @Query("lng") longitude: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String
    ): Single<List<RestaurantDTO>>

    /**
     * get restaurent details of given specific restaurant
     *
     * @param restaurantID : id of restaurant for which we need details
     */
    @GET(RESTAURANT_DETAILS_END_POINT)
    fun getRestaurantDetails(@Path(RESTAURANT_ID) restaurantID: String): Single<RestaurantDetailsDTO>
}