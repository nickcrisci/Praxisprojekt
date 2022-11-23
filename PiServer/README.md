# Hardware Simulator
Diese Komponente ist dafür gedacht, die Hardware eines Fahrzeuges, bzw. einzelne Teile, simulieren zu können. Um dies erreichen wird ein Raspberry Pi verwendet, welcher mithilfe eines Python Flask Servers eine Rest Schnittstelle anbietet.
Über diese Schnittstelle können die angeschlossenen Sensoren und Aktoren angesteuert werden. 

## Aufbau des Hardware Simulators
Um den Hardware Simulator zu implementieren wurde ein **Raspberry Pi 4 4GB** verwendet. Auf diesen wurde ein **GrovePi+** aufgesetzt, der es ermöglicht, kompatible Sensoren und Aktoren an den Raspberry Pi anzuschließen und diese mithilfe der Programmiersprache Python zu steuern.
Für die Umsetzung der Schnittstelle wurde ein Flask Server aufgesetzt. Dieser bietet verschiedene Routen an um die angeschlossene Hardware zu steuern. Wird weitere Hardware an den Raspberry Pi angeschlossen können problemlos weitere Routen hinzugefügt werden.

<img src="https://user-images.githubusercontent.com/45553336/203639606-a3b0077a-c1ce-448a-a52e-6ae9eb2a193a.jpg"  width="500">

## Aufbau des Servers
Die Server Dateien befinden sich im [`./flaskr/`](https://github.com/nickcrisci/Praxisprojekt/tree/main/PiServer/flaskr) Ordner. Unter [`./flaskr/hardware/`](https://github.com/nickcrisci/Praxisprojekt/tree/main/PiServer/flaskr/hardware) befindet sich der Code zur Steuerung der einzelnen Aktoren/Sensoren.
Ein kurzes Beispiel:
```py
def changeLedMode(mode):
    try:
        digitalWrite(led, ledMode)
    except IOError:
        return { "success": False }
    
    return { "success": True }
```

In der [`./flaskr/sim.py`](https://github.com/nickcrisci/Praxisprojekt/tree/main/PiServer/flaskr/sim.py) befinden sich die Routen zum Ansprechen der Hardware. Dort werden dann die oben beschriebenen Funktionen verwendet:
```py
@bp.route("/led", methods=["POST"])
def changeLedMode():
    content_type = request.headers.get('Content-Type')
    if (content_type == 'application/json'):
        json = request.get_json()
        return jsonify(led.changeLedMode(json["mode"]))
    else:
        return { "error": "Content-Type not supported" }
```

Unter [`./flaskr/schema.sql`](https://github.com/nickcrisci/Praxisprojekt/tree/main/PiServer/flaskr/schema.sql) befindet sich ein Schema für Log Nachrichten. Die Log Nachrichten enthalten dabei folgende Informationen:
- `id` - Eine eindeutige Id
- `category` - Die Kategorie der Log Nachricht ('Info', 'Error', 'Debug', 'Warning', 'Other')
- `title` - Der Titel der Log Nachricht
- `body` - Der Mengentext einer Log Nachricht
- `created` - Das Datum an dem die Log Nachricht erzeugt wurde

## Starten des Servers
<!-- Hier beschreiben wie benötigte Pakete installiert werden und Venv aufgesetzt wird --->
Um den Server starten zu können muss zuerst die Datenbank aufgesetzt werden. Hierzu reicht es den folgenden Befehl einzugeben:

`flask --app flaskr setup-db` 

Nachdem die Datenbank initialisiert wurde kann der Server durch `flask --app flaskr run` gestartet werden.
Damit der Server auch von Außen erreichbar ist muss die Option `host=0.0.0.0` hinzugefügt werden:


`flask --app flaskr run --host=0.0.0.0` 

Der Server sollte nun unter `http://127.0.0.1:5000` erreichbar sein.

## Routen
| Route          | HTTP Verb | Beschreibung                            |
|----------------|-----------|-----------------------------------------|
| /sim/led       | POST      | LED an- bzw. ausschalten                |
| /sim/lcd       | POST      | Text auf dem LCD Bildschirm anzeigen    |
| /log           | POST      | Log Event erzeugen                      |
| /log           | GET       | Übersicht von geloggten Events anzeigen |
| /log/info/<id> | GET       | Abrufen eines bestimmten Log Events     |
