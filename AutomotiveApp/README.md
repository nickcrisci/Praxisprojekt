# Automotive App
Diese Komponente stellt eine Android Automotive Anwendung dar, welche auf dem AAOS Image ausgeführt werden kann, und mit dem DREA kommuniziert. 

## Kommunikation
Zur Kommunikation mit dem PiServer wird Retrofit verwendet, um die von dem Server zur Verfügung gestellte Api nutzen zu können.
Der entsprechende Code findet sich [hier](https://github.com/nickcrisci/Praxisprojekt/blob/c3bf0717c346f5088cdb7d4484165f2a4a0f9d63/AutomotiveApp/automotive/src/main/java/com/example/automotiveApp/retro/). 
Um die App später starten zu können, bzw. die Verbindung zum Server herstellen zu können, muss in der [ServiceBuilder.kt](https://github.com/nickcrisci/Praxisprojekt/blob/c3bf0717c346f5088cdb7d4484165f2a4a0f9d63/AutomotiveApp/automotive/src/main/java/com/example/automotiveApp/retro/ServiceBuilder.kt) Datei die IP Adresse auf die IP Adresse des Geräts, auf dem der Server läuft geändert werden. 

Zur Kommunikation mit dem DREA wird die HiveMQ Library genutzt. Der entsprechende Code findet sich [hier](https://github.com/nickcrisci/Praxisprojekt/tree/c3bf0717c346f5088cdb7d4484165f2a4a0f9d63/AutomotiveApp/automotive/src/main/java/com/example/automotiveApp/mqtt). 
Um einen Client zu erzeugen wird folgender Code genutzt:
```kt
val client = MqttClient(host: String)
client.subscribe(topic: String, callback: (Mqtt5Publish) -> Unit)
```
Es muss eine Host Adresse übergeben werden, in diesem Projekt wird der MQTT Broker von Eclipse verwendet: "mqtt.eclipseprojects.io"
Zum Subscriben eines Topics wird die client.subscribe Funktion aufgerufen. Dabei wird das entsprechende Topic übergeben, und ein Callback welcher die erhaltenen Daten verarbeitet.

In diesem Projekt sieht das dann folgendermaßen aus:
```kt
val client = MqttClient("mqtt.eclipseprojects.io")
client.subscribe("drea") { publish ->
  Log.i("drea", "Received payload: ${publish.payloadAsBytes.decodeToString()}")
}
```

## Ausführen auf der Hardware Lösung
Um die App auf der Hardware Lösung ausführen zu können, muss mithilfe von der Android Debug Bridge (ADB) eine Verbindung zum Gerät hergestellt werden.
Dafür muss zuerst das WIFI-Debugging auf dem Gerät aktiviert werden.
Danach wird `adb connect <ip-des-geräts>` verwendet, um die Verbindung herzustellen. Daraufhin sollte das Gerät zum Ausführen der App zur Verfügung stehen.
