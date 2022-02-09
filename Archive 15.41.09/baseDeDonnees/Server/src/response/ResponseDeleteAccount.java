package response;



import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.json.JSONException;

public final class ResponseDeleteAccount extends ResponseLogged {
	
	/**
	 *  @param info : the JSONObject enclosed into the HTTP request as parameter.
	 */
	public ResponseDeleteAccount(JSONObject info) throws JSONException,sqlexception.NotFoundException {
		super(info);
		try{
			SqlDeleteAccount(username,token);
			statusCode=HttpStatus.SC_OK;
			
		}
				
		catch (sqlexception.NotFoundException e) {
			e.printStackTrace();
			statusCode=HttpStatus.SC_NOT_FOUND;
			throw e;
		}
		
		
		
	}
	
	
	private void SqlDeleteAccount(final String username, final String token) throws sqlexception.NotFoundException {
		//TODO : cf name
	}

	

}
