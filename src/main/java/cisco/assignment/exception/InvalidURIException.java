package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidURIException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Invalid URI requested";
	}
}