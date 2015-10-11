package cisco.assignment.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class URLUtilsTest {
	
	URLUtils urlUtils;
	
	@Before
	public void setup()
	{
		urlUtils = new URLUtilsImpl();
	}

	@Test
	public void appendNoSlash() {
		String sampleURL = "my.url/my";
		String sampleURI = "sample/uri";

		String appendedURL = sampleURL + "/" + sampleURI;

		assertEquals(appendedURL, urlUtils.appendURI(sampleURL, sampleURI));
	}
	
	@Test
	public void appendURISlash() {
		String sampleURL = "my.url/my";
		String sampleURI = "/sample/uri";

		String appendedURL = sampleURL + sampleURI;

		assertEquals(appendedURL, urlUtils.appendURI(sampleURL, sampleURI));
	}
	
	@Test
	public void appendURILSlash() {
		String sampleURL = "my.url/my/";
		String sampleURI = "sample/uri";

		String appendedURL = sampleURL + sampleURI;

		assertEquals(appendedURL, urlUtils.appendURI(sampleURL, sampleURI));
	}

}
