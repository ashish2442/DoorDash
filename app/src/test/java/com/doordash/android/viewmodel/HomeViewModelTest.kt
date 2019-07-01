package com.doordash.android.viewmodel

import android.view.View
import com.doordash.android.network.dto.RestaurantDTO
import com.doordash.android.network.service.GatewayApi
import com.doordash.android.network.service.NetworkServiceProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleJust
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import java.util.ArrayList

import org.mockito.Mockito.*

/**
 * Created by Ashish Thirunavalli on 01 July 2019.
 *
 */

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class HomeViewModelTest {

    private var homeViewModel: HomeViewModel = HomeViewModel()

    private val mockRestaurantList: List<RestaurantDTO>
        get() {

            val restaurants = ArrayList<RestaurantDTO>()

            for (i in 1..10) {
                val restaurantDTO = mock(RestaurantDTO::class.java)
                `when`(restaurantDTO.restaurantId).thenReturn(i.toString() + "")
                `when`(restaurantDTO.restaurantName).thenReturn("testName $i")
                `when`(restaurantDTO.statusOfRestaurant).thenReturn("status $i")
                restaurants.add(restaurantDTO)
            }

            return restaurants
        }

    @Before
    fun setUp() {

        val networkServiceProvider = mock(NetworkServiceProvider::class.java)
        homeViewModel.scheduler = AndroidSchedulers.mainThread()

        val gatewayApi = Mockito.mock(GatewayApi::class.java)

        val singleObservable = SingleJust(mockRestaurantList)
        `when`(
            gatewayApi.getRestaurants(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(singleObservable)

        `when`(networkServiceProvider.gatewayApi()).thenReturn(gatewayApi)
        homeViewModel.networkServiceProvider = networkServiceProvider
    }

    @Test
    @Throws(Exception::class)
    fun testGetCLS_TAG() {
        val result = homeViewModel.CLS_TAG
        Assert.assertEquals("HomeViewModel", result)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRestaurantsLiveDataInitialState() {
        val result = homeViewModel.restaurantsLiveData
        val value = result.value
        //initially its null due to init block calling loadRestaurantsData.
        Assert.assertNull(value)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRestaurantsLiveDataAfterData() {
        homeViewModel.loadRestaurantsData("a","b","c","d")

        Assert.assertEquals( 10, homeViewModel.restaurantsLiveData.value?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRestaurantsLiveDataItems() {
        homeViewModel.loadRestaurantsData("a","b","c","d")
        val restaurantDTO = homeViewModel.restaurantsLiveData.value?.get(0) as RestaurantDTO
        Assert.assertEquals( "1", restaurantDTO.restaurantId)
        Assert.assertEquals( "testName 1", restaurantDTO.restaurantName)
        Assert.assertEquals( "status 1", restaurantDTO.statusOfRestaurant)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRestaurantsLiveDataEmpty() {

        //mock data
        val networkServiceProvider = mock(NetworkServiceProvider::class.java)
        val gatewayApi = Mockito.mock(GatewayApi::class.java)

        val singleObservable = SingleJust(listOf<RestaurantDTO>())
        `when`(
            gatewayApi.getRestaurants(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(singleObservable)

        `when`(networkServiceProvider.gatewayApi()).thenReturn(gatewayApi)
        homeViewModel.networkServiceProvider = networkServiceProvider


        homeViewModel.loadRestaurantsData("a","b","c","d")

        val restaurantDTO = homeViewModel.restaurantsLiveData.value
        Assert.assertEquals( 0, restaurantDTO?.size)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRestaurantsLiveDataFailure() {

        //mock data
        val networkServiceProvider = mock(NetworkServiceProvider::class.java)
        val gatewayApi = Mockito.mock(GatewayApi::class.java)

        val singleObservable: Single<List<RestaurantDTO>> = Single.error(Throwable("my error"))
        `when`(
            gatewayApi.getRestaurants(
                anyString(),
                anyString(),
                anyString(),
                anyString()
            )
        ).thenReturn(singleObservable)

        `when`(networkServiceProvider.gatewayApi()).thenReturn(gatewayApi)
        homeViewModel.networkServiceProvider = networkServiceProvider


        homeViewModel.loadRestaurantsData("a","b","c","d")

        val error = homeViewModel.errorGettingRestaurantsLiveData.value
        Assert.assertEquals( "my error", error?.message)
    }

    @Test
    @Throws(Exception::class)
    fun testProgressBarVisibility() {

        homeViewModel.loadRestaurantsData("a","b","c","d")

        val progressBar = homeViewModel.progressBarVisibilityLiveData.value
        Assert.assertEquals( View.GONE, progressBar)
    }
}
