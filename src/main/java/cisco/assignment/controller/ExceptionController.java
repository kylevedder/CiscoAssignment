package cisco.assignment.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cisco.assignment.exception.EntryNotFoundException;
import cisco.assignment.exception.GenericErrorException;
import cisco.assignment.exception.InvalidURIException;
import cisco.assignment.exception.UIDMismatchException;
import cisco.assignment.exception.UnsupportedMethodException;
import cisco.assignment.model.ErrorObject;

//https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
@ControllerAdvice
public class ExceptionController {
	// http://stackoverflow.com/questions/17201072/using-spring-mvc-accepting-post-requests-with-bad-json-leads-to-a-default-400-e
	@ExceptionHandler({ HttpMessageNotReadableException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorObject badMessage(HttpServletRequest request) {
		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(),
				"Not a JSON Object");
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorObject requestNotSupported(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(),
				"Improper request method: " + ex.getMethod());
	}

	@ExceptionHandler(value = { UnsupportedMethodException.class, InvalidURIException.class,
			GenericErrorException.class, UIDMismatchException.class, EntryNotFoundException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorObject customExceptions(HttpServletRequest request, Exception ex) {
		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(), ex.getMessage());
	}

}
