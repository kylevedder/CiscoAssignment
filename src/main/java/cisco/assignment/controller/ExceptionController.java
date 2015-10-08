package cisco.assignment.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import cisco.assignment.model.ErrorObject;

@Controller
public class ExceptionController {
//	@RequestMapping(value = "/error", produces = "application/json")
//	public ErrorObject error(HttpServletRequest request) {
//		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(), "ERROR!!!");
//	}

//	@ExceptionHandler(HttpMessageNotReadableException.class)
//	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Bad Request")
//	public ErrorObject badMessage(HttpServletRequest request) {
//
//		return new ErrorObject(request.getMethod().toUpperCase(), request.getRequestURL().toString(),
//				"Not a JSON Object");
//	}
}
