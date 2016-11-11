
NUI Chapter 6. Controlling a Robot Arm

From the website:

  Killer Game Programming in Java
  http://fivedots.coe.psu.ac.th/~ad/jg

  Dr. Andrew Davison
  Dept. of Computer Engineering
  Prince of Songkla University
  Hat yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th


If you use this code, please mention my name, and include a link
to the website.

Thanks,
  Andrew

============================

This directory contains 5 Java files:
  * RobotArm.java, Joint.java, ArmCommunicator.java,
    Coord3D.java, JointID.java


There 4 arm configuration files, used by the application:
  * baseJI.txt, shoulderJI.txt, 
    elbowJI.txt, wristJI.txt


There are 2 batch files:
  * compile.bat
  * run.bat
     - make sure they refer to the correct locations for your
       download of libusbjava


There are 2 PNG files
  * sponge.png, workarea.png
      - these are not part of the application;
        I've included them to show what my robot work area looks like;
        see below for details

----------------------------
Before Compilation/Execution:

You need to download and install:

   * libusb-win32: http://sourceforge.net/apps/trac/libusb-win32/wiki
             I downloaded libusb-win32-bin-1.2.4.0.zip
             --> The executable installs libusb-win32 into c:\Program Files\LibUSB-Win32\
             --> copy and rename the relevant DLLs in bin\ over to the OS
                   e.g. libusb0_x86.dll --> Windows\system32\libusb0.dll 
                        libusb0.sys --> Windows\systems32\drivers\libusb0.sys

   * libusbjava: http://libusbjava.sourceforge.net/wp/
             I downloaded ch.ntb.usb-0.5.9.jar and LibusbJava_dll_0.2.4.0.zip
             --> move the JAR and unzipped DLL to c:\LibUsbJava

----------------------------
Other requirements:

  * obtain a OWI-535 robot arm 
   (http://www.owirobot.com/products/Robotic-Arm-Edge.html), 
    **and** its USB interface.
      ^^^
    I obtained mine from Maplin Electronics
       http://www.maplin.co.uk/robotic-arm-kit-with-usb-pc-interface-266257

    Most online electronic hobbyist sites stock the bundle
    e.g. Planet Robotic 
       http://www.planetrobotic.com/content/
                         robotic-arm-edge-bundle-owi-535-kit-and-usb-interface

  * The driver software may not include a Windows 7 version, but the 
    OWI Robotics website has drivers at 
      http://www.owirobot.com/pages/Downloads.html

  * install the arm's ELAN USB driver

  * Create a libusb-win32 device driver for the ELAN driver using 
    libusb-win32's inf-wizard.exe


  * Obtain the USB analysis tools:
       - USBDeview (free from http://www.nirsoft.net/utils/usb_devices_view.html) 
       - SnoopyPro (free from http://sourceforge.net/projects/usbsnoop)
      
----------------------------
Setting up the Robot's Work Area

1. You'll need something for the robot to grab. I used a bit of sponge
(see sponge.png) cut out of a washpad for cleaning dishes. Make it
about 7-8cm high, with a narrow top but wider base.

2. To check robot movements, you'll need a large sheet of graph paper
marked in centimeters (see workarea.png). The base of the arm,
directly below the shoulder gear, should be placed at (0,0).

Right of the base is the +x axis, left is -x, and forwards is the +y axis

I draw the graph paper myself: a grid at 5cm intervals.

----------------------------
Compilation:

> compile *.java
    // you must have libusb-win32, libusbjava installed

----------------------------
Execution:

Before you start:
    - you must have libusb-win32, libusbjava installed;
    - you must have installed a libusb driver for the robot arm;
    - you must plug in the robot arm 


> run ArmCommunicator
    - use this to move the arm using keyboard commands
    - type ? <ENTER> to get a list of the commands


> run RobotArm
    - the main() method contains an example of moving to a location, grabbing,
      moving again, then releasing; modify this code to try other arm moves

----------------------------
Last updated: 10th July 2011
