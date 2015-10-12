package cisco.assignment.exception;

/**
 * Exception thrown when payload cannot be parsed as JSON.
 * 
 * @author kyle
 *
 */
public class InvalidDataException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Not a JSON Object";
	}
}
