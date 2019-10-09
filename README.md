# Java VoIP
A very simple UDP VoIP system implemented in Java.

## Compilation
Java VoIP makes use of `Maven` for compilation and dependency handling. Compilation can be carried out as follows
```shell
mvn compile
```

The package can be tested using
```shell
mvn test
```

## Usage
Using Java VoIP is incredibly simple! To start with, create a new UDPVoIP object
```java
// Create the VoIP object
UDPVoIP voip = new UDPVoIP("address to call", "our address");

// Start the call
voip.start();

// Do your own thing here

// And end the call
voip.stop();
```

## Dependencies
Java VoIP is built using only classes in a default Java installation, and so the only dependencies are Java 1.3+.

## Contributing
Feel free to submit an issue or a pull request if you can think of anything! This repository is mainly a place to store a few classes I wrote during an assignment, but if it is of use to you, by all means, use it.
