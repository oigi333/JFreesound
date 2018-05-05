package pl.michalwa.jfreesound.auth;

import org.apache.http.client.methods.HttpGet;

/** The basic authentication mechanism using an access token */
public class BasicAuthentication implements Authentication
{
	/** The API token used for authentication */
	private String token;
	
	/** Constructs an instance of the basic authentication mechanism
	 * with the given API access token. */
	public BasicAuthentication(String token)
	{
		this.token = token;
	}
	
	@Override
	public void processRequest(HttpGet request)
	{
		request.setHeader("Authorization", "Token " + token);
	}
}
