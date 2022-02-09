import java.util.Date;

public class Journey 
{
	private City aCity;
	private City dCity;
	
	public Journey(City aCity, City dCity) 
	{
		this.aCity=aCity;
		this.dCity=dCity;
	}
	
	public City getACity() 
	{
		return aCity;
	}
	
	public City getDCity() 
	{
		return dCity;
	}


}
