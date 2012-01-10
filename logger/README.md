Ustvarjanje datoteke preko mreže
================================

Princip delovanja
-----------------

Za prenost podatkov se uporablja protokol UDP/IP, ki je enostavnejši od TCP/IP in za te namene dovolj dober.


Strežnik
--------
Na PC-ju teče strežnik (UDPServer), ki sprejema udp pakete po mreži in zapisjue vsebino v datoteko out.csv. 
Strežnik prevedeš in poženeš z:

    javac UDPServer.java
    java UDPServer

Testni klient
-------------

Delovanje strežnika UDPServer lahko stestiraš z UDPClient-om. Ta pošlje 1000 vrstis s trojkami nakjučno
generiranih decimalnih števil. Prevedš in poženeš z:

    javac UDPClient.java
    java  UDPClient

POZOR: prevajaš in poganjaš v novem oknu. Strežnik mora biti pognagn v svojem oknu.


Android
-------

V android projekt vključi class UDPSender. Najbolje da kar klikneš na paket com.marakana in izbereš New Class,
ga poimenuješ UDPSender in vanj prekopiraš vsebino datoteke UDPSender.java.

V razred CameraACtivity dodaj udpSender kot statični element (recimo pod WindowManager). Primer, kamor sem ga vključil jaz:

  ...
  private WindowManager mWindowManager;
  private UDPSender udpSender = new UDPSender("ip-tvojega-pcja", 9876);
  private Display mDisplay;
  ... 

Ko želiš vrstico s podatki poslati na strežnik, uporabiš:

    float x = ...
    float y = ...
    float z = ...
    udpSender.send(String.format("%f, %f, %f", x, y, z));

Ob vsakem klicu udpSender.send, se bo na PC-ju v datoteko out.csv zapisala vsebina vrstice (String.format("....", x,y,z)), ki si jo poslala.

Datoteko out.csv lahko odpreš v excelu.

