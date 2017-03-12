#include <SoftwareSerial.h>
#include "HX711.h"

//Rx pin to receive data from Bluetooth shield
//Tx pin to transmit data to Bluetooth shield
#define RxD 2
#define TxD 3

//HX711 n1
//DOUT -> D4
//CLK -> D5
#define DOUT1 4
#define CLK1 5

//HX711 n2
//DOUT -> D6
//CLK -> D7
#define DOUT2 6
#define CLK2 7

//HX711 n3
//DOUT -> D8
//CLK -> D9
#define DOUT3 8
#define CLK3 9

//Defining HX711 amps
HX711 sensor1(DOUT1,CLK1);
HX711 sensor2(DOUT2,CLK2);
HX711 sensor3(DOUT3,CLK3);


//Empty int array for testing receiving 'A' char

const int zeroSensorValues[]={0,0,0,0};

// variables to hold the analog readings
int sensorReading1;
int sensorReading2;
int sensorReading3;


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
const int COMM_SPEED_USB = 9600;
const int COMM_SPEED_BLUETOOTH=9600;

// time stuff
long time_start = millis();
const int DELAY_MS = 100;

//Declaring the software serial to communicate with the bluetooth shield
SoftwareSerial bluetoothSerial(RxD,TxD);


// function to get sensor data
void getData(int *data) {
    // read the analog input from the sensors
    /*
    sensorReading1 = analogRead(sensorAnalog1);
    sensorReading2 = analogRead(sensorAnalog2);
    sensorReading3 = analogRead(sensorAnalog3);
    sensorReading4 = analogRead(sensorAnalog4);
    */
    sensorReading1=sensor1.get_units();
    sensorReading2=sensor2.get_units();
    sensorReading3=sensor3.get_units();

    // map the values to the set range
    sensorReading1 = map(sensorReading1, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);
    sensorReading2 = map(sensorReading2, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);
    sensorReading3 = map(sensorReading3, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);
    //sensorReading4 = map(sensorReading4, MIN_DEFAULT, MAX_DEFAULT, MIN_SET, MAX_SET);

    data[0] = sensorReading1;
    data[1] = sensorReading2;
    data[2] = sensorReading3;
    data[3] = 0;
}

// function to write data to Serial
void writeData(int *sensorData) {
  // put the values into a string
  String s = (String) sensorData[0]+";" + (String) sensorData[1] + ";" + (String) sensorData[2] + ";" + (String) sensorData[3];
  // send out the values
  Serial.write(CODE_BEGIN);
  bluetoothSerial.write(CODE_BEGIN);
  
      
  for (int i=0; i < s.length(); i++) {
    Serial.write(s.charAt(i));
    bluetoothSerial.write(s.charAt(i));
  }
      
  Serial.write(CODE_END);
  Serial.println();
  bluetoothSerial.write(CODE_END);
}



void setup(void) {
  pinMode(RxD,INPUT);
  pinMode(TxD,OUTPUT);
  Serial.begin(COMM_SPEED_USB); // We'll send debugging information via the Serial monitor
  bluetoothSerial.begin(COMM_SPEED_BLUETOOTH);

  Serial.println("Before setting up the scale:");
  Serial.print("read: \t\t");
  Serial.print(sensor1.read());      // print a raw reading from the ADC
  Serial.print(";");
  Serial.print(sensor2.read());      // print a raw reading from the ADC
  Serial.print(";");
  Serial.print(sensor3.read());      // print a raw reading from the ADC
  Serial.println(";");

  Serial.print("read average: \t\t");
  Serial.print(sensor1.read_average(20));      // print average of 20 raw reading from the ADC
  Serial.print(";");
  Serial.print(sensor2.read_average(20));      // print average raw reading from the ADC
  Serial.print(";");
  Serial.println(sensor3.read_average(20));      // print a raw reading from the ADC

  Serial.print("get value: \t\t");
  Serial.print(sensor1.get_value(5));   // print the average of 5 readings from the ADC minus the tare weight (not set yet)
  Serial.print(";");
  Serial.print(sensor2.get_value(5));
  Serial.print(";");
  Serial.print(sensor3.get_value(5));
  Serial.println(";");

  Serial.print("get units: \t\t");
  Serial.print(sensor1.get_units(5), 1);  // print the average of 5 readings from the ADC minus tare weight (not set) divided
  Serial.print(";");
  Serial.print(sensor2.get_units(5), 1);  // print the average of 5 readings from the ADC minus tare weight (not set) divided
  Serial.print(";");
  Serial.print(sensor3.get_units(5), 1);  // print the average of 5 readings from the ADC minus tare weight (not set) divided
  Serial.println(";");
            // by the SCALE parameter (not set yet)

  sensor1.set_scale(1240.f);                      // this value is obtained by calibrating the scale with known weights; see the README for details
  sensor2.set_scale(1240.f);
  sensor3.set_scale(1240.f);

  sensor1.tare();
  sensor2.tare();
  sensor3.tare();
}

void loop(void) {
  bool testing = false;

  // get the current time
  long time_now = millis();

  // get the time difference
  long dt = time_now - time_start;
  if (dt >= DELAY_MS) {
    // write the sensor data
    int sensorData[4];
    getData(sensorData);
    writeData(sensorData);
    // reset the timer after doing the work
    time_start = time_now;
  }
  
  // Is there any data to be read on USB serial port ?
  if (Serial.available() > 0) {
    int value=Serial.read();
    char valueChar=(char)value;
    if(valueChar=='A'){
      writeData(zeroSensorValues);
      sensor1.tare();
      sensor2.tare();
      sensor3.tare();
    }
    
  }
  //Is there any data to be read on Bluetooth Serial ?
  if(bluetoothSerial.available() >0){
    int value=bluetoothSerial.read();
    char valueChar=(char)value;
    if(valueChar=='A'){

      //If we receive an 'A' character, then send sensorValues of 0 to see on the visualization program the dot moving
      //This shows that we can receive values via Bluetooth or Serial
      writeData(zeroSensorValues);
    }
  }

}
