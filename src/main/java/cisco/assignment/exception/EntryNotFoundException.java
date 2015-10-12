package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an entry does not exist in the database.
 * @author kyle
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntryNotFoundException extends RuntimeException {

	public EntryNotFoundException() {
	}

	@Override
	public String getMessage() {
		return "Entry not found";
	}
}
