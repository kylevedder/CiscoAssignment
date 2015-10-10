package cisco.assignment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cisco.assignment.exception.EntryNotFoundException;
import cisco.assignment.exception.InvalidURIException;
import cisco.assignment.exception.UIDMismatchException;
import cisco.assignment.exception.UnsupportedMethodException;
import cisco.assignment.model.ErrorObject;

//https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
@ControllerAdvice
public class ExceptionController {

	private static final Logger logger = Logger.getLogger(InputController.class);

	// http://stackoverflow.com/questions/17201072/using-spring-mvc-accepting-post-requests-with-bad-json-leads-to-a-default-400-e
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorObject badMessage(HttpServletRequest request) {
		logger.error("Payload not JSON");
		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(),
				"Not a JSON Object");
	}

	@ExceptionHandler({ InvalidURIException.class, UIDMismatchException.class, EntryNotFoundException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorObject dumpExceptionBadRequest(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		logger.error(request.getMethod() + ": " + ex.getMessage());
		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(), ex.getMessage());
	}

	@ExceptionHandler({ UnsupportedMethodException.class })
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ResponseBody
	public ErrorObject dumpExceptionNotImplemented(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		logger.error(request.getMethod() + ": " + ex.getMessage());
		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(), ex.getMessage());
	}

}
