package response;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This abstract class represents the response handler to user requests when the user is yet to log in.
 * Each sub-class constructor will call a JSONObject with a "password" and a "username" field.
 *  
 */
public abstract class ResponseNotLogged extends Response {
	
	protected static final String REQUEST_JSON_KEY_PASSWORD = "password";
	protected String password;
	
	/**
	 * Every request while logged in will provide the username and the password thus these attributes are always initialized.
	 * @throws JSONException if the format is bad or if the fields do not exist.
	 */
	public ResponseNotLogged(JSONObject info) throws JSONException { 
		try {
			username = info.getString(REQUEST_JSON_KEY_USER);
			password = info.getString(REQUEST_JSON_KEY_PASSWORD);
			
		}
		catch(JSONException e) {
			e.printStackTrace();
			statusCode=HttpStatus.SC_BAD_REQUEST;					
			throw e;			
		}
		
	}
	
}
