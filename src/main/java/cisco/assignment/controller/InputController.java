package cisco.assignment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cisco.assignment.exception.EntryNotFoundException;
import cisco.assignment.exception.GenericErrorException;
import cisco.assignment.exception.InvalidURIException;
import cisco.assignment.exception.UIDMismatchException;
import cisco.assignment.exception.UnsupportedMethodException;
import cisco.assignment.model.DataObject;
import cisco.assignment.model.URLListObject;
import cisco.assignment.repo.DataRepo;
import cisco.assignment.util.URLUtils;

//http://www.davismol.net/2015/06/05/jackson-using-jsonserialize-or-jsondeserialize-annotation-to-rsegister-a-custom-serializer-or-deserializer/
@RestController
public class InputController {

	@Autowired
	private DataRepo dataRepo;
	@Autowired
	private URLUtils urlUtils;

	@RequestMapping(method = { })
	public void unmappedOperations(HttpServletRequest request) {
		switch (request.getMethod().toUpperCase()) {
		case "GET":
		case "POST":
		case "DELETE":
		case "PUT":
			throw new InvalidURIException();
		default:
			throw new UnsupportedMethodException();
		}
	}

	// http://stackoverflow.com/questions/7312436/spring-mvc-how-to-get-all-request-params-in-a-map-in-spring-controller/18489124#18489124
	@RequestMapping(value = "/api/objects", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public DataObject post(@RequestBody Map<String, Object> data) {
		return dataRepo.insert(new DataObject(data));
	}

	@RequestMapping(value = "/api/objects/{uid}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public DataObject put(@PathVariable String uid, @RequestBody Map<String, Object> data) {
		if (!uid.equals(data.get("uid"))) {
			throw new UIDMismatchException(uid, String.valueOf(data.get("uid")));
		}
		return dataRepo.save(new DataObject(uid, data));
	}

	@RequestMapping(value = "/api/objects/{uid}", method = RequestMethod.GET, produces = "application/json")
	public DataObject get(@PathVariable String uid) {
		DataObject dataObject = dataRepo.findByUid(uid);
		if (dataObject == null) {
			throw new EntryNotFoundException(uid);
		}
		return dataObject;
	}

	@RequestMapping(value = "/api/objects", method = RequestMethod.GET, produces = "application/json")
	public URLListObject getAll(HttpServletRequest request) {
		String baseUrl = request.getRequestURL().toString();
		List<String> urls = new ArrayList<>();
		for (DataObject dataObj : dataRepo.findAll()) {
			urls.add(urlUtils.appendURI(baseUrl, dataObj.getUid()));
		}
		return new URLListObject(urls);
	}

	@RequestMapping(value = "/api/objects/{uid}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String uid) {
		dataRepo.delete(uid);
	}

	// @RequestMapping(value = "/error")
	// public void error(HttpServletRequest request) {
	//// throw new GenericErrorException();
	// }

}
