package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UIDMismatchException extends RuntimeException {
	private String urlId;
	private String dataId;

	public UIDMismatchException(String urlId, String dataId) {
		this.urlId = urlId;
		this.dataId = dataId;
	}

	@Override
	public String getMessage() {
		// return "UID in message \"" + dataId + "\" did not match UID in URL
		// \"" + urlId + "\"";
		return "UID mismatch";
	}
}
