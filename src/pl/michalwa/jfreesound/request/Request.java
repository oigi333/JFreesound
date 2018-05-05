package pl.michalwa.jfreesound.request;

import java.util.StringJoiner;
import java.util.stream.Stream;
import org.apache.http.client.methods.HttpGet;
import pl.michalwa.jfreesound.Freesound;

/** The base interface of all requests that can
 * be made to the API. A request stores some data
 * and builds an HTTP request based on that data. */
public abstract class Request
{
	/** Builds the HTTP request. */
	public HttpGet httpRequest()
	{
		return new HttpGet(Freesound.API_BASE_URL + url());
	}
	
	/** Returns the request sub-url (the part
	 * of the url after the base url) for this request. */
	protected abstract String url();
	
	/** Joins the given url parts into a request sub-url
	 * using the '/' (slash) separator. */
	public static String joinURL(Object... parts)
	{
		StringJoiner joiner = new StringJoiner("/");
		Stream.of(parts).forEach(obj -> joiner.add(obj.toString()));
		return joiner.toString();
	}
}
