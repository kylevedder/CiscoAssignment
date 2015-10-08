package cisco.assignment.util;

import org.springframework.stereotype.Component;

@Component
public class URLUtilsImpl implements URLUtils {
	@Override
	public String appendURI(String url, String uri) {
		if (!url.endsWith("/"))
			url = url.concat("/");
		url = url.concat(uri);
		return url;
	}

}
