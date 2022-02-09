import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import sqlexception.InvalidEntryException;
import sqlexception.NotAllowedException;
import sqlexception.NotFoundException;

/**
 *  HTTP json object server based on a classic (blocking) I/O model.
 */
public class HttpReadOnlyServer {

    public static void main(String[] args) throws Exception {
    	
      
        int port = 8080;	//Basic port for HTTP
       

      
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();

        final HttpServer server = ServerBootstrap.bootstrap()
                .setListenerPort(port)
                .setServerInfo("Test/1.1")
                .setSocketConfig(socketConfig)
                .setExceptionLogger(new StdErrorExceptionLogger())
                .registerHandler("*", new HttpJsonHandler())
                .create();

        server.start();
        server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.shutdown(5, TimeUnit.SECONDS);
            }
        });
    }

    static class StdErrorExceptionLogger implements ExceptionLogger {

        @Override
        public void log(final Exception ex) {
            if (ex instanceof SocketTimeoutException) {
                System.err.println("Connection timed out");
            } else if (ex instanceof ConnectionClosedException) {
                System.err.println(ex.getMessage());
            } else {
                ex.printStackTrace();
            }
        }

    }

    static class HttpJsonHandler implements HttpRequestHandler  {

        public HttpJsonHandler() {
            super();           
        }

        public void handle(
                final HttpRequest request,
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {
        	

            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
            
            //Checks whether or not the methods are supported
            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST") ) {
                throw new MethodNotSupportedException(method + " method not supported");
            }
                        
            
            
            //Since the request must contain a JSONObject, it has to be an HttpEntityEnclosing request
            if (request instanceof HttpEntityEnclosingRequest) { 
				
            	//debug line:
            	System.out.println( "The request is of type : " +((HttpEntityEnclosingRequest) request).getEntity().getClass().getName() );
            	
            	//The enclosing entity is to be a BasicHttpEntity
            	if (((HttpEntityEnclosingRequest) request).getEntity() instanceof BasicHttpEntity) {
                              

            		try(BufferedReader reader =  new BufferedReader(
            				new InputStreamReader((((HttpEntityEnclosingRequest)request).getEntity().getContent())));
            				
            		){
            			String content =  reader.readLine();  
            			
            			
            			//Debug line:
            			System.out.println(content);
            			
            			//The JSONObject cannot be created from a null String
            			if (content ==null) {
            				throw new NullPointerException();
            			}
            			
            			JSONObject entity = new JSONObject(content);
            			System.out.println("JSON content acquired \n");
            			
            			
            			//One Response class is associated to one possible function
            			switch(entity.getString("function")) {
            			
            				case "CreateRide()":
            					ResponseSetter(response, context ,new response.ResponseCreateRide(entity));
            					break;
            				
            				case "SeekJourney()":
            					ResponseSetter(response, context ,new response.ResponseSeekJourney(entity));            					
            					break;
            				
            				case "DeleteRide()":
            					ResponseSetter(response, context ,new response.ResponseDeleteRide(entity));
            					break;
            					           				
            				case "Login()":
            					ResponseSetter(response, context ,new response.ResponseLogin(entity));
            					break;
            					
            				case "Logout()":
            					ResponseSetter(response, context ,new response.ResponseLogout(entity));
            					break;
            				
            				case "UserInfo()":
            					ResponseSetter(response, context ,new response.ResponseUserInfo(entity));
            					break;
            					
            				case "DirectionPath()":
            					ResponseSetter(response, context ,new response.ResponseDirectionPath(entity));
            					break;
            				
            				case "CreateAccount()":
            					ResponseSetter(response, context ,new response.ResponseCreateAccount(entity));
            					break;
            				
            				case "DeleteAccount()":
            					ResponseSetter(response, context ,new response.ResponseDeleteAccount(entity));
            					break;            				
            			
            				default:
            					response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            					throw new JSONException("Function name does not match predefined values");
            			
            			}            			
            			
            			
            		}
            		catch (JSONException  e) {
            			throw new JSONException("An unexpected error occurred while reading the request content.", e);
            		}
            		
            		catch(NullPointerException e) {
            			System.err.println("The stream is down \n");
            			throw e;
            		} 
            		catch (NotFoundException e) {
						e.printStackTrace();
					} 
            		catch (NotAllowedException e) {
						e.printStackTrace();
					} 
            		catch (InvalidEntryException e) {
						e.printStackTrace();
					}
                
            		
            	}
            
            }
            
            else {            
        		response.setStatusCode(HttpStatus.SC_FORBIDDEN);
        	}
           
            
        }

    }


public static void ResponseSetter(final HttpResponse response,
        final HttpContext context, response.Response requestResp) throws HttpException, IOException {
	
	requestResp.formatHttpResponse(response);
	
}



}


        

      
      
