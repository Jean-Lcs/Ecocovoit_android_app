import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientL2 {
	
	public static void main(String args[]) throws Exception {
			
		if (args.length < 2) throw new Exception("Port number and ip required");
		int port = Integer.parseInt(args[0]);
		String ip = args[1];
		
		try(Socket socket = new Socket(InetAddress.getByName(ip) , port);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); 
			Scanner scanner = new Scanner(System.in);
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);	
				
		){
		while(true) {

		
		String message = scanner.nextLine();
		writer.println("Client message : " + message);
			
			
	 	//Format the line to string:
		String line = reader.readLine();    // reads a line of text
		
		System.out.println(line);
			
			}
		
		}
		
		catch (UnknownHostException e) {
	         e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		catch(Exception e) {
			throw e;
		}
		
	}
	

}
