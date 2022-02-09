package response;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  This class handles the response to an Account Creation Request.
 */
public final class ResponseCreateAccount extends ResponseNotLogged {

	
	/**
	 *  @param info : the JSONObject enclosed into the HTTP request as parameter.
	 */
	public ResponseCreateAccount(JSONObject info) throws JSONException,sqlexception.InvalidEntryException {
		super(info);
		try{			
			SqlCreateAccount(username,password);
			statusCode=HttpStatus.SC_OK;
			
		}
				
		catch (sqlexception.InvalidEntryException e) {
			e.printStackTrace();
			statusCode=HttpStatus.SC_CONFLICT;
			throw e;
		}
	}
		
	private int SqlCreateAccount(String username, String password ) throws sqlexception.InvalidEntryException {
		
		//TODO: return the authentification token if no error and creates fields for the new user
		
		int token =0;
		
		return token;
	}
	
	
	
		
		


}
