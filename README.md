# Praxisprojekt
## Abstract
In diesem Praxisprojekt wird die Frage bearbeitet, wie eine Grundlage zur Entwicklung von [Android Automotive](https://developers.google.com/cars/design/automotive-os) Apps,
die den [MultiKnob](https://dl.gi.de/handle/20.500.12116/39258) nutzen, geschaffen werden kann.

Das Ziel besteht darin, prototypisch zu zeigen, wie der MultiKnob in solche eine App eingearbeitet werden kann und welche Vorteile er bringt.
Um dies zu erreichen, wird auf einem Raspberry Pi ein Android Automotive Image laufen
([hier findet sich eine Anleitung](https://medium.com/snapp-automotive/android-automotive-os-11-usb-image-for-the-raspberry-pi-4b-34efe1119356)).
Dies dient dazu, den Entickler\*innen den Zugang zu einem physischen Automotive Gerät zu ermöglichen, ohne dass dafür die Anschaffung eines Fahrzeugs notwendig ist.

Ein weiterer Raspberry Pi bietet eine Schnittstelle, um Hardware des Autos wie z.B. die Fenster oder die Klimaanlage zu simulieren. 
Dafür werden die entsprechenden Komponente an diesem angeschlossen. Die App die auf dem Automotive Image laufen wird kann daraufhin diese Komponenten ansprechen und 
steuern.

Die bereits erwähnte App wird prototypisch darstellen, wie eine solche App auf die Fahrzeuginternen Systeme zugreifen kann und diese beeinflussen kann. 
Da diese Funktionalitäten relativ neu sind, können noch nicht alle Systeme angesprochen werden (daher besteht die Notwendigkeit zur Nutzung eines zweiten Raspberry Pi's,
welcher die fehlenden Funktionalitäten simuliert).
Die App zeigt außerdem, wie der MultiKnob Aufgaben wie z.B. das Verstellen der Lautstärke des Infotainmentsystems vereinfachen und somit die Ablenkung des
Fahrers minimieren kann. Dies ist eine direkte Implementierung der [Designprinzipien](https://developers.google.com/cars/design/design-foundations/interaction-principles#discourage_distraction).

Durch die Schaffung dieser Grundlage zur Entwicklung bieten sich viele Möglichkeiten der Weiterentwicklung. Darüber hinaus bleibt Raum für weitere Forschungenfragen
in diesem Bereich, z.B. ob der MultiKnob in Zusammenarbeit mit der Android Automotive App tatsächlich die Aufmerksamkeit des Fahrers verbessern kann, die in
weiteren wissenschaftlichen Ausarbeitungen beantwortet werden können.
