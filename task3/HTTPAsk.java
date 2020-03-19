import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class HTTPAsk {
    public static void main( String[] args) throws IOException {

      int portNumber;
      if(args.length > 0){
        portNumber = Integer.parseInt(args[0]);
      } else {
        portNumber = 8888;
      }

      ServerSocket HTTPSocket = new ServerSocket(portNumber);

      // headers
      String HTTP200 = "HTTP/1.1 200 OK\r\n\r\n";
      String HTTP400 = "HTTP/1.1 400 Bad Request\r\n";
      String HTTP404 = "HTTP/1.1 404 Not Found\r\n";

      String request;
      String port;
      String hostname;
      String string;

      while(true){
        try {
          Socket connectionSocket = HTTPSocket.accept();
          BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
          DataOutputStream outStream = new DataOutputStream(new DataOutputStream(connectionSocket.getOutputStream()));
          StringBuilder response =  new StringBuilder();
          request = in.readLine();

          if(request != null){
            // Split intp parts
            String[] parts = request.split("[?&= ]");

            // Reset
            port = null;
            hostname = null;
            string = null;
            for(int i = 0; i < parts.length; i++){
              if(parts[i].equals("hostname"))
                hostname = parts[++i];
              else if(parts[i].equals("port"))
                port = parts[++i];
              else if(parts[i].equals("string"))
                string = parts[++i];
            }

            if(hostname != null && port != null && port.matches("[0-9]+") && parts[1].equals("/ask")){
              try{

                String serverResp = TCPClient.askServer(hostname,  Integer.parseInt(port), string);

                response.append(HTTP200);

                response.append(serverResp);
              } catch(IOException e) {

                response.append(HTTP404);
              }
            } else {

              response.append(HTTP400);
            }

            outStream.writeBytes(response.toString());
          }
          // close connection
          connectionSocket.close();
        } catch(IOException e) {
          System.err.println(e);
        }
      }
    }

}