package response;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This abstract class represents the response handler to user requests when the user is already logged in.
 * Each sub-class constructor will call a JSONObject with a "token" field.
 * The token is a temporary access given to user instead of providing his credentials every time he makes a request.
 * However, due to the incertainty of the generation token function being a bijection, each request needs to contain
 * the username and the token. 
 * 
 */
public abstract class ResponseLogged extends Response {
	
	protected static final String REQUEST_JSON_KEY_TOKEN = "token";	
	protected String token;
	
	/**
	 * Every request while logged in will provide the username and the token thus these attributes are always initialized.
	 * @throws JSONException if the format is bad or if the fields do not exist.
	 */
	public ResponseLogged(JSONObject info) throws JSONException { 
			try {
				username = info.getString(REQUEST_JSON_KEY_USER);
				token = info.getString(REQUEST_JSON_KEY_TOKEN);
				
			}
			catch(JSONException e) {
				e.printStackTrace();
				statusCode=HttpStatus.SC_BAD_REQUEST;					
				throw e;			
			}
			
		}
	
	/**
	 * Checks whether or not the token provided in the request is the same as the one in the database.
	 * If both tokens are Null, then it means the user isn't logged in, so it returns false.
	 * @return true if the tokens are equals else false
	 */
	protected boolean checkToken(/* MySQLObject database */) {
		//TODO : compare user token to token stored in database
		
		return true;
	}

}
