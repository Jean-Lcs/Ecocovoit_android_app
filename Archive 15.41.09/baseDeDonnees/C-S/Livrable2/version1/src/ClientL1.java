import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.util.Scanner;


public class ClientL1 {

public static void main(String args[]) {
	

int n = 3987; //port number

	try(Socket socket = new Socket("127.0.0.1",n); //prend le port n et envoie à ma machine
	    PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
		Scanner scan = new Scanner(System.in);
		BufferedReader brc1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		){
				
	    while(true) {	
		
	    String line = brc1.readLine();
		System.out.println(line);
		
		String message = scan.nextLine();
		writer.println("Message for server : "+message);
		
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

