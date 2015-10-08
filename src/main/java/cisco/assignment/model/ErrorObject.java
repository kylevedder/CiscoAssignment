package cisco.assignment.model;

public class ErrorObject {
	private String verb;
	private String url;
	private String message;

	public ErrorObject(String verb, String url, String message) {
		this.verb = verb;
		this.url = url;
		this.message = message;
	}
}
