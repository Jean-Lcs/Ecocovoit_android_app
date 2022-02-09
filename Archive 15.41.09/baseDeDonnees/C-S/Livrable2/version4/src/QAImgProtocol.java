
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO; 


public class QAImgProtocol {
	private static final int numberQ = 4;

	private int numberAsked;
	
	
	public QAImgProtocol() {
		numberAsked = 0; 
	}

	public int getNumberQ() {
		return numberQ;
	}

	private int numberQuestionsAsked() {
		return numberAsked;
	}
	
	private static int factorial(int n) {
		if (n<0) return -1;
		
		int res=1;
		for (int i=1; i< n+1; i++) {
			res *= i;
		}
		return res;
	}    
	    
	public static String date() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
		LocalDateTime date =  LocalDateTime.now();  
		return formatter.format(date);
	}
	
	public static int[] readDate(String date) {
		String[] parsing = date.split(" ");
		
		String[] accurDate = parsing[1].split(":");
		
		String[] globDate = parsing[0].split("/");
		
		int[] res = new int[6];
		
		for (int i =0;i<3;i++) {res[i] = Integer.parseInt(accurDate[2-i]);}
		
		for (int i =3;i<6;i++) {res[i] = Integer.parseInt(globDate[i-3]);}
		
		return res;		//res : [s, m, H, d, M, y]
		
	}
	
	
	public static boolean compareDate(String date1, String date2) {
		int[] N1 = readDate(date1);
		int[] N2 = readDate(date2);
		
		int deltaT = 0;
		
		
		deltaT = N1[0] - N2[0] + (N1[1] - N2[1])*60 + (N1[2] - N2[2]) *3600 + (N1[3] - N2[3]) *3600*24;
		//At this point, it's just "no luck" if the date is on a changing month or year.
		//It's not the aim of this exercise, so it should be enough
		
		if (Math.abs(deltaT) < 30) return true;
		
		return false;
		
		
	}
	
	private static BufferedImage sendImage(String name) {	
		File imgFile = new File("data/" + name);
		BufferedImage img = null;
		try {
			
			img = ImageIO.read(imgFile);
			return img;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return img;
		
				
	}
	
	public String[] readQuestion(String fileName) throws FileNotFoundException,IOException {
		String[] questionsClient = new String[numberQ];
			
		try (BufferedReader buffer = new BufferedReader(new FileReader(fileName));
			){
			String line;
			int cpt = 0;
			while ((line = buffer.readLine()) != null && cpt < numberQ) {
				questionsClient[cpt] = line;
				cpt++;
			}
			
			return questionsClient;
			
		}
	
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("File name not correct");
		}

		catch (IOException e) {
			throw new IOException("Issue with IO streams");
		}
		
	}
	
	public Object AnswerQuestion(String question) {
		System.out.println(question);
		String[] parsedText = question.split(":");
		numberAsked++;
		
		switch(parsedText[0]) {
			case "factorial":
				return String.valueOf(factorial(Integer.parseInt(parsedText[1])));
			
			case "date":
				return date();
			
			case "numberQuestionsAsked":
				return String.valueOf(numberQuestionsAsked());
				
			case "Envoie l'image":
				return sendImage(parsedText[1]);
			
			default:
				return "Error: Question doesn't match models!";
			}
						
		}

}

	