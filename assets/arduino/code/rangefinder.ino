#include "ssd.h"
#include "range.h"

//
// This demo gets the distance in decimeters, and prints it to 
// a seven segment display.
//
// Requires digital pins 2-9 to be in LED
// pin 0 to be in rangefinder

void setup() {
  // This opens up a serial connection to shoot the results back to the PC console
  Serial.begin(9600);

  // Setup for 7-segment display.
  setupSSD();
}

void loop() {
  // Shows numbers from 0-9
  int output = getRange(0);
  Serial.println(output);
  showNumber(output);
  delay(1000);
}
