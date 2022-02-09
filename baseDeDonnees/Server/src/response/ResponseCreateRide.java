package response;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class ResponseCreateRide extends ResponseLogged {

	
    public static final String REQUEST_JSON_KEY_START_LOCATION = "startLocation";   
    public static final String REQUEST_JSON_KEY_END_LOCATION = "endLocation";
    public static final String REQUEST_JSON_KEY_START_TIME = "startTime";
       
   
    private String cityStart;
    private String cityStop;
    private LocalDateTime startTime;
    
    
	
	
    /**
	 *  @param info : the JSONObject enclosed into the HTTP request as parameter.
	 */
	public ResponseCreateRide(JSONObject info) 
			throws JSONException,sqlexception.NotFoundException,sqlexception.NotAllowedException {
		super(info);
		try{
			
			cityStart = info.getString(REQUEST_JSON_KEY_START_LOCATION);
			cityStop = info.getString(REQUEST_JSON_KEY_END_LOCATION);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
			startTime = LocalDateTime.parse(info.getString(REQUEST_JSON_KEY_START_TIME), formatter);					
		
			SqlCreateRide(username,token, startTime, cityStart, cityStop);
			statusCode=HttpStatus.SC_OK;
			
		}
		catch(DateTimeParseException e) {
			e.printStackTrace();
			statusCode=HttpStatus.SC_BAD_REQUEST;
			throw e;			
		}
		
		catch (sqlexception.NotFoundException e) {
			e.printStackTrace();
			statusCode=HttpStatus.SC_NOT_FOUND;
			throw e;
		}
	
		catch (sqlexception.NotAllowedException e) {
			e.printStackTrace();
			statusCode=HttpStatus.SC_FORBIDDEN;
			throw e;
		}
	}
		
	private void SqlCreateRide( final String username, final String token, final LocalDateTime startTime,
			final String cityStart, final String cityStop) 
			throws sqlexception.NotFoundException,sqlexception.NotAllowedException {
		
		/*TODO: Creates a ride with the given parameters
		 *  
		 *  hint : formatter.format(startTime) to get a string from the date under the precised format
		 * */
		
	}

}
