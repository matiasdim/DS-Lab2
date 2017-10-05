# CIS655 - Lab 01 - Matías Gil Echavarría

### ComputePi:
It is just the Java RMi official demo adding into ComputePi.java class a functionality to input from console. Depending of this input it calculates Pi, or primes of a range or exits. 
The Primes computation is performed on the class Primes.java.

### Chat:
The project structure is:
* client package
  * Client.java contains:
    * All the registry with the presence service.
    * Listener socket initialization.
    * User registration against the Presence Service.
    * Console input buffer.
    * Processor of console commands as indicated on porject specs.
    * Functionality to exit the chat
    * Functionality to update status as busy/available
    * Functionality to list user on the chat
    * Functionality to send message to a particular user
    * Functionality to send a message to all the available users
    * Static functions for communication with the presence server (Registration, search user, update status, list users, remove user).
    * Aditionally the console prints has codification to print with colors and style a little bit the console.
  * TextListener.java:

* engine package

* server package
