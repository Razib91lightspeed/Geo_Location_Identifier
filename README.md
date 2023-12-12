# Mobile Finder App

## Overview

The Mobile Finder app is an Android application that integrates location services and QR code scanning to provide information about scanned locations and calculate the distance from the current device location.

## Features

- QR Code Scanning
- Location Information Display
- Distance Calculation
- Address Geocoding

## Prerequisites

Before running the app, make sure you have the following:

- Android Studio installed
- Android device or emulator for testing
- Permissions for camera and location services

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/Razib91lightspeed/Mobile_Finder.git
2. Open the project in Android Studio.
3. Build and run the app on your Android device or emulator.

## Usage
- Scan a QR code to view location information.
- See the scanned location's address, coordinates, and distance from your current location.

## Components

### MainActivity
The main activity of the app containing UI elements and handling QR code scanning, location permission checks, and user interface updates.

### LocationHandler
A utility class for handling location-related operations, such as retrieving the last known location of the device.

## Permissions
Ensure the app has the necessary permissions:

- Camera: For QR code scanning
- Location: For accessing device location

## Troubleshooting
If you encounter any issues or have questions, please create an issue.

### License
This project is licensed under the MIT License.
