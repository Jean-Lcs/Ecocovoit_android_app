package response;

import org.apache.http.HttpStatus;

import org.json.JSONException;
import org.json.JSONObject;

public final class ResponseLogout extends ResponseLogged {
	
	/**
	 *  @param info : the JSONObject enclosed into the HTTP request as parameter.
	 */
	public ResponseLogout(JSONObject info) 
			throws JSONException, sqlexception.NotFoundException, sqlexception.NotAllowedException {
		super(info);
		try{
			SqlLogoutAccount(username,token);
			statusCode=HttpStatus.SC_OK;
			
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
		
	private void SqlLogoutAccount( final String username, String token ) 
			throws sqlexception.NotFoundException, sqlexception.NotAllowedException {
		
		//TODO: return the authentification token if no error and make logged in column to true?
		
	}

}
