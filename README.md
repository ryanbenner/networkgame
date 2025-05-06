# networkgame

This repo contains programs to implement a multi-threaded TCP chat server and client

* MtClient.java handles keyboard input from the user.
* ClientListener.java receives responses from the server and displays them
* MtServer.java listens for client connections and creates a ClientHandler for each new client
* ClientHandler.java receives messages from a client and relays it to the other clients, it prevents duplicate usernames between clients. Who? prompts a printing of users connected to the server. 
* Client.java contains the socket and the client's username

## Source Files

* MtClient.java 
* ClientListener.java
* MtServer.java 
* ClientHandler.java 
* Client.java

## Build Insructions

Terminal 1:
* javac MtServer.java

Terminal 2:
* javac MtClient.java

Terminal 3:
* javac MtClient.java

## Execution Instructions

Once compiled, execute the server and two clients as follows. Each client will be prompted to enter a username, and then is able to communicate with other clients connected to the server.

Terminal 1:
* java MtServer.java

Terminal 2:
* java MtClient.java

Terminal 3:
* java MtClient.java
