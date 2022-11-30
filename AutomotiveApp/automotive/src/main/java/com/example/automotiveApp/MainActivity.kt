package com.example.automotiveApp

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.AutomotiveApp.databinding.ActivityMainBinding
import com.example.automotiveApp.mqtt.MqttClient
import com.example.automotiveApp.retro.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"

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

    /** Car API. */
    private lateinit var car: Car
    private var ledMode = "Off"

    /**
     * An API to read VHAL (vehicle hardware access layer) properties. List of vehicle properties
     * can be found in {@link VehiclePropertyIds}.
     *
     * <p>https://developer.android.com/reference/android/car/hardware/property/CarPropertyManager
     */
    private lateinit var carPropertyManager: CarPropertyManager

    private var carPropertyListener = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(value: CarPropertyValue<Any>) {
            writeToTextView(value)
        }

        override fun onErrorEvent(propId: Int, zone: Int) {
            Log.w(TAG, "Received error car property event, propId=$propId")
        }

        fun writeToTextView(value: CarPropertyValue<Any>) {
            when (value.propertyId) {
                VehiclePropertyIds.GEAR_SELECTION -> {
                    binding.selectedGear.text =
                        VEHICLE_GEARS.getOrDefault(value.value as Int, GEAR_UNKNOWN)
                }
                VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE -> {
                    binding.outsideTemp.text = value.value.toString()
                }
                VehiclePropertyIds.NIGHT_MODE -> {
                    binding.nightModeSwitch.isChecked = value.value as Boolean
                }
                VehiclePropertyIds.PARKING_BRAKE_ON -> {
                    binding.parkingBrake.text = value.value.toString()
                }
            }
        }
    }

    // create binding var
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ledMode.text = ledMode

        // createCar() returns a "Car" object to access car service APIs. It can return null if
        // car service is not yet ready but that is not a common case and can happen on rare cases
        // (for example car service crashes) so the receiver should be ready for a null car object.
        //
        // Other variants of this API allows more control over car service functionality (such as
        // handling car service crashes graciously). Please see the SDK documentation for this.
        car = Car.createCar(this)
        carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager

        val getBrakeValue =
            carPropertyManager.getBooleanProperty(VehiclePropertyIds.PARKING_BRAKE_ON, 0)
        binding.parkingBrake.text = getBrakeValue.toString()

        // ################################
        // Some carPropertyManager Callbacks to act as examples
        carPropertyManager.registerCallback(
            carPropertyListener,
            VehiclePropertyIds.GEAR_SELECTION,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        carPropertyManager.registerCallback(
            carPropertyListener,
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        carPropertyManager.registerCallback(
            carPropertyListener,
            VehiclePropertyIds.NIGHT_MODE,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        carPropertyManager.registerCallback(
            carPropertyListener,
            VehiclePropertyIds.PARKING_BRAKE_ON,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )

        carPropertyManager.registerCallback(
            carPropertyListener,
            VehiclePropertyIds.HVAC_AC_ON,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )
        // ################################

        val piApi = ServiceBuilder.getInstance().create(PiApi::class.java)

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }


        // Creates a MQTT client
        val client = MqttClient("mqtt.eclipseprojects.io")
        // subscribes to the "drea" topic
        client.subscribe(
            "drea"
        ) { publish -> // Changes the numbers displayed on the lcd to the received parameter
            Log.i("drea", "Received payload: ${publish.payloadAsBytes.decodeToString()}")
            val rotation = publish.payloadAsBytes.decodeToString().split(",")[2]

            lifecycleScope.launch(Dispatchers.Default + coroutineExceptionHandler) {

                try {
                    val result = piApi.showTextOnLcd(LcdTask("Rotation: $rotation"))
                    if (result != null) {
                        val response = piApi.postLog(
                            PiLog(
                                "Display Rotation on Lcd",
                                "Changed Rotation to $rotation",
                                Category.Info
                            )
                        )
                        if (response != null) {
                            Log.i("drea", "New Log Entry: ${response.body()}")
                        }
                        // Checking the results
                        Log.i("drea", result.body().toString())
                    } else {
                        Log.d(TAG, "LEER")
                    }
                } catch (e: Error) {
                    Log.i(TAG, e.toString())
                }
            }
        }

        // Shows that the app can directly change properties of devices connected to the server
        // By clicking the button the led toggles its mode
        binding.switchLed.setOnClickListener {
            ledMode = if (ledMode == "Off") {
                "On"
            } else {
                "Off"
            }
            lifecycleScope.launch(Dispatchers.Default + coroutineExceptionHandler) {

                try {
                    val result = piApi.changeLedMode(LedTask(ledMode))
                    if (result != null) {
                        val response = piApi.postLog(
                            PiLog(
                                "Led Mode Change",
                                "Changed Led mode to $ledMode",
                                Category.Info
                            )
                        )
                        if (response != null) {
                            Log.i(TAG, response.body().toString())
                        }
                        // Checking the results
                        Log.d(TAG, result.body().toString())
                    } else {
                        Log.d(TAG, "LEER")
                    }
                } catch (e: Error) {
                    Log.i(TAG, e.toString())
                }
            }
            binding.ledMode.text = ledMode
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        car.disconnect()
    }
}