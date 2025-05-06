import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * MTClient.java
 * 
 * This program implements a simple multithreaded chat client.  It connects to the
 * server (assumed to be localhost on port 7654) and starts two threads:
 * one for listening for data sent from the server, and another that waits
 * for the user to type something in that will be sent to the server.
 * Anything sent to the server is broadcast to all clients.
 * 
 * The MTClient uses a ClientListener whose code is in a separate file.
 * The ClientListener runs in a separate thread, recieves messages form the server,
 * and displays them on the screen.
 *  
 * Data received is sent to the output screen, so it is possible that as
 * a user is typing in information a message from the server will be
 * inserted.
 */

public class MtClient {
  public static void main(String[] args) {
    try {
      String hostname = "localhost";
      int port = 9014;  // Updated port to 9014 to match the server.

      System.out.println("Connecting to server on port " + port);
      Socket connectionSock = new Socket(hostname, port);

      DataOutputStream serverOutput = new DataOutputStream(connectionSock.getOutputStream());

      System.out.println("Connection made.");

      // Start a thread to listen for and display data sent from the server.
      ClientListener listener = new ClientListener(connectionSock);
      Thread theThread = new Thread(listener);
      theThread.start();

      // Use a scanner for user input to send messages continually.
      Scanner keyboard = new Scanner(System.in);
      while (true) {
        String data = keyboard.nextLine();
        serverOutput.writeBytes(data + "\n");
        if (data.equals("Goodbye")) {
          System.out.println("Exiting chat.");
          keyboard.close();
          connectionSock.close();
          System.exit(0);
        }
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
