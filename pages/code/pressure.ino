#define LED1 2
#define LED2 3
#define LED3 4
#define LED4 5
#define PRESSURE 0

void setup()
{
  Serial.begin(9600);
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(LED4, OUTPUT);
}

void loop()
{
  float sum;
  float average;
  float pressure;

  for (int i = 0; i < 60 ; i++) {
    // Average to get more accurate readings                                    
    sum += analogRead(PRESSURE)/2;
    delay(5);
  }
  average = sum/60;
  pressure = 1/average;
  
  Serial.println(pressure);

  digitalWrite(LED1, LOW);
  digitalWrite(LED2, LOW);
  digitalWrite(LED3, LOW);
  digitalWrite(LED4, LOW);

  if(pressure >= 0.02) {
    digitalWrite(LED1, HIGH);
  } 
  if(pressure >= .1) {
    digitalWrite(LED2, HIGH);
  }
  if(pressure >= .5) {
    digitalWrite(LED3, HIGH);
  }
  if(pressure >= 1.1) {
    digitalWrite(LED4, HIGH);
  }

  delay(500);
}
