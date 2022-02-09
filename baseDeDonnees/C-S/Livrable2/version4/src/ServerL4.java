
import java.awt.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;


public class ServerL4 {

	public static void main(String args[]) {
			try(ServerSocket servSocket = new ServerSocket(32513);
				Socket socket = servSocket.accept();
				InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(streamReader);
				ObjectOutputStream imgStream = new ObjectOutputStream(socket.getOutputStream());

				){
				
				imgStream.reset();
							
				QAImgProtocol quest = new QAImgProtocol();
				int n =0;
				int max =quest.getNumberQ();
				while(n <max) {	
			
					
					String line = reader.readLine();    // reads a line of text
				
			
					if (line.split(":")[0].equals("Envoie l'image")) {
						
						Image img = (Image) quest.AnswerQuestion(line);
						ImageIcon icon = new ImageIcon(img);
						imgStream.writeObject(icon);
						
					}
			
					else {
						imgStream.writeObject(quest.AnswerQuestion(line));				
					}
			
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
