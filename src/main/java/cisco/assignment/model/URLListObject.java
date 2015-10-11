package cisco.assignment.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cisco.assignment.serializer.URLListObjectDeserializer;
import cisco.assignment.serializer.URLListObjectSerializer;

@JsonDeserialize(using = URLListObjectDeserializer.class)
@JsonSerialize(using = URLListObjectSerializer.class)
public class URLListObject {
	private List<String> urlList;

	public URLListObject() {

	}

	public URLListObject(List<String> urlList) {
		this.urlList = urlList;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof URLListObject)) {
			return false;
		}
		URLListObject ulo = (URLListObject) obj;
		return this.getUrlList().equals(ulo.getUrlList());
	}
}
