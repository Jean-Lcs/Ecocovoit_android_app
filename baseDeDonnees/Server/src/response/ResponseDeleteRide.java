package response;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

public final class ResponseDeleteRide extends ResponseLogged {

	
	private static final String REQUEST_JSON_KEY_RIDE = "ride";
	
	private int ride;
	
	/**
	 *  @param info : the JSONObject enclosed into the HTTP request as parameter.
	 */
	public ResponseDeleteRide(JSONObject info) 
			throws JSONException,sqlexception.NotFoundException,sqlexception.NotAllowedException {
		super(info);
		try{
			ride = Integer.getInteger(info.getString(REQUEST_JSON_KEY_RIDE));
			SqlDeleteRide(username,token,ride);
			statusCode=HttpStatus.SC_OK;
			
		}
				
		catch (sqlexception.NotFoundException e) {
			e.printStackTrace();
			statusCode= HttpStatus.SC_NOT_FOUND;
			throw e;
		}
	
		catch (sqlexception.NotAllowedException e) {
			e.printStackTrace();
			statusCode= HttpStatus.SC_FORBIDDEN;
			throw e;
		}
	}
		
	private void SqlDeleteRide( final String username, final String token, final int ride ) 
			throws sqlexception.NotFoundException,sqlexception.NotAllowedException {
		
		/*TODO: Seeks ride corresponding to ride(number) in username profile, deletes it
		Should it delete itineraries too? */
		
	}

}
