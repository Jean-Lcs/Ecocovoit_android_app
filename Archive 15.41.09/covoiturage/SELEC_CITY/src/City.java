import java.util.ArrayList;

public class City 

{
	
	private String name;
	private double x;
	private double y;
	private double z;
	private double score;
	private int population;
	
	public City(String s, double x, double y,double z,int population) 
	{
		this.name=s;
		this.x= x;
		this.y= y;
		this.z= z;
		this.population=population;
	} 
	
	public void setX(double x) 
	{
		this.x=x;
	}
	
	public double getX() 
	{
		return this.x;
	}
	
	public void setY(double y) 
	{
		this.y=y;
	}
	
	public double getY() 
	{
		return this.x;
	}
	
	public void setZ(double z) 
	{
		this.z=z;
	}
	
	public double getZ() 
	{
		return this.z;
	}
	
	public int getPopulation() 
	{
		return this.population;
	}
	
	public void setScore(Journey journey) 
	{
		int populationMax=3000;
		int lengthFrance=1000000;
		
		this.score=Math.pow(population/populationMax,2)*(lengthFrance/this.distanceToRoad(journey))*1000000000;
	}
	
	public void setScore(Double score) 
	{
		this.score=score;
	}
	
	public double getScore() 
	{
		return this.score;
	}
	
	public static double getDistance(City city1, City city2) 
	{
		return Math.sqrt((Math.pow(city1.getX()-city2.getX(),2)+Math.pow(city1.getY()-city2.getY(),2)+Math.pow(city1.getZ()-city2.getZ(),2)));
	}
	
	public double distanceToRoad(Journey journey) 
	{
		ArrayList<Double> u= citiesToVect(journey.getACity(),journey.getDCity());
		ArrayList<Double> ba= citiesToVect(journey.getACity(),this);
		ArrayList<Double> v = prodVect(u,ba);
		return getNorme(v)/getNorme(u);
	}

	public String getName() 
	{
		return name;
	}
	
	public static ArrayList<Double> citiesToVect(City city1,City city2)
	{
		ArrayList<Double> res= new ArrayList<Double>();
		res.add(city1.getX()-city2.getX());
		res.add(city1.getY()-city2.getY());
		res.add(city1.getZ()-city2.getZ());
		return res;
	}
	
	public ArrayList<Double> cityToVect()
	{
		ArrayList<Double> res= new ArrayList<Double>();
		res.add(this.x);
		res.add(this.y);
		res.add(this.z);
		return res;
	}
	
	
	public double getNorme(ArrayList<Double> arr) 
	{
		return Math.sqrt((Math.pow(arr.get(0),2)+Math.pow(arr.get(1),2)+Math.pow(arr.get(2),2)));
	}
	
	public static ArrayList<Double> prodVect(ArrayList<Double> arr1,ArrayList<Double> arr2)
	{
		ArrayList<Double> res= new ArrayList<Double>();
		res.add(arr1.get(1)*arr2.get(2)-arr1.get(2)*arr2.get(1));
		res.add(arr1.get(2)*arr2.get(0)-arr1.get(0)*arr2.get(2));
		res.add(arr1.get(0)*arr2.get(1)-arr1.get(1)*arr2.get(0));
		return res;
	}
	
	
	
	
	
	
	
	
	
	public static ArrayList<Double> carteToGps(double x, double y, double z)
	{
		ArrayList<Double> res = new ArrayList<Double>();
		double r=6371000.;
		res.add(Math.asin(z / r));
		res.add(Math.atan2(y, x));
		return res;
	}
	
	public static ArrayList<Double> gpsToCart(ArrayList<Double> arr)
	{
		ArrayList<Double> res = new ArrayList<Double>();
		double r=6371000.;
		res.add(r*Math.cos(arr.get(0))*Math.cos(arr.get(1)));
		res.add(r*Math.cos(arr.get(0))*Math.sin(arr.get(1)));
		return res;
	}
	


	
	
}
