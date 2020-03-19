import java.net.*;
import java.io.*;

public class ConcHTTPAsk {
  public static void main( String[] args) throws IOException {
    int port;
    if(args.length > 0){
      port = Integer.parseInt(args[0]);
    } else { port = 8888; }
    try{
      ServerSocket ServerSocket = new ServerSocket(port);
      while(true){
        (new Thread(new MyRunnable(ServerSocket.accept()))).start();
      }
    } catch(IOException e){
      System.err.println(e);
    }
  }
}