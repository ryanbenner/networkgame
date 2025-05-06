import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * MTServer.java
 *
 * <p>This program implements a simple multithreaded chat server.  Every client that
 * connects to the server can broadcast data to all other clients.
 * The server stores an ArrayList of sockets to perform the broadcast.
 *
 * <p>The MTServer uses a ClientHandler whose code is in a separate file.
 * When a client connects, the MTServer starts a ClientHandler in a separate thread
 * to receive messages from the client.
 *
 * <p>To test, start the server first, then start multiple clients and type messages
 * in the client windows.
 *
 */

public class MtServer {
  // Maintain a list of client objects (each with its Socket and username)
  private ArrayList<Client> clientList;

  public MtServer() {
    clientList = new ArrayList<Client>();
  }

  private void getConnection() {
    try {
      // Changed the port to 9014 to match your sample.
      int port = 9014;
      System.out.println("Waiting for client connections on port " + port + ".");
      ServerSocket serverSock = new ServerSocket(port);
      while (true) {
        Socket connectionSock = serverSock.accept();
        // Hand over the new connection to the ClientHandler.
        Client newClient = new Client(connectionSock, "");
        ClientHandler handler = new ClientHandler(newClient, this.clientList);
        Thread theThread = new Thread(handler);
        theThread.start();
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    MtServer server = new MtServer();
    server.getConnection();
  }
}
