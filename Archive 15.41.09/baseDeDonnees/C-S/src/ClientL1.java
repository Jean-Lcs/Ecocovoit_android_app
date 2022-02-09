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
			
	    OutputStream output = socket.getOutputStream();
	    PrintWriter writer = new PrintWriter(output,true);
		Scanner scan = new Scanner(System.in);
			
		InputStream input = socket.getInputStream();
		
		BufferedReader brc1 = new BufferedReader(new InputStreamReader(input));
			 
			){
				
	    while(true) {	
		
	    String line = brc1.readLine();
		System.out.println(line);
		
		String message = scan.nextLine();
		writer.println("msg for server"+message);
		
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