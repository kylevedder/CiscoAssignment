package cisco.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Exception thrown when an unsupported method is used, i.e. HEAD, PATCH, etc.
 * @author kyle
 *
 */
@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
public class UnsupportedMethodException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Unsupported method";
	}
}