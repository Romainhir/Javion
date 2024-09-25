# Javions
## Introduction
Javions is a project developed to facilitate air traffic control by decoding and displaying real-time ADS-B (Automatic Dependent Surveillance-Broadcast) messages transmitted by aircraft and other aerial vehicles like helicopters or balloons. ADS-B messages provide critical information such as identity, position, velocity, and flight direction of the transmitting aircraft.

## Project Overview
This project involves writing a program that decodes ADS-B messages received via a software-defined radio (SDR) and displays the detected aircraft on a map. The AirSpy R2 SDR is used to capture these messages, which are transmitted on a frequency of 1090 MHz. The system provides real-time tracking of aircraft in the vicinity of the receiver, based on the quality of reception and environmental conditions.

### The main functionalities of the project include:

Capturing ADS-B messages using an SDR.
Decoding the messages to extract information such as the aircraft's identity, position, and speed.
Displaying the aircraft on a graphical map interface in real-time.
## System Requirements
Software: The project was developed in Java.

Hardware: The AirSpy R2 software-defined radio and an appropriate antenna are required to receive ADS-B messages.

Operating Environment: The system works best when the antenna has a clear line of sight to the sky, ideally placed on a rooftop or elevated surface.
## How It Works
Signal Reception: The AirSpy R2 SDR captures radio signals transmitted by aircraft on the 1090 MHz frequency.

Signal Processing: The SDR digitizes the received signal and sends it to the computer.

Message Decoding: The program decodes the digital signals into ADS-B messages.

Visualization: Aircraft positions are plotted on a map, providing real-time air traffic visualization for the Lausanne area and its surroundings.
## Limitations
Due to the curvature of the Earth, the reception range is limited to a few hundred kilometers. To track aircraft over larger distances, it would be necessary to integrate data from multiple SDRs spread across a wider geographic area. However, this project focuses on processing data from a single SDR without interfacing with external ADS-B data sources.

## Future Improvements
#### Possible future extensions of the project could include:

Connecting with online ADS-B aggregators like ADSBHub or flightradar24 for global tracking.
Optimizing the graphical interface for smoother visualization.
Enhancing the decoding algorithm for better accuracy and range.
#### Acknowledgments
This project was developed during our first year at EPFL and involved utilizing a software-defined radio for signal reception and real-time aircraft tracking.
