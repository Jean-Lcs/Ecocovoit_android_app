import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.util.Scanner;


public class ServerL1 {

public static void main(String args[]) {
	

int n = 3987; //port number

	try(ServerSocket sSocket = new ServerSocket(n); 
		Socket socket = sSocket.accept();
		BufferedReader brs1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
		Scanner scan = new Scanner(System.in);
			
			){
		
				
	    while(true) {	
	    String message = scan.nextLine();
		writer.println("Message for client : "+message);
		
	    String line = brs1.readLine();
		System.out.println(line);
		
		
		 
	    }
	}
		catch (UnknownHostException e) {
			e.printStackTrace();
			
		}
		catch (IOException e) {
			System.err.println("Wrong port");

		}
}

}
