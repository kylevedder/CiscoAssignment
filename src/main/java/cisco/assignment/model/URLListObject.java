package cisco.assignment.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cisco.assignment.serializer.URLListObjectSerializer;

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
}
