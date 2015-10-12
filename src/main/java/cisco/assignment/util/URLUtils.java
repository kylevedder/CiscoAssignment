package cisco.assignment.util;

import org.springframework.stereotype.Component;

@Component
public class URLUtils {

	/**
	 * Takes a URL and URI and appends them appropriately, regardless of leading
	 * and trailing slashes.
	 * 
	 * @param url
	 *            Base URL
	 * @param uri
	 *            URI to append
	 * @return
	 */
	public String appendURI(String url, String uri) {
		if (!url.endsWith("/") && !uri.startsWith("/"))
			url = url.concat("/");
		if (url.endsWith("/") && uri.startsWith("/"))
			uri = uri.substring(1);
		url = url.concat(uri);
		return url;
	}

}
