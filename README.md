# pression
### Projet PeiP2
---

An arduino project about correcting one's sitting posture. 
It uses 50kg load sensors placed under the legs of a chair coupled with HX711 amplifiers to retrieve data from the load sensors.
This data will then be read by an Arduino and sent via serial communication and/or Bluetooth to a Java desktop app (or an Android app) which determines the quality of the posture based on the values of the load sensors.


---

Force-voltage equation:

Vo = Vcc ( R / (R + FSR) )



