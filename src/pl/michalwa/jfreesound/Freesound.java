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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import pl.michalwa.jfreesound.request.Request;

/** The main API client class. Used to make requests and retriev
 * data from the API. To use this class, firstly build an instance
 * with {@link Freesound.Builder Freesound.builder()}. */
public class Freesound
{
	/** The base url that all API request URLs begin with.
	 * Used by the {@link Request} class to construct request URLs. */
	public static final String API_BASE_URL = "https://freesound.org/apiv2/";
	
	/** The API secret access token */
	private String token;
	/** The HttpClient used to make requests to the API */
	private HttpClient http;
	/** The parser used to parse json responses from the API */
	private JsonParser json;
	
	/** Constructs the API client */
	private Freesound(String token, HttpClient http, JsonParser json)
	{
		this.token = token;
		this.http = http;
		this.json = json;
	}
	
	/** Submits a request to the API and returns the result as JSON.
	 * @throws IOException if the HTTP GET request fails */
	public JsonObject request(Request request) throws IOException
	{
		// Prepare the request
		HttpUriRequest httpRequest = request.httpRequest();
		httpRequest.setHeader("Authorization", "Token " + token);
		
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
		private String token = null;
		private HttpClient http = null;
		private JsonParser json = null;
		
		/** Sets the Freesound API token. Required
		 * to build a Freesound instance. */
		public Builder token(String token)
		{
			this.token = token;
			return this;
		}
		
		/** Uses the given HttpClient to make http requests
		 * to the API instead of the default one. */
		public Builder useHttpClient(HttpClient http)
		{
			this.http = http;
			return this;
		}
		
		/** Uses the given JsonParser to parse API
		 * responses instead of the default one. */
		public Builder useJsonParser(JsonParser json)
		{
			this.json = json;
			return this;
		}
		
		/** Builds the API client instance */
		public Freesound build()
		{
			if(token == null) throw new IllegalStateException("Token cannot be null.");
			if(http == null) http = HttpClientBuilder.create().build();
			if(json == null) json = new JsonParser();
			
			return new Freesound(token, http, json);
		}
	}
}
