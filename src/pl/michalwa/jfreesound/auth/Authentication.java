package pl.michalwa.jfreesound.auth;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

/** Represents an authentication type and mechanism */
public abstract class Authentication
{
	private URIBuilder uriBuilder;
	
	/** Processes the given HTTP request before execution.
	 * Applies headers, parameters, etc. */
	public void processRequest(HttpGet request)
	{
		URI uri = request.getURI();
		uriBuilder = new URIBuilder(request.getURI());
		process(request);
		try {
			uri = uriBuilder.build();
		} catch(URISyntaxException e) {
			e.printStackTrace();
		}
		request.setURI(uri);
	}
	
	protected abstract void process(HttpGet request);
}
