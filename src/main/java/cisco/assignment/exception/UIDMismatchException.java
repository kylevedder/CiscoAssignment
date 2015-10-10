package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UIDMismatchException extends RuntimeException {

	public UIDMismatchException() {
	}

	@Override
	public String getMessage() {
		return "UID mismatch";
	}
}
