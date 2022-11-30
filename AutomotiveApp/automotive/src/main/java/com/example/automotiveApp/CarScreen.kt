package com.example.automotiveApp

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.*

class CarScreen(carContext: CarContext): Screen(carContext) {

    companion object {
        private const val TAG = "CarScreen"

        private const val GEAR_UNKNOWN = "GEAR_UNKNOWN"

        // Values are taken from android.car.hardware.CarSensorEvent class.
        private val VEHICLE_GEARS = mapOf(
            0x0000 to GEAR_UNKNOWN,
            0x0001 to "GEAR_NEUTRAL",
            0x0002 to "GEAR_REVERSE",
            0x0004 to "GEAR_PARK",
            0x0008 to "GEAR_DRIVE"
        )
    }

    private lateinit var car: Car
    private lateinit var carPropertyManager: CarPropertyManager

    private var carPropertyListener = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(p0: CarPropertyValue<Any>?) {
            Log.d(TAG, "Received on changed car property event")
            //Log.d(TAG, VEHICLE_GEARS.getOrDefault(p0?.value as Int, GEAR_UNKNOWN))
            Log.d(TAG, p0?.value.toString())
        }

        override fun onErrorEvent(p0: Int, p1: Int) {
            Log.w(TAG, "Received error car property event.")
        }
    }

    override fun onGetTemplate(): Template {

        car = Car.createCar(carContext)
        carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager

        carPropertyManager.registerCallback(
            carPropertyListener,
            VehiclePropertyIds.IGNITION_STATE,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        // carPropertyManager.setBooleanProperty(NIGHT_MODE, , false)
        val row = Row.Builder().setTitle("Car").build()

        val pane = Pane.Builder()
            .addRow(row)
            .build()

        return PaneTemplate.Builder(pane)
            .setHeaderAction(Action.BACK)
            .build()
    }
}