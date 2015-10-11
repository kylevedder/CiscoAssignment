package cisco.assignment.model;

public class ErrorObject {
	private String verb;
	private String url;
	private String message;

	public ErrorObject() {
	}

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

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ErrorObject)) {
			return false;
		}
		ErrorObject eo = (ErrorObject) obj;
		return (this.getVerb().equals(eo.getVerb()) && this.getUrl().equals(eo.getUrl())
				&& this.getMessage().equals(eo.getMessage()));
	}
}
