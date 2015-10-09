package cisco.assignment.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cisco.assignment.serializer.ErrorObjectSerializer;

@JsonSerialize(using = ErrorObjectSerializer.class)
public class ErrorObject {
	private String verb;
	private String url;
	private String message;

	public ErrorObject(String verb, String url, String message) {
		this.verb = verb;
		this.url = url;
		this.message = message;
	}

	public String getVerb() {
		return verb;
	}

	public String getUrl() {
		return url;
	}

	public String getMessage() {
		return message;
	}
}
