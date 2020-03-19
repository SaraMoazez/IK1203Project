package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
	
    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
    	if(ToServer == null){
			return askServer(hostname, port);
		}
		//initiates the TCP connection between client and server
    	Socket clientSocket = new Socket(hostname,port);
		//timeout 2 seconds.
    	clientSocket.setSoTimeout(2000);
		//create stream objects attached to the socket
    	DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    	BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		String sentence;
    	outToServer.writeBytes(ToServer+'\n');
    	StringBuilder builder = new StringBuilder();
		//characters from the server flow through the stream inFromServer and get placed into the string sentence.
    	try{
    		while( (sentence = inFromServer.readLine()) != "\n" && sentence != null){
    			builder.append(sentence+'\n');
    		}
    		clientSocket.close(); //close socket and TCP connection
    		return builder.toString();
    	}
    	catch( IOException e){
    		clientSocket.close();
    		return builder.toString();
    	}
    }
	
    public static String askServer(String hostname, int port) throws  IOException {
    	Socket clientSocket = new Socket(hostname,port);
    	clientSocket.setSoTimeout(2000);
    	BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
    	String sentence;
    	StringBuilder builder = new StringBuilder();
 
        int count = 0;
    	try{
    		while( (sentence = inFromServer.readLine()) != "\n" && sentence != null){
    			builder.append(sentence+'\n');
                count++;
                if(count >= 1024)
                    return builder.toString();
    		}
    		clientSocket.close();
    		return builder.toString();
    	}
    	catch( IOException e){
    		clientSocket.close();
    		return builder.toString();
    	}
    }
}
