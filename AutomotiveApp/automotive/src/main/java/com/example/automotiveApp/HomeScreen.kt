package com.example.automotiveApp

import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.*
import androidx.core.graphics.drawable.IconCompat
import com.example.AutomotiveApp.R

class HomeScreen(carContext: CarContext): Screen(carContext) {
    override fun onGetTemplate(): Template {
        val TAG = "Icon"
        val mGridIcon = IconCompat.createWithResource(carContext, R.drawable.ic_car_icon)
        val mGridAlertIcon = IconCompat.createWithResource(carContext, R.drawable.ic_alert_icon)
        val mGridSearchIcon = IconCompat.createWithResource(carContext, R.drawable.ic_search_icon)

        val gridItemCar = GridItem.Builder()
            .setTitle("Car Info")
            .setImage(CarIcon.Builder(mGridIcon).build(), GridItem.IMAGE_TYPE_LARGE)
            .setOnClickListener {
                Log.d(TAG, "GridItemCar was clicked")
                screenManager.push(CarScreen(carContext))
            }.build()

        val gridItemAlerts = GridItem.Builder()
            .setTitle("Alerts")
            .setImage(CarIcon.Builder(mGridAlertIcon).build(), GridItem.IMAGE_TYPE_LARGE)
            .setOnClickListener {
                Log.d(TAG, "GridItemAlerts was clicked")
            }.build()

        val gridItemSearch = GridItem.Builder()
            .setTitle("Search")
            .setImage(CarIcon.Builder(mGridSearchIcon).build(), GridItem.IMAGE_TYPE_LARGE)
            .setOnClickListener {
                Log.d(TAG, "GridItemSearch was clicked")
            }.build()

        val gridList = ItemList.Builder()
            .addItem(gridItemCar)
            .addItem(gridItemAlerts)
            .addItem(gridItemSearch)
            .build()

        return GridTemplate.Builder()
            .setTitle("Car Information App")
            .setSingleList(gridList)
            .build()
    }
}