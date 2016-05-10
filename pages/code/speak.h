#include <Arduino.h>
#include <SoftwareSerial.h>

#define txPin  2
SoftwareSerial speakjet = SoftwareSerial(0, txPin);
char * phonemeDict[255];

void phonemeDictSetup()
{
  for (int i=0; i<255; i++)
  {
    phonemeDict[i]="";
  }
  phonemeDict[0]="P0";
  phonemeDict[1]="P1";
  phonemeDict[2]="P2";
  phonemeDict[3]="P3";
  phonemeDict[4]="P4";
  phonemeDict[5]="P5";
  phonemeDict[6]="P6";
  phonemeDict[7]="FAST";
  phonemeDict[8]="SLOW";
  phonemeDict[14]="STRESS";
  phonemeDict[15]="RELAX";
  phonemeDict[128]="IY";
  phonemeDict[129]="IH";
  phonemeDict[130]="EY";
  phonemeDict[131]="EH";
  phonemeDict[132]="AY";
  phonemeDict[133]="AX";
  phonemeDict[134]="UX";
  phonemeDict[135]="OH";
  phonemeDict[136]="AW";
  phonemeDict[137]="OW";
  phonemeDict[138]="UH";
  phonemeDict[139]="UW";
  phonemeDict[140]="MM";
  phonemeDict[141]="NE";
  phonemeDict[142]="NO";
  phonemeDict[143]="NGE";
  phonemeDict[144]="NGO";
  phonemeDict[145]="LE";
  phonemeDict[146]="LO";
  phonemeDict[147]="WW";
  phonemeDict[148]="RR";
  phonemeDict[149]="IYRR";
  phonemeDict[150]="EYRR";
  phonemeDict[151]="AXRR";
  phonemeDict[152]="AWRR";
  phonemeDict[153]="OWRR";
  phonemeDict[154]="EYIY";
  phonemeDict[155]="OHIY";
  phonemeDict[156]="OWIY";
  phonemeDict[157]="OHIH";
  phonemeDict[158]="IYEH";
  phonemeDict[159]="EHLE";
  phonemeDict[160]="IYUW";
  phonemeDict[161]="AXUW";
  phonemeDict[162]="IHWW";
  phonemeDict[163]="AYWW";
  phonemeDict[164]="OWWW";
  phonemeDict[165]="JH";
  phonemeDict[166]="VV";
  phonemeDict[167]="ZZ";
  phonemeDict[168]="ZH";
  phonemeDict[169]="DH";
  phonemeDict[170]="BE";
  phonemeDict[171]="BO";
  phonemeDict[172]="EB";
  phonemeDict[173]="OB";
  phonemeDict[174]="DE";
  phonemeDict[175]="DO";
  phonemeDict[176]="ED";
  phonemeDict[177]="OD";
  phonemeDict[178]="GE";
  phonemeDict[179]="GO";
  phonemeDict[180]="EG";
  phonemeDict[181]="OG";
  phonemeDict[182]="CH";
  phonemeDict[183]="HE";
  phonemeDict[184]="HO";
  phonemeDict[185]="WH";
  phonemeDict[186]="FF";
  phonemeDict[187]="SE";
  phonemeDict[188]="SO";
  phonemeDict[189]="SH";
  phonemeDict[190]="TH";
  phonemeDict[191]="TT";
  phonemeDict[192]="TU";
  phonemeDict[193]="TS";
  phonemeDict[194]="KE";
  phonemeDict[195]="KO";
  phonemeDict[196]="EK";
  phonemeDict[197]="OK";
  phonemeDict[198]="PE";
  phonemeDict[199]="PO";
  phonemeDict[200]="R0";
  phonemeDict[201]="R1";
  phonemeDict[202]="R2";
  phonemeDict[203]="R3";
  phonemeDict[204]="R4";
  phonemeDict[205]="R5";
  phonemeDict[206]="R6";
  phonemeDict[207]="R7";
  phonemeDict[208]="R8";
  phonemeDict[209]="R9";
  phonemeDict[210]="A0";
  phonemeDict[211]="A1";
  phonemeDict[212]="A2";
  phonemeDict[213]="A3";
  phonemeDict[214]="A4";
  phonemeDict[215]="A5";
  phonemeDict[216]="A6";
  phonemeDict[217]="A7";
  phonemeDict[218]="A8";
  phonemeDict[219]="A9";
  phonemeDict[220]="B0";
  phonemeDict[221]="B1";
  phonemeDict[222]="B2";
  phonemeDict[223]="B3";
  phonemeDict[224]="B4";
  phonemeDict[225]="B5";
  phonemeDict[226]="B6";
  phonemeDict[227]="B7";
  phonemeDict[228]="B8";
  phonemeDict[229]="B9";
  phonemeDict[230]="C0";
  phonemeDict[231]="C1";
  phonemeDict[232]="C2";
  phonemeDict[233]="C3";
  phonemeDict[234]="C4";
  phonemeDict[235]="C5";
  phonemeDict[236]="C6";
  phonemeDict[237]="C7";
  phonemeDict[238]="C8";
  phonemeDict[239]="C9";
  phonemeDict[240]="D0";
  phonemeDict[241]="D1";
  phonemeDict[242]="D2";
  phonemeDict[243]="D3";
  phonemeDict[244]="D4";
  phonemeDict[245]="D5";
  phonemeDict[246]="D6";
  phonemeDict[247]="D7";
  phonemeDict[248]="D8";
  phonemeDict[249]="D9";
  phonemeDict[250]="D10";
  phonemeDict[251]="D11";
  phonemeDict[252]="M0";
  phonemeDict[253]="M1";
  phonemeDict[254]="M2";
}

void speakjetSetup()  
{
  // Sets up voicebox module.  Uses pins 2-13
  // Requires <SoftwareSerial.h>
  // and the code:
  // #define txPin 1
  // SoftwareSerial speakjet = SoftwareSerial(0, txPin);

  //Configure the pins for the SpeakJet module
  #define RDY  13
  #define RES  3
  #define SPK  4

  pinMode(txPin, OUTPUT);
  pinMode(SPK, INPUT);
  
  //Set up a serial port to talk from Arduino to the SpeakJet module on pin 3.
  speakjet.begin(9600);    
  
  //Configure the Ready pin as an input
  pinMode(RDY, INPUT);
  
  //Configure Reset line as an output
  pinMode(RES, OUTPUT);
       
  //Configure all of the Event pins as outputs from Arduino, and set them Low.
  for(int i=5; i<=12; i++)
  {
    pinMode(i, OUTPUT);
    digitalWrite(i, LOW);
  }
  
  //All I/O pins are configured. Reset the SpeakJet module
  digitalWrite(RES, LOW);
  delay(100);
  digitalWrite(RES, HIGH);
  
  phonemeDictSetup();
}

byte lookup(char * phoneme)
{
  for (int i=0; i<255; i++)
  {
    if (String(phonemeDict[i]) == String(phoneme))
    {
      return i;  
    }
  }
  return 0;
}

void say(char * mystring)
{
  int length = strlen(mystring);
  byte out[length];
  char tmpstr[length];
  
  int counter = 0;
  int j = 0;
  for (int i=0; i<length; i++)
  {
    if (mystring[i]==' ')
    {
      tmpstr[j] = 0;
      out[counter++] = lookup(tmpstr);
      j = 0;
    }
    else
    {
      tmpstr[j++] = mystring[i];
    }
  }

  tmpstr[j] = 0;
  out[counter++] = lookup(tmpstr);
  out[counter] = 0;
  
  delay(300);
  speakjet.print((char *) out);
}

void sound(byte * mysound)
{
  speakjet.print((char *) mysound);
}
