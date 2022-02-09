import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class ServerL3 {

	
	public static void main(String args[]) {
		try(ServerSocket servSocket = new ServerSocket(32513);
			Socket socket = servSocket.accept();
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				
			){
			
		
		QAProtocol quest = new QAProtocol();
		int n =0;
		int max =quest.getNumberQ();
		while(n <max) {	
		
	 	//enables to read strings
		String line = reader.readLine();    // reads a line of text
		
		
		writer.println(quest.AnswerQuestion(line));	
		n++;
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
	
