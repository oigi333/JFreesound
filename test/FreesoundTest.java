import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import pl.michalwa.jfreesound.Freesound;
import pl.michalwa.jfreesound.auth.Authentication;
import pl.michalwa.jfreesound.request.SimpleRequest;

import static org.junit.Assert.*;

public class FreesoundTest
{
	String token;
	Freesound freesound;
	
	@Before
	public void setup()
	{
		// Read the configuration
		Reader reader = new InputStreamReader(getClass().getResourceAsStream("/config.json"));
		JsonObject config = new JsonParser().parse(reader).getAsJsonObject();
		token = config.get("token").getAsString();
		
		// Build the test object
		freesound = Freesound.builder().withToken(token).build();
	}
	
	@Test
	public void simpleRequestTest()
	{
		JsonObject response = freesound.request(new SimpleRequest("sounds", 1234), null);
		
		assertNotNull(response);
		assertEquals(1234,                   response.get("id").getAsInt());
		assertEquals("180404D.mp3",          response.get("name").getAsString());
		assertEquals("Traveling drum sound", response.get("description").getAsString());
	}
	
	@Test
	public void authenticationTest()
	{
		final String headerName = "LoremIpsum";
		final String headerValue = "loremIpsumDolor";
		
		HttpGet request = new HttpGet("http://example.com");
		Authentication auth = new Authentication()
		{
			@Override
			protected void process(HttpGet request)
			{
				addParameter("foo", "bar");
				addParameter("abc", 123);
				request.addHeader(headerName, headerValue);
			}
		};
		auth.processRequest(request);
		String uri = request.getURI().toString();
		
		assertEquals("http://example.com?foo=bar&abc=123", uri);
		assertEquals(headerName + ": " + headerValue, request.getFirstHeader(headerName).toString());
	}
}
