import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.io.Reader;
import org.junit.Before;
import org.junit.Test;
import pl.michalwa.freesoundclient.Freesound;

public class FreesoundTest
{
	String token;
	Freesound freesound;
	
	@Before
	public void setup()
	{
		readConfig();
		freesound = Freesound.builder()
		 		.withToken(token)
				.build();
	}
	
	@Test
	public void simpleRequestTest()
	{
		JsonObject json = freesound.makeRequest("https://freesound.org/apiv2/sounds/1234/");
		System.out.println(json.toString());
	}
	
	private void readConfig()
	{
		Reader reader = new InputStreamReader(getClass().getResourceAsStream("/config.json"));
		JsonObject config = new JsonParser().parse(reader).getAsJsonObject();
		
		token = config.get("token").getAsString();
	}
}