package cisco.assignment.exception;

public class InvalidDataException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Not a JSON Object";
	}
}
