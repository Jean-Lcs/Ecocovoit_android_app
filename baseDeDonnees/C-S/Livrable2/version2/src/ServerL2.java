import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerL2 {

	
	public static void main(String args[]) {
		try(ServerSocket servSocket = new ServerSocket(32513);
			Socket socket = servSocket.accept();
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			Scanner scanner = new Scanner(System.in);		
			){
			
		while(true) {		
			
		
	 	//Format the line to String
		String line = reader.readLine();    // reads a line of text

		System.out.println(line);	
		
		String message = scanner.nextLine();
		writer.println("Server message : " + message);
		

		
		}
		
		}
		
		catch (UnknownHostException e) {
	         e.printStackTrace();
		}
		catch(IOException e) {
			System.err.println("Bad port");
		}
		
	}

}
	
