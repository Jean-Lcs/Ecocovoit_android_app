import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientL3 {
	
	public static void main(String args[]) throws Exception {
		
		

		
		if (args.length < 1) throw new Exception("Port number required");
		int port = Integer.parseInt(args[0]);
		
		try(Socket socket = new Socket("127.0.0.1", port);
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); 
	
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);	
			
				
		){
			
		QAProtocol quest = new QAProtocol();
		
		int n = 0;
		int max = quest.getNumberQ();
		
		
		String[] questions = quest.readQuestion("data/questions.txt");
		
		while(n < max) {	
					
		
		writer.println(questions[n]);
		System.out.println(questions[n]);
			
			
	 	//enables to read strings
		String line = reader.readLine();    // reads a line of text		
		
		
	
		if (questions[n].equals("date:")){	/*Careful: if in the text file there is a space type after 'date:' it won't work
													because the 2 strings won't be equals*/
			System.out.println("Checking dates");
			if(QAProtocol.compareDate(line, QAProtocol.date())){
				System.out.println("Dates are matching.");
				
			}
			
			else System.out.println("Dates do not match.");
		}
		System.out.println(line);
		
		n++;
			
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