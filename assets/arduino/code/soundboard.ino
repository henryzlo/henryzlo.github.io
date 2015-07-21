#include "touchpad.h"
#include "speak.h"
#include <SoftwareSerial.h>

void setup()
{
  touchpadSetup();
  speakjetSetup();
}

void loop()
{
  int number = 0;
  while(number==0)
    number = getNumber();

  switch (number) {
    case 1:
      // Hello
      say("HE FAST EHLE LO OWWW");
      break;
    case 2:
      // I can speak
      say("OHIH P4 KE SLOW AY NE P4 SE PE SLOW IY EK");
      break;
    case 3:
      // I love you
      say("OHIH P4 LO SLOW UX SLOW VV P4 SLOW IYUW");
      break;
    case 4:
      // JK LOL
      say("JH EYIY P4 KE EYIY P3 EH EHLE P1 OW OWWW P1 EH EHLE");
      break;
    case 5:
      // Stop touching me
      say("SE TT SLOW OH PO SLOW TT SLOW UX CH IH NGE MM IY IY");
      break;
    case 6:
      // Program me
      say("PE RR OW GE FAST RR AY MM P4 MM IY IY");
      break;
    case 7:
      // Harder better faster stronger
      say("HE AW RR FAST DE AXRR BE SLOW EH FAST DE EYRR");
      break;
    case 8:
      say("FF AY SE TT RR SE TT FAST RR OH FAST NGE EYRR");
      break;
    case 9:
      byte ping[] = {20, 96, 21, 114, 22, 88, 23, 5, 252};
      sound(ping);
      break;
    }
  delay(1000);
}
