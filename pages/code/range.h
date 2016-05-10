#include <Arduino.h>

float getRange(int pin) {
  // Returns distance to target in inches.
  // Reads from pin number "pin"
  pinMode(pin, INPUT);

  float sum = 0;
  for(int i = 0; i < 60 ; i++)
  {
    // Average to get more accurate readings
    sum += analogRead(pin)/2;;
    delay(10);
  }  

  return(sum/60);
}
