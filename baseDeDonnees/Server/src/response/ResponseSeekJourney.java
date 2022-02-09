package response;

import org.json.JSONObject;

public final class ResponseSeekJourney extends ResponseLogged {
	
	/**
	 *  @param info : the JSONObject enclosed into the HTTP request as parameter.
	 */
	public ResponseSeekJourney(JSONObject info) {
		super(info);
	}

}
