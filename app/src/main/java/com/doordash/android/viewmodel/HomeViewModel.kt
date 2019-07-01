package com.doordash.android.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.doordash.android.network.dto.RestaurantDTO
import com.doordash.android.network.service.NetworkServiceProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Ashish Thirunavalli on 30 June 2019.
 *
 * Android view model to support view
 */

class HomeViewModel: ViewModel() {

    val CLS_TAG = HomeViewModel::class.java.simpleName

    //this will hold the data. observe on this if data is needed.
    var restaurantsLiveData: MutableLiveData<List<RestaurantDTO>> = MutableLiveData()
    var errorGettingRestaurantsLiveData: MutableLiveData<Throwable> = MutableLiveData()
    var progressBarVisibilityLiveData: MutableLiveData<Int> = MutableLiveData()

    //network data provider
    var networkServiceProvider = NetworkServiceProvider()

    //when you want to dispose bunch of observables at once this is great.
    private val bagDisposable = CompositeDisposable()

    //Scheduler
    var scheduler: Scheduler = Schedulers.io()

    init {
        //initial states.
        progressBarVisibilityLiveData.value = VISIBLE

        //load data as soon as view model is created.
        loadRestaurantsData("37.422740", "-122.139956", "0", "50")
    }

    fun loadRestaurantsData(latitude: String, longitude: String, offSet: String, limit: String){
        bagDisposable.add(networkServiceProvider.gatewayApi().getRestaurants(latitude,longitude,offSet,limit)
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    restaurantsLiveData.value = it
                    progressBarVisibilityLiveData.value = GONE
                },
                {
                    progressBarVisibilityLiveData.value = GONE
                    errorGettingRestaurantsLiveData.value = it
                    Log.e(CLS_TAG, "network call failed", it)
                }
            )

        )
    }
    fun refreshAllData(){
        //As of now its only one observable call. In future I could have multiple observed her if needed.
        loadRestaurantsData("37.422740", "-122.139956", "0", "50")
    }

    override fun onCleared() {
        super.onCleared()
        bagDisposable.dispose()
    }
}