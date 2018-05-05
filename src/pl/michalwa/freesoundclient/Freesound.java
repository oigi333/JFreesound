package pl.michalwa.freesoundclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Freesound
{
	private String token;
	private HttpClient http;
	
	private Freesound(String token)
	{
		this.token = token;
		http = HttpClientBuilder.create().build();
	}
	
	public JsonObject makeRequest(String url)
	{
		HttpGet request = new HttpGet(url);
		JsonObject json = null;

		request.setHeader("Authorization", "Token " + token);
		try {
			HttpResponse response = http.execute(request);
			HttpEntity entity = response.getEntity();
			json = new JsonParser().parse(new InputStreamReader(entity.getContent())).getAsJsonObject();
			EntityUtils.consume(entity);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	public static class Builder
	{
		private String token = null;
		
		public Builder withToken(String token)
		{
			this.token = token;
			return this;
		}
		
		public Freesound build()
		{
			if(token == null) throw new IllegalStateException("Token must be set.");
			Freesound freesound = new Freesound(token);
			return freesound;
		}
	}
}
