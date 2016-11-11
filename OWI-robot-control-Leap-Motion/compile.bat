@echo off
echo Compiling %* with LibusbJava...

javac -cp "c:\LibUsbJava\ch.ntb.usb-0.5.9.jar;." %*

echo Finished.
