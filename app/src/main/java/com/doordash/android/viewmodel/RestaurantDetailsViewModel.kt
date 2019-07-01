package com.doordash.android.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.doordash.android.network.dto.RestaurantDTO
import com.doordash.android.network.dto.RestaurantDetailsDTO
import com.doordash.android.network.service.NetworkServiceProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Ashish Thirunavalli on 01 July 2019.
 *
 * Android view model to support view
 */
class RestaurantDetailsViewModel: ViewModel() {

    val CLS_TAG = RestaurantDetailsViewModel::class.java.simpleName

    //this will hold the data. observe on this if data is needed.
    var restaurantDetailsLiveData: MutableLiveData<RestaurantDetailsDTO> = MutableLiveData()
    var errorGettingRestaurantDetailsLiveData: MutableLiveData<Throwable> = MutableLiveData()
    var progressBarVisibilityLiveData: MutableLiveData<Int> = MutableLiveData()
    var contentVisibilityLiveData: MutableLiveData<Int> = MutableLiveData()

    var restaurantId: String? = null
    set(value) {
        //load data when restaurantId Changes. Ideally this should change only once in this case. But in future if restaurantId changes for some reason we will always fetch latest data for restaurantId
        value?.let {
            loadRestaurantDetails(it)
        }
        field = value
    }
    //network data provider
    private val networkServiceProvider = NetworkServiceProvider()

    //when you want to dispose bunch of observables at once this is great.
    private val bagDisposable = CompositeDisposable()

    init {
        //initial states.
        progressBarVisibilityLiveData.value = VISIBLE
        contentVisibilityLiveData.value = GONE
    }
    fun loadRestaurantDetails(restaurantID: String){
        bagDisposable.add(networkServiceProvider.gatewayApi().getRestaurantDetails(restaurantID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    restaurantDetailsLiveData.value = it
                    progressBarVisibilityLiveData.value = GONE
                    contentVisibilityLiveData.value = VISIBLE
                },
                {
                    progressBarVisibilityLiveData.value = GONE
                    errorGettingRestaurantDetailsLiveData.value = it
                    Log.e(CLS_TAG, "network call failed", it)
                }
            )

        )
    }

    fun refreshAllData(restaurantID: String){
        //As of now its only one observable call. In future I could have multiple observed her if needed.
        loadRestaurantDetails(restaurantID)
    }
    override fun onCleared() {
        super.onCleared()
        bagDisposable.dispose()
    }
}