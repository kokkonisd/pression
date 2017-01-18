// pression - Projet PeiP2
// script to read the values of four load sensors and
// send them to the computer through serial comm

// the 4 sensors are connected to analog ports 0 - 3
int sensorAnalog1 = 0; // port A0
int sensorAnalog2 = 1; // port A1
int sensorAnalog3 = 2; // port A2
int sensorAnalog4 = 3; // port A3

// variables to hold the analog readings
int sensorReading1;
int sensorReading2;
int sensorReading3;
int sensorReading4;

// constants
// the first two store the default range of the load sensors
// the last two store a set interval of values
int MIN_DEFAULT = 0;
int MAX_DEFAULT = 1023;
int MIN_SET = 0;
int MAX_SET = 5000;

void setup(void) {
    Serial.begin(9600); // We'll send debugging information via the Serial monitor   
}

void loop(void) {
    // read the analog input from the sensors
    sensorReading1 = analogRead(sensorAnalog1);
    sensorReading2 = analogRead(sensorAnalog2);
    sensorReading3 = analogRead(sensorAnalog3);
    sensorReading4 = analogRead(sensorAnalog4);

    // map the values to the set range
    sensorReading1 = map(sensorReading1, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);
    sensorReading2 = map(sensorReading2, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);
    sensorReading3 = map(sensorReading3, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);
    sensorReading4 = map(sensorReading4, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);

    String s=((String)sensorReading1+";" + (String)sensorReading2 + ";" + (String)sensorReading3 + ";" + (String)sensorReading4);
    //char *values=strcat(values,sensorReading1);
    // send out the values
    Serial.write(2);
    for(int i=0;i<s.length();i++){
      Serial.write((int)s.charAt(i));
    }
    Serial.write(13);

    

    delay (100);
}
