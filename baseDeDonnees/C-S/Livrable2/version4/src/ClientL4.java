import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientL4 {

	

	public static void displayPicture(ImageIcon icon) throws IOException {

		
		JFrame frame = new JFrame();

		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String args[]) throws Exception {

		if (args.length < 1)
			throw new Exception("Port number required");
		int port = Integer.parseInt(args[0]);

		try (Socket socket = new Socket("127.0.0.1", port);
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				ObjectInputStream objInput = new ObjectInputStream(socket.getInputStream());

		) {

			QAImgProtocol quest = new QAImgProtocol();
			
			
			
			int n = 0;
			int max = quest.getNumberQ();

			String[] questions = quest.readQuestion("data/questions.txt");

			while (n < max) {

				writer.println(questions[n]);
				System.out.println(questions[n]);

				// Since the server sends objects and not only strings, it is necessary to use a
				// if/else syntax on the question content

				if (questions[n].equals("date:")) { /*
													 * Careful: if in the text file there is a space type after 'date:'
													 * it won't work because the 2 strings won't be equals
													 */

					String line = (String) objInput.readObject(); // reads a line of text
					
					System.out.println(line);
					
					System.out.println("Checking dates");

					if (QAImgProtocol.compareDate(line, QAImgProtocol.date())) {
						System.out.println("Dates are matching.");

					}

					else
						System.out.println("Dates do not match.");
				} 
				
				else {

					if (questions[n].split(":")[0].equals("Envoie l'image")) {
						displayPicture((ImageIcon) objInput.readObject());
					}

					else {
						System.out.println((String) objInput.readObject());
					}

				}

				n++;
			}

		}

		catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	

		catch (Exception e) {
			throw e;
		}

	}

}
