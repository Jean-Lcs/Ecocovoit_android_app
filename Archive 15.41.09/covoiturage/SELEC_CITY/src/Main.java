import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONArrayBuilder;*/
import java.io.BufferedReader;



public class Main {
	
	public static void main(String[] args) 
	{
			// on essaye de récuper la City à partir de RES, On créé le 'journey', on set le score des villes, on execute l'algo de selection
		
		String dCity= "paris"; 			// minuscule, pas de tiret
		String aCity= "marseille";
		
		PrintWriter pw = null;
		
		try {
			CityList cityList = new CityList();

			City arrivalCity= cityList.getCityList().get(0);
			City departureCity= cityList.getCityList().get(0);
			
					
			
			for (City city: cityList.getCityList()) 
			{
				if (city.getName().equals(dCity)){departureCity= city;}
				if (city.getName().equals(aCity)){arrivalCity= city;}
			}
			
			System.out.println(departureCity.getName()+", X:"+departureCity.getX()+", Y:"+departureCity.getY()+", Z:"+departureCity.getZ()+", Pop:"+departureCity.getPopulation());
			System.out.println(arrivalCity.getName()+", X:"+arrivalCity.getX()+", Y:"+arrivalCity.getY()+", Z:"+arrivalCity.getZ()+", Pop:"+arrivalCity.getPopulation());
			double distance = arrivalCity.getDistance(departureCity,arrivalCity);
			System.out.println(distance);
			
			Journey journey = new Journey(arrivalCity,departureCity);
			
			
			pw  = new PrintWriter("data/otherList1 .txt");
			for (int k=0;k<30000;k++) 
			{
				City city= cityList.getCityList().get(k);
				city.setScore(journey);
				pw.write(city.getName()+","+city.distanceToRoad(journey)+ ","+city.getPopulation()+ ","+city.getScore()+'\n');
			}
			System.out.println("distance saint etienne à la route:  " + cityList.getCityList().get(16123).distanceToRoad(journey));
			
			ArrayList<City> res = cityList.selecCity(20, journey);
			
			
			/**City city1= new City("1",0,0,0,0); City city2= new City("2",0,0,0,0);City city3= new City("3",0,0,0,0);
			city1.setScore(1.0);city2.setScore(30.0);city3.setScore(0.0);
			ArrayList<City> test= new ArrayList<City>();
			test.add(city1);test.add(city2);test.add(city3);
			for(int i=0;i<test.size();i++) 
			{
				System.out.println(test.get(i).getName()+"  "+test.get(i).getScore());
			}
			CityList.triSelect(test);
			for(int i=0;i<test.size();i++) 
			{
				System.out.println(test.get(i).getName()+"  "+test.get(i).getScore());
			}*/
			
			
			for (City city:res) 
			{
				System.out.println(city.getName()+"---"+city.getScore());
			}
			
			/**JSONObject myjson= new JSONObject();
			JSONArray arr = new JSONArray();
			for (City city:res) 
			{
				arr.put(city.carteToGps(city.getX(),city.getY(),city.getZ()).get(0)+","+city.carteToGps(city.getX(),city.getY(),city.getZ()).get(1));
			}
			myjson.put("middleCities", arr);*/
			
			System.out.println(cityList.getCityList().get(0).getScore());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {pw.close();}
	}
}


