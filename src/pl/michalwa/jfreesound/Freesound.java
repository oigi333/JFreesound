package pl.michalwa.jfreesound;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import pl.michalwa.jfreesound.auth.Authentication;
import pl.michalwa.jfreesound.auth.BasicAuthentication;
import pl.michalwa.jfreesound.request.Request;

/** The main API client class. Used to make requests and retriev
 * data from the API. To use this class, firstly build an instance
 * with {@link Freesound.Builder Freesound.builder()}. */
public class Freesound
{
	/** The base url that all API request URLs begin with.
	 * Used by the {@link Request} class to construct request URLs. */
	public static final String API_BASE_URL = "https://freesound.org/apiv2/";
	
	/** The authentication mechanism used in API requests */
	private Authentication auth;
	/** The HttpClient used to make requests to the API */
	private HttpClient http;
	/** The parser used to parse json responses from the API */
	private JsonParser json;
	
	/** Constructs the API client */
	private Freesound(Authentication auth, HttpClient http, JsonParser json)
	{
		this.auth = auth;
		this.http = http;
		this.json = json;
	}
	
	/** Submits a request to the API and returns the result as JSON.
	 * @throws IOException if the HTTP GET request fails */
	public JsonObject request(Request request) throws IOException
	{
		// Prepare the request
		HttpGet httpRequest = request.httpRequest();
		auth.processRequest(httpRequest);
		
		// Execute the request
		HttpResponse response = http.execute(httpRequest);
		HttpEntity entity = response.getEntity();
		
		// Evaluate the request
		Reader responseReader = new InputStreamReader(entity.getContent());
		JsonObject result = json.parse(responseReader).getAsJsonObject();
		
		// Close and return
		EntityUtils.consume(entity);
		return result;
	}
	
	/** Submits a request to the API and returns the result as JSON.
	 * Unlike {@link Freesound#request(Request)} it catches the exception
	 * and passes it to the given {@link Consumer}.
	 * @returns The response as JSON or <code>null</code> if the request fails. */
	public JsonObject request(Request request, Consumer<IOException> onError)
	{
		JsonObject result = null;
		try {
			result = request(request);
		} catch(IOException e) {
			Optional.ofNullable(onError).ifPresent(err -> err.accept(e));
		}
		return result;
	}
	
	/** Returns the default Freesound instance builder */
	public static Builder builder()
	{
		return new Builder();
	}
	
	/** Freesound instance builder */
	public static class Builder
	{
		/* These fields hold the contructor arguments */
		private Authentication auth = null;
		private HttpClient http = null;
		private JsonParser json = null;
		
		/** Uses the {@link BasicAuthentication} with the given token. */
		public Builder withToken(String token)
		{
			this.auth = new BasicAuthentication(token);
			return this;
		}
		
		/** Uses the given HttpClient to make http requests
		 * to the API instead of the default one. */
		public Builder withHttpClient(HttpClient http)
		{
			this.http = http;
			return this;
		}
		
		/** Uses the given JsonParser to parse API
		 * responses instead of the default one. */
		public Builder withJsonParser(JsonParser json)
		{
			this.json = json;
			return this;
		}
		
		/** Builds the API client instance */
		public Freesound build()
		{
			// Required parameters
			if(auth == null) throw new IllegalStateException("Authentication type must be set.");
			
			// Optional parameters
			http = Optional.ofNullable(http).orElseGet(() -> HttpClientBuilder.create().build());
			json = Optional.ofNullable(json).orElseGet(JsonParser::new);
			
			return new Freesound(auth, http, json);
		}
	}
}
