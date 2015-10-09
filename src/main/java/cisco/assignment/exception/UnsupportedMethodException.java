package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnsupportedMethodException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Unsupported operation";
	}
}