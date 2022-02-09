import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CityList 
{
	public CityList()
	{
		FileReader     fr = null;
		BufferedReader br = null;
		PrintWriter pw=null;
		try
		{
			fr = new FileReader("data/cityList.txt") ;
			br = new BufferedReader(fr) ;
			cityList =new ArrayList<City>();
			String mots;
			while ((mots = br.readLine()) != null)
			{
				String[] s = mots.split(",");
				ArrayList<Double> gps = new ArrayList<Double>();
				gps.add(Double.parseDouble(s[20]));
				gps.add(Double.parseDouble(s[19]));
				ArrayList<Double> cart = gpsToCart(gps);
				String name= s[4];
				cityList.add(new City(name,cart.get(0),cart.get(1),cart.get(2),Integer.parseInt(s[s.length-11])));
			}
		} catch(IOException e) {System.out.println("An IO exception occured"); }
		finally 
		{
			try
			{
				fr.close();
				br.close();
			}catch(Exception e) {}	
		}
	}
	
	private ArrayList<City> cityList;
	
	public void add(City city) 
	{
		cityList.add(city);
	}
	
	public  ArrayList<City> getCityList()
	{
		return this.cityList;
	}
	
	
	private static void swap (int i, int j,ArrayList<City> arr) 
	{
		City cityTmp = arr.get(i);
		arr.set(i,arr.get(j));
		arr.set(j,cityTmp);
	}
	
	
	/**public static ArrayList<City> sortCities(ArrayList<City> arr) 
    {  
         int taille = arr.size();  
       
         for (int i = 1; i < taille; i++)
         { 
        	  double score = arr.get(i).getScore();
              int j = i-1;  
           
              while(j >= 0 && arr.get(j).getScore() < score)  
              {
            	   swap(j,j+1,arr); 
            	   j++;
              }     
       }
       return arr;
    } */
	

     public static ArrayList<City> triSelect (ArrayList<City> arr )
     { 
        for (int i =arr.size()-1;i>-1;i--) 
         {
           for (int j =0;j< i;j++) 
           {
        	   if(arr.get(j).getScore() < arr.get(j+1).getScore())
               {
        		   swap(j,j+1,arr);
               }
           }
         }
		return arr;
     }
     
     
	public ArrayList<City> selecCity(int k, Journey journey) 
	{
		double distance= City.getDistance(journey.getACity(), journey.getDCity());
		ArrayList<City> res = new ArrayList<City>();
		for (int i=0; i<k; i++) 
		{
			res.add(cityList.get(i));
		}
		res=triSelect(res);
		for (City city:cityList) 
		{
			if (city.getScore()>res.get(k-1).getScore() 
					&& City.getDistance(city,journey.getACity())<distance 
					&&  City.getDistance(city,journey.getDCity())<distance) 
			{
				res.set(k-1, city);
				res=triSelect(res);
			}
		}
		return res;
	}
	
	
	
	public static ArrayList<Double> carteToGps(ArrayList<Double> arr)
	{
		ArrayList<Double> res = new ArrayList<Double>();
		double r=6371000.;
		res.add(Math.asin(arr.get(2) / r));
		res.add(Math.atan2(arr.get(1), arr.get(0)));
		return res;
	}
	
	public static ArrayList<Double> gpsToCart(ArrayList<Double> arr)
	{
		ArrayList<Double> res = new ArrayList<Double>();
		double r=6371000.;
		res.add(r*Math.cos(arr.get(0)*Math.PI/180)*Math.cos(arr.get(1)*Math.PI/180));
		res.add(r*Math.cos(arr.get(0)*Math.PI/180)*Math.sin(arr.get(1)*Math.PI/180));
		res.add(r*Math.sin(arr.get(1)*Math.PI/180));
		return res;
	}
	
	
	
	
	
	
	
	
	
}
