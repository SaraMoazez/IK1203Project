import java.net.*;
import java.io.*;

public class HTTPEcho {
    public static void main( String[] args) throws IOException {
      
		//get portnumber and open  port
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
		String header = "HTTP/1.1 200 OK\r\n\r\n";
		String fromClient;
        while(true){
	        	Socket connectionSocket = serverSocket.accept();
	        	connectionSocket.setSoTimeout(2000);
	        	BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	        	DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	   
	        	StringBuilder stringbuilder = new StringBuilder();
	        	stringbuilder.append(header);
		        while((fromClient = inFromClient.readLine()) != null && fromClient.length() !=0){
		        	stringbuilder.append(fromClient+"\r\n");
		        }
				
		        outToClient.writeBytes(stringbuilder.toString());
				//close connetion
		        connectionSocket.close();
		
        }
    }
}
