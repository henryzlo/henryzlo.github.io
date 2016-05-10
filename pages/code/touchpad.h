// include the atmel I2C libs
#include <Arduino.h>
#include "mpr121.h"
#include "i2c.h"

#define ONE 8
#define TWO 5
#define THREE 2
#define FOUR 7
#define FIVE 4
#define SIX 1
#define SEVEN 6
#define EIGHT 3
#define NINE 0

void touchpadSetup()
{
  // Sets up touch shield.
  // Number presses calls the speak function.
  // requires "mpr121.h" and "i2c.h"
  
  // Match key inputs with electrode numbers
  
  //output on ADC4 (PC4, SDA)
  DDRC |= 0b00010011;
  // Pull-ups on I2C Bus
  PORTC = 0b00110000; 
  // initalize I2C bus. Wiring lib not used. 
  i2cInit();
  
  delay(100);
  // initialize mpr121
  mpr121QuickConfig();
}

int getNumber()
{
  int touchNumber = 0;
  uint16_t touchstatus;
  
  touchstatus = mpr121Read(0x01) << 8;
  touchstatus |= mpr121Read(0x00);
  
  for (int j=0; j<12; j++)  // Check how many electrodes were pressed
  {
    if ((touchstatus & (1<<j)))
      touchNumber++;
  }
  if (touchNumber == 1)
  {
    if (touchstatus & (1<<SEVEN))
      return(7);
    else if (touchstatus & (1<<FOUR))
      return(4);
    else if (touchstatus & (1<<ONE))
      return(1);
    else if (touchstatus & (1<<EIGHT))
      return(8);
    else if (touchstatus & (1<<FIVE))
      return(5);
    else if (touchstatus & (1<<TWO))
      return(2);
    else if (touchstatus & (1<<NINE))
      return(9);
    else if (touchstatus & (1<<SIX))
      return(6);
    else if (touchstatus & (1<<THREE))
      return(3);
  }
  return(0);
}
