import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ClientListener.java
 *
 * <p>This class runs on the client end and just
 * displays any text received from the server.
 *
 */

public class ClientListener implements Runnable {
  private Socket connectionSock = null;

  public ClientListener(Socket sock) {
    this.connectionSock = sock;
  }

  public void run() {
    try {
      BufferedReader serverInput = new BufferedReader(
          new InputStreamReader(connectionSock.getInputStream()));
      String serverText;
      while ((serverText = serverInput.readLine()) != null) {
        System.out.println(serverText);
      }
      System.out.println("Closing connection for socket " + connectionSock);
      connectionSock.close();
    } catch (IOException e) {
    }
  }
}
