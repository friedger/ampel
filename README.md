
Ampel
===================================

This app results from a short hackathon at droidcon Berlin 2015 (6 June 2015).
It informs car drivers (who are checking their mobile phones) when a red traffic light switches to green.

Description
------------

This app is installed on an Android device in a car. During the hackathon we used the Panasonic Mirror.
In addition, the mirror is wirelessly connected to the car engine (a Lego EV3).

The app starts a foreground service that scans for Bluetooth LE devices.
These devices broadcast their state (red or green) using the Uribeacon format (beaconLayout). As we only had one RFduino and
there were many BLE devices around us we also filtered by mac address.
In the real implementation, a specific beacon Layout will be defined, that is used
for filtering.

When the car approaches a beacon the app informs the user about the state of the traffic light by showing
a large green or red icon is shown. For autonomous cars like we used, the app sends control commands to the Lego
brick to start or stop the engine motors.

Background
----------
We used the Arduino IDE to program the RFduino with a RGB/button shield. The state of the light is changed by pressing
one or the other button.

The app is based on the BluetoothLeGatt example that comes with Android Studio and the [HTTYR Android control app](https://lejosnews.wordpress.com/2014/05/02/httyr-android-control-app/)

Unfortunately, the mirror could not manage BLE and Wifi hotspot at the same time gracefully.

Pre-requisites
--------------

- RFDuino with RGB/button shield
- Lego mindstorm EV3
- Android device with Android 4.3
- Android SDK v22
- Android Build Tools v22.0.1
- Android Support Repository


Open Source Licenses
--------------------
We use open source software from
* The Andrdoid Open Source Project, Inc. under APL 2.0

License
-------

Copyright 2015 The Ampel Team

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
