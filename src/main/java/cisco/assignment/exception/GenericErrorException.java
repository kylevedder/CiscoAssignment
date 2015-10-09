package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericErrorException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Something went wrong. Generic error.";
	}
}
