import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ClientHandler.java
 *
 * <p>This class handles communication between the client
 * and the server. It runs in a separate thread but has a
 * link to a common list of sockets to handle broadcast.
 */
public class ClientHandler implements Runnable {
  private Client myClient = null;
  private ArrayList<Client> clientList;

  public ClientHandler(Client client, ArrayList<Client> clientList) {
    this.myClient = client;
    this.clientList = clientList;
  }

  /**
   * Runs the client handler.
   *
   * <p>This method handles communications between a client
   * and the server. It receives messages from the client, adds the client
   * to the list of clients, and broadcasts messages to the other clients.
   */
  public void run() {
    try {
        DataOutputStream out = new DataOutputStream(myClient.connectionSock.getOutputStream());
        BufferedReader in = new BufferedReader(
            new InputStreamReader(myClient.connectionSock.getInputStream()));

        // 1) Username negotiation
        out.writeBytes("Enter your username:\n");
        String name;
        while (true) {
            name = in.readLine();
            synchronized (clientList) {
                boolean taken = false;
                for (Client c : clientList) {
                    if (c.username.equals(name)) {
                        taken = true;
                        break;
                    }
                }
                if (!taken) {
                    myClient.username = name;
                    clientList.add(myClient);
                    out.writeBytes("Welcome, " + name + "!\n");
                    broadcastMessage(name + " has joined the chat");
                    break;
                } else {
                    out.writeBytes("Username already in use, try another:\n");
                }
            }
        }

        // 2) Main message loop: handle commands via switch
        String clientText;
        while ((clientText = in.readLine()) != null) {
            switch (clientText) {
                case "Goodbye":
                case "Goodbye!":
                    out.writeBytes("Goodbye!\n");
                    removeClient();
                    broadcastMessage(myClient.username + " has left the chat");
                    myClient.connectionSock.close();
                    try {
                        myClient.connectionSock.close();
                    } catch (IOException ignored) { }
                    return;

                case "Who?":
                    StringBuilder users = new StringBuilder("Connected users:\n");
                    synchronized (clientList) {
                        for (Client c : clientList) {
                            if (!c.username.equals(myClient.username)) {
                                users.append(c.username).append("\n");
                            }
                        }
                    }
                    out.writeBytes(users.toString());
                    break;

                default:
                    broadcastMessage(myClient.username + ": " + clientText);
                    break;
            }
        }

    } catch (java.net.SocketException se) {
        
    } catch (IOException e) {
        e.printStackTrace();
        removeClient();
        broadcastMessage(myClient.username + " has left the chat unexpectedly");
    }
}


  // broadcast the message to all other clients than sender
  private void broadcastMessage(String message) {
    synchronized (clientList) {
        for (Client c : clientList) {
            try {
                DataOutputStream out = new DataOutputStream(c.connectionSock.getOutputStream());
                out.writeBytes(message + "\n");
            } catch (IOException ignored) { }
        }
    }
  }

  // Remove the client from the client list when it disconnects.
  private void removeClient() {
    synchronized (clientList) {
        clientList.remove(myClient);
    }
  }
}
