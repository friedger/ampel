// Physical-Web example for RFDigital RFduino

#include <RFduinoBLE.h>

uint8_t advdata[] =
{
  0x03,  // length
  0x03,  // Param: Service List
  0xD8, 0xFE,  // URI Beacon ID
  0x06,  // length
  0x16,  // Service Data
  0xD8, 0xFE, // URI Beacon ID
  0x00,  // flags
  0xEE,  // power
  0x00,  
};

// pins
// pin 2 on the RGB shield is the red led
int led1 = 2;
// pin 3 on the RGB shield is the green led
int led2 = 3;

int button1 = 5;
int button2 = 6;

int green = 0;
int red = 1;

// debounce time (in ms)
int debounce_time = 10;

// maximum debounce timeout (in ms)
int debounce_timeout = 100;


void setup() {
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(button1, INPUT);
  pinMode(button2, INPUT);
  startAdvert(green);    
}

void loop() {
  delay_until_button(button1, HIGH);

  delay_until_button(button2, HIGH);
}


int debounce(int button, int state)
{
  int start = millis();
  int debounce_start = start;
  
  while (millis() - start < debounce_timeout)
    if (digitalRead(button) == state)
    {
      if (millis() - debounce_start >= debounce_time)
        return 1;
    }
    else
      debounce_start = millis();

  return 0;
}

int delay_until_button(int button, int state)
{
  // set button edge to wake up on
  if (state)
    RFduino_pinWake(button, HIGH);
  else
    RFduino_pinWake(button, LOW);
    
  do
    // switch to lower power mode until a button edge wakes us up
    RFduino_ULPDelay(INFINITE);
  while (! debounce(button, state));
  
  // if multiple buttons were configured, this is how you would determine what woke you up
  if (RFduino_pinWoke(button))
  {
      RFduinoBLE.end();
    if (button == button1) {
      startAdvert(red);
    } else {
      startAdvert(green);
    }
    // execute code here
    RFduino_resetPinWake(button);
  }
}

void startAdvert(int lightState) {
   digitalWrite(led1, LOW);
   digitalWrite(led2, LOW);
    
  if (lightState == red) {
    digitalWrite(led1, HIGH);
  } else {
    digitalWrite(led2, HIGH);
  }
  advdata[10] = lightState;
  RFduinoBLE_advdata = advdata;
  RFduinoBLE_advdata_len = sizeof(advdata);
  RFduinoBLE.advertisementInterval = 1000; // advertise every 1000ms
  RFduinoBLE.begin();
}
  
void RFduinoBLE_onAdvertisement(bool start)
{
  // turn the green led on if we start advertisement, and turn it
  // off if we stop advertisement
  
  if (!start)
  {
    digitalWrite(led1, LOW);
    digitalWrite(led2, LOW);
  }
}

