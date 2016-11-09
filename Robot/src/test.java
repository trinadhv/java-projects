
import ch.ntb.usb.LibusbJava;
import ch.ntb.usb.Usb_Bus;
import ch.ntb.usb.Utils;

/**
 * Initalises Libusb and prints the bus(ses) with attached devices to the
 * standard out.<br>
 * 
 * 
 * @author schlaepfer
 * 
 */
public class test {

  private static void logBus() {
    // if you don't use the ch.ntb.usb.Device class you must initialise
    // Libusb before use
    LibusbJava.usb_init();
    LibusbJava.usb_find_busses();
    LibusbJava.usb_find_devices();

    // retrieve a object tree representing the bus with its devices and
    // descriptors
    Usb_Bus bus = LibusbJava.usb_get_busses();

    // log the bus structure to standard out
    Utils.logBus(bus);
  }

  public static void main(String[] args) {
    logBus();
  }
}