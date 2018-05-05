package pl.michalwa.jfreesound.request;

/** A basic request implementation. Should only
 * be used for testing. */
public class SimpleRequest extends Request
{
	/** Parts of the request URL */
	private Object[] parts;
	
	/** Constructs a simple request with the given sub-url */
	public SimpleRequest(String url)
	{
		parts = new String[] { url };
	}
	
	/** Constructs a simple request from the given sub-url parts */
	public SimpleRequest(Object... url)
	{
		parts = url;
	}
	
	@Override
	protected String url()
	{
		return joinURL(parts);
	}
}
