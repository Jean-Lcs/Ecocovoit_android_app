package response;

/**
 * This abstract class represents the basic response handler to user requests.
 * Its sub-classes are designed to each answer a specific type of request which is determined by the "function" field
 * of the JSONObject entity received by the HTTP Server
 * Each and every of the protected static final String attributes (in the sub-classes too) are keywords to read the formatted JSONObject 
 * info passed to every constructor of the sub-classes.
 * 
 */
public abstract class Response {

	protected static final String REQUEST_JSON_KEY_USER = "user";
	
	/**
	 * In every request, the user will provide his username.
	 * 
	 */
	protected String username;
	
	/**
	 * It represents the StatusCode of the HTTP Response.
	 * 
	 */
	protected int statusCode;
	
	/**
	 * A JSONObject's string which will be passed through the HTTP Response.
	 * 
	 */
	private String resp;
	
	/**
	 * This function sets the HTTP Response which will be sent to the user.
	 * @param response The HTTP Response sent to the user.
	 */
	public void formatHttpResponse(org.apache.http.HttpResponse response) {
		response.setStatusCode(statusCode);

	}

}
