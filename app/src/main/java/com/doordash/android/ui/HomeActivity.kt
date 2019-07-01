package com.doordash.android.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import com.doordash.android.R
import com.doordash.android.adapter.RestaurantsAdapter
import com.doordash.android.utils.RESTAURANT_ID
import com.doordash.android.utils.recyclerview.RecyclerViewClickEventHandler
import com.doordash.android.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.progress_bar



class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {

        //show icon in app bar
        showAppIcon()

        //Initialize Recyclerview
        initRecyclerView()

        //init view model
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        //subscribe for view model events.
        subscribeForRestaurantsDataEvent()
        subscribeForFetchRestaurantsFailureEvent()
        subscribeForProgressBarVisibilityLiveData()
    }

    private fun showAppIcon() {
        supportActionBar?.setIcon(R.mipmap.ic_launcher_custom)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initRecyclerView() {
        restaurants_recycler_view.layoutManager = LinearLayoutManager(this)
        restaurants_recycler_view.adapter = RestaurantsAdapter()
        restaurants_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        setClickActionForRecycleViewItems()
    }

    private fun setClickActionForRecycleViewItems() {
        val recycleViewItemClickEventsHandler = RecyclerViewClickEventHandler(restaurants_recycler_view)
        recycleViewItemClickEventsHandler.setOnItemClickListener { recyclerView, view, position, viewType ->
            val restaurants = (recyclerView.adapter as RestaurantsAdapter).getAdapterItems()
            if (position < restaurants.size) {
                val intent = Intent(baseContext, RestaurantDetailsActivity::class.java)
                intent.putExtra(RESTAURANT_ID, restaurants[position].restaurantId)
                startActivity(intent)
            }
        }
    }

    private fun subscribeForRestaurantsDataEvent() {
        homeViewModel.restaurantsLiveData.observe(this, Observer { restaurants ->
            restaurants?.let {
                (restaurants_recycler_view.adapter as RestaurantsAdapter).updateAdapter(it)
            }
        })
    }

    private fun subscribeForFetchRestaurantsFailureEvent() {
        homeViewModel.errorGettingRestaurantsLiveData.observe(this, Observer { throwable ->
            showSnackBar()
        })
    }

    private fun showSnackBar() {
        val snackbar = Snackbar
            .make(root, getString(R.string.error_could_not_load_data), Snackbar.LENGTH_LONG)
            .setAction("RETRY") {
                homeViewModel.refreshAllData()
            }

        // Changing message text color
        snackbar.setActionTextColor(resources.getColor(R.color.colorAccent, null))
        snackbar.show()
    }

    private fun subscribeForProgressBarVisibilityLiveData() {
        homeViewModel.progressBarVisibilityLiveData.observe(this, Observer { visibility ->
            progress_bar.visibility = visibility ?: GONE
        })
    }
}
