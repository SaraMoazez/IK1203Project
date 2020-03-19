import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class MyRunnable implements Runnable {
  Socket connectionSocket;
  String HTTP200 = "HTTP/1.1 200 OK\r\n\r\n";
  String HTTP400 = "HTTP/1.1 400 Bad Request\r\n";
  String HTTP404 = "HTTP/1.1 404 Not Found\r\n";
  String port = null;
  String hostname = null;
  String string = null;
  String request;

  public MyRunnable(Socket connectionSocket) {
    this.connectionSocket = connectionSocket;
  }
  public void run() {
    try {
      BufferedReader inPutStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      DataOutputStream outPutStream = new DataOutputStream(new DataOutputStream(connectionSocket.getOutputStream()));
      StringBuilder response =  new StringBuilder();
      request = inPutStream.readLine();
      if(request != null){
        // Split the request into parts
        String[] parts = request.split("[?&= ]");
        for(int i = 0; i < parts.length; i++){
          if(parts[i].equals("hostname"))
            hostname = parts[++i];
          else if(parts[i].equals("port"))
            port = parts[++i];
          else if(parts[i].equals("string"))
            string  = parts[++i];
        }
        if(hostname != null && port != null && port.matches("[0-9]+") && parts[1].equals("/ask")){
          try{
            String serverResponse = TCPClient.askServer(hostname,  Integer.parseInt(port), string);
            // add the HTTP header response
            response.append(HTTP200);
            response.append(serverResponse);
          } catch(IOException e) {
            response.append(HTTP404);
          }
        } else {
          response.append(HTTP400);
        }
        outPutStream.writeBytes(response.toString());
      }
      // close connection
      connectionSocket.close();
    } catch(IOException e) {
      System.err.println(e);
    }
  }
}