import java.net.Socket;

public class Client {
  public Socket connectionSock = null;
  public String username = "";

  public Client(Socket sock, String username) {
    this.connectionSock = sock;
    this.username = username;
  }
}
