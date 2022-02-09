package response;

import org.apache.http.HttpStatus;

import org.json.JSONException;
import org.json.JSONObject;



import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class ResponseLogin extends ResponseNotLogged {

	private static final String SECRET_KEY = "pact21?";
		
	
	/**
	 *  @param info : the JSONObject enclosed into the HTTP request as parameter.
	 */
	public ResponseLogin(JSONObject info) 
			throws JSONException,sqlexception.InvalidEntryException, sqlexception.NotFoundException {
		super(info);
		try{
			SqlLoginAccount(username,password);
			statusCode=HttpStatus.SC_OK;
			
		}
				
		catch (sqlexception.InvalidEntryException e) {
			e.printStackTrace();
			statusCode=HttpStatus.SC_FORBIDDEN;
			throw e;
		}
		
		catch (sqlexception.NotFoundException e) {
			e.printStackTrace();
			statusCode= HttpStatus.SC_NOT_FOUND;
			throw e;
		}
	}
		
	private String SqlLoginAccount(String username, String password ) 
			throws sqlexception.InvalidEntryException, sqlexception.NotFoundException {
		
		//TODO: return the authentification token if no error and make logged in column to true?
		
		String token = generateToken(username, password, SECRET_KEY);
		
		return token;
	}
	
	
	private String generateToken(final String username, final String password, final String key) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
		LocalDateTime date =  LocalDateTime.now();  
				
		String message = username + password + formatter.format(date) ;
		
        String algorithm = "HmacSHA256";  

        try {
            
            Mac sha256_hmac = Mac.getInstance(algorithm);

            // 2. Create secret key.
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);

            // 3. Assign secret key algorithm.
            sha256_hmac.init(secret_key);

            // 4. Generate Base64 encoded cipher string.
            String hash = Base64.getEncoder().encodeToString(sha256_hmac.doFinal(message.getBytes("UTF-8")));

            return hash;
            

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (InvalidKeyException e) {

            e.printStackTrace();

        }
        
		return null;		//Will raise an error in the sql assignement if null
		
		
		
		
	}
}
