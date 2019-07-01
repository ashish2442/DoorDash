package com.doordash.android.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.doordash.android.utils.RESTAURANT_ID
import com.doordash.android.viewmodel.RestaurantDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurent_details.*
import android.support.design.widget.Snackbar
import android.widget.TextView
import com.doordash.android.R


class RestaurantDetailsActivity : AppCompatActivity() {

    lateinit var restaurantId: String
    private lateinit var restaurantDetailsViewModel: RestaurantDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurent_details)
        init()
    }

    private fun init() {

        //these shows back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //get Intent data
        restaurantId = intent?.extras?.getString(RESTAURANT_ID) ?: ""

        //init view model
        restaurantDetailsViewModel = ViewModelProviders.of(this).get(RestaurantDetailsViewModel::class.java)
        restaurantDetailsViewModel.restaurantId = restaurantId

        //subscribe for view model events.
        subscribeForRestaurantDetailsEvent()
        subscribeForFetchRestaurantDetailsFailEvent()
        subscribeForProgressBarVisibilityLiveData()
        subscribeForContentVisibilityLiveData()
    }

    private fun subscribeForRestaurantDetailsEvent() {
        restaurantDetailsViewModel.restaurantDetailsLiveData.observe(this, Observer { restaurant ->
            restaurant?.let {
                restaurant_name.text = it.restaurantName
                phone_number.text = it.phoneNumber
                yelp_rating.text = it.yelpRating

                if (!it.coverImageURL.isNullOrEmpty()) {
                    Picasso.get().load(it.coverImageURL)
                        .resize(1080, 720)
                        .onlyScaleDown() // the image will only be resized if it's bigger than resize pixels.
                        .into(main_image)
                }
            }
        })
    }

    private fun subscribeForFetchRestaurantDetailsFailEvent() {
        restaurantDetailsViewModel.errorGettingRestaurantDetailsLiveData.observe(this, Observer { throwable ->
            //Showing raw failure message as of now to user, to known what going on.
            showSnackBar()
        })
    }

    private fun showSnackBar() {
        val snackbar = Snackbar
            .make(root, getString(R.string.error_could_not_load_data), Snackbar.LENGTH_LONG)
            .setAction("RETRY") {
                restaurantDetailsViewModel.refreshAllData(restaurantId)
            }

        // Changing message text color
        snackbar.setActionTextColor(resources.getColor( R.color.colorAccent, null))
        snackbar.show()
    }
    private fun subscribeForProgressBarVisibilityLiveData() {
        restaurantDetailsViewModel.progressBarVisibilityLiveData.observe(this, Observer { visibility ->
            progress_bar.visibility = visibility ?: View.GONE
        })
    }

    private fun subscribeForContentVisibilityLiveData() {
        restaurantDetailsViewModel.contentVisibilityLiveData.observe(this, Observer { visibility ->
            content.visibility = visibility ?: View.GONE
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
