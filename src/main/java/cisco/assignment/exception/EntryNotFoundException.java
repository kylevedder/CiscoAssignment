package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // 404
public class EntryNotFoundException extends RuntimeException {
	private String id;

	public EntryNotFoundException(String id) {
		this.id = id;
	}

	@Override
	public String getMessage() {
		//return "Entry \"" + id + "\" not found";
		return "Entry not found";
	}
}
