package cisco.assignment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cisco.assignment.exception.EntryNotFoundException;
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

	private static final Logger logger = Logger.getLogger(InputController.class);

	@Autowired
	private DataRepo dataRepo;
	@Autowired
	private URLUtils urlUtils;

	@RequestMapping(value = "${api-uri}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public DataObject post(@RequestBody Map<String, Object> data) {
		logger.debug("POST: Create new entry");
		return dataRepo.insert(new DataObject(data));
	}

	@RequestMapping(value = "${api-uri}/{uid}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public DataObject put(@PathVariable String uid, @RequestBody Map<String, Object> data) {
		logger.debug("PUT: Add new entry with uid \"" + uid + "\"");
		if (!uid.equals(data.get("uid"))) {
			logger.error("PUT: Mismatch in entry UIDs");
			throw new UIDMismatchException();
		}
		return dataRepo.save(new DataObject(uid, data));
	}

	@RequestMapping(value = "${api-uri}/{uid}", method = RequestMethod.GET, produces = "application/json")
	public DataObject get(@PathVariable String uid) {
		logger.debug("GET: Getting entry with uid \"" + uid + "\"");
		DataObject dataObject = dataRepo.findByUid(uid);
		if (dataObject == null) {
			logger.error("GET: Requested entry with UID \"" + uid + "\" not found!");
			throw new EntryNotFoundException();
		}
		return dataObject;
	}

	@RequestMapping(value = "${api-uri}", method = RequestMethod.GET, produces = "application/json")
	public URLListObject getAll(HttpServletRequest request) {
		logger.debug("GET: List unique entries");
		String baseUrl = request.getRequestURL().toString();
		List<String> urls = new ArrayList<>();
		for (DataObject dataObj : dataRepo.findAll()) {
			urls.add(urlUtils.appendURI(baseUrl, dataObj.getUid()));
		}
		return new URLListObject(urls);
	}

	@RequestMapping(value = "${api-uri}/{uid}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String uid) {
		logger.debug("DELETE: Deleting entry with uid \"" + uid + "\"");
		if (dataRepo.findByUid(uid) == null) {
			logger.error("DELETE: Requested UID \"" + uid + "\" not found!");
			throw new EntryNotFoundException();
		}
		dataRepo.delete(uid);
	}

	@RequestMapping(method = {})
	public void unmappedOperations(HttpServletRequest request, HttpServletResponse response) {
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
}
