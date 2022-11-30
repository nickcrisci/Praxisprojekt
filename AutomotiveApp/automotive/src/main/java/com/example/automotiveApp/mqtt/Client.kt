package com.example.automotiveApp.mqtt

import android.util.Log
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import java.util.*

// Package for creating a MQTT client by using the HiveMQ MQTT client library
class MqttClient(val host: String) {
    var connected = false

    val client = Mqtt5Client.builder()
        .identifier(UUID.randomUUID().toString())
        .serverHost(host)
        .buildAsync()

    private fun _subscribe(topic: String, callback: (Mqtt5Publish) -> Unit) {
        client.subscribeWith()
            .topicFilter(topic)
            .retainAsPublished(true)
            .callback(callback)
            .send()
    }


    fun subscribe(topic: String, callback: (Mqtt5Publish) -> Unit) {
        if (!connected) {
            Log.e("MQTT", host)
            val ackMsg = client.connect()
            ackMsg.whenComplete { mqtt5ConnAck, throwable ->
                if(throwable != null) {
                    Log.e("MQTT", throwable.toString())
                } else {
                    connected = true
                    _subscribe(topic, callback)
                }
            }
        } else {
            _subscribe(topic, callback)
        }
    }
}

