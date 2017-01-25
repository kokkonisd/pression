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

// --constants--
// constants to set the default min and max values of the sensors
const int MIN_DEFAULT = 0;
const int MAX_DEFAULT = 1023;

// constants to set a range to which to map the sensor values
const int MIN_SET = 0;
const int MAX_SET = 5000;

// serial comm codes
const int CODE_BEGIN = 2;
const int CODE_END = 13;

// serial comm speed
const int COMM_SPEED = 9600;

// time stuff
long time_start = millis();
const int DELAY_MS = 100;

// function to get sensor data
int *getData(void) {
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

    int data[] = {sensorReading1, sensorReading2, sensorReading3, sensorReading4};

    return data;
}

// function to write data to Serial
void writeData(void) {
  // get the sensor data
  int *sensorData = getData(); 
  
  // put the values into a string
  String s = (String) sensorData[0]+";" + (String) sensorData[1] + ";" + (String) sensorData[2] + ";" + (String) sensorData[3];
  // send out the values
  Serial.write(CODE_BEGIN);
      
  for (int i=0; i < s.length(); i++) {
    Serial.write(s.charAt(i));
  }
      
  Serial.write(CODE_END);
}

void setup(void) {
    Serial.begin(COMM_SPEED); // We'll send debugging information via the Serial monitor
}


void loop(void) {
  // set to true when testing without the sensors
  bool testing = true;

  // if bluetooth works or if we're testing
  if (Serial.available() > 0 || testing) {
    // get the current time
    long time_now = millis();

    // get the time difference
    long dt = time_now - time_start;
    if (dt >= DELAY_MS) {
      // reset the timer
      time_start = time_now;
      // write the sensor data
      writeData();
    }
  }
}

