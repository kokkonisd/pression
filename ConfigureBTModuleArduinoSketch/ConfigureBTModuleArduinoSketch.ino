/*This sketch is designed to allow programming the Bluetooth module via AT commands,
 * You can send AT commands via Serial monitor with No end line and AT+command text
 */



#include <SoftwareSerial.h>

#define RxD 2
#define TxD 3

SoftwareSerial BTSerie=SoftwareSerial(RxD,TxD);

void setup() {
  pinMode(RxD,INPUT);
  pinMode(TxD,OUTPUT);
  Serial.begin(9600);
  BTSerie.begin(9600);


}

void loop() {
  char recvChar;  
  //On lit caractere par caractere sur le BTSerie et on affiche sur le Terminal Serie  
  if (BTSerie.available()) {  
    recvChar = BTSerie.read();  
    Serial.print(recvChar);  
  }  
  // Serial.write(blueToothSerial.read());  
  if (Serial.available()) {  
    recvChar = Serial.read();  
    BTSerie.write(recvChar);  
  }  

}
