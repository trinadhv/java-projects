@echo off
echo Executing %* with LibusbJava...

java -cp "c:\LibUsbJava\ch.ntb.usb-0.5.9.jar;." -Djava.library.path="c:\LibUsbJava;." %*

echo Finished.
