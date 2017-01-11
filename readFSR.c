int fsrAnalogPin = 0; // FSR is connected to analog 0
int fsrReading; // the analog reading from the FSR resistor divider

void setup(void) {
    Serial.begin(9600); // We'll send debugging information via the Serial monitor   
}

void loop(void) {
    // read the analog input from the sensor
    //
    // analogRead() reads input between 0 and 5V
    // and maps it to values between 0 and 1023
    fsrReading = analogRead(fsrAnalogPin);
    // print out the values
    Serial.print("Analog reading = ");
    Serial.println(fsrReading);

    delay (100);
}
