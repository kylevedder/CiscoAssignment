package cisco.assignment.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class URLUtilsTest {

	URLUtils urlUtils;

	@Before
	public void setup() {
		urlUtils = new URLUtils();
	}

	/**
	 * Tests that neither has slash
	 */
	@Test
	public void appendNoSlash() {
		String sampleURL = "ilove.cisco/my";
		String sampleURI = "sample/uri";

		String appendedURL = sampleURL + "/" + sampleURI;

		assertEquals(appendedURL, urlUtils.appendURI(sampleURL, sampleURI));
	}

	/**
	 * Tests that URI has leading.
	 */
	@Test
	public void appendURISlash() {
		String sampleURL = "ilove.cisco/my";
		String sampleURI = "/sample/uri";

		String appendedURL = sampleURL + sampleURI;

		assertEquals(appendedURL, urlUtils.appendURI(sampleURL, sampleURI));
	}

	/**
	 * Tests that URL has trailing.
	 */
	@Test
	public void appendURLSlash() {
		String sampleURL = "ilove.cisco/my/";
		String sampleURI = "sample/uri";

		String appendedURL = sampleURL + sampleURI;

		assertEquals(appendedURL, urlUtils.appendURI(sampleURL, sampleURI));
	}

	/**
	 * Tests both slash
	 */
	@Test
	public void appendURIOnlySlash() {
		String sampleURL = "ilove.cisco/my/";
		String sampleURI = "/";

		String appendedURL = sampleURL;

		assertEquals(appendedURL, urlUtils.appendURI(sampleURL, sampleURI));
	}

}
