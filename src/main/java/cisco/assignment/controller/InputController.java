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

import cisco.assignment.database.Database;
import cisco.assignment.exception.EntryNotFoundException;
import cisco.assignment.exception.InvalidURIException;
import cisco.assignment.exception.UIDMismatchException;
import cisco.assignment.exception.UnsupportedMethodException;
import cisco.assignment.model.DataObject;
import cisco.assignment.model.URLListObject;
import cisco.assignment.util.URLUtils;

@RestController
public class InputController {

	private static final Logger logger = Logger.getLogger(InputController.class);

	@Autowired
	private Database database;
	@Autowired
	private URLUtils urlUtils;

	/**
	 * Maps POSTs from the ${api-uri} to insert payload as a new entry.
	 * 
	 * If payload is valid JSON, returns the payload along with a "uid" field
	 * detailing the unique id for that payload.
	 * 
	 * If payload is valid JSON, Returns error payload.
	 * 
	 * @param data
	 *            Map of all nested data in key-value form, pulled out of the
	 *            POSTed JSON by Spring
	 * @return Initial POSTed payload along with a "uid" field detailing the
	 *         unique id or error payload
	 */
	@RequestMapping(value = "${api-uri}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public DataObject post(@RequestBody Map<String, Object> data) {
		logger.debug("POST: Create new entry");
		return database.insert(new DataObject(data));
	}

	/**
	 * Maps PUTs from the ${api-uri}/{uid} to insert payload as an entry with
	 * the given "uid".
	 * 
	 * Will overwrite existing entry at the given "uid" if one exists.
	 * 
	 * If valid JSON, returns PUTed payload.
	 * 
	 * If invalid JSON or if "uid" in PUTed payload does not match the "uid" in
	 * the URI, returns error payload.
	 * 
	 * @param uid
	 *            "uid" from the passed URI
	 * @param data
	 *            Map of all nested data in key-value form, pulled out of the
	 *            PUTed JSON by Spring
	 * @returnInitial PUTed payload or error payload
	 */
	@RequestMapping(value = "${api-uri}/{uid}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public DataObject put(@PathVariable String uid, @RequestBody Map<String, Object> data) {
		logger.debug("PUT: Add new entry with uid \"" + uid + "\"");
		// Checks that the uid of the URL is the same as the uid in the map
		// Has the intended side effect of also removing the uid from the
		// dataset, which is desired for proper serialization
		if (!data.remove("uid", uid)) {
			logger.error("PUT: Mismatch in entry UIDs");
			throw new UIDMismatchException();
		}
		return database.save(new DataObject(uid, data));
	}

	/**
	 * Maps GETs from the ${api-uri}/{uid} to pull entry at the given "uid".
	 * 
	 * If entry with the given "uid" exists, returns that entry.
	 * 
	 * If entry with the given "uid" is missing, throws an exception.
	 * 
	 * @param uid
	 *            "uid" of the entry to return
	 * @return Entry with requested "uid" or error payload from exception
	 */
	@RequestMapping(value = "${api-uri}/{uid}", method = RequestMethod.GET, produces = "application/json")
	public DataObject get(@PathVariable String uid) {
		logger.debug("GET: Getting entry with uid \"" + uid + "\"");
		DataObject dataObject = database.findByUid(uid);
		if (dataObject == null) {
			logger.error("GET: Requested entry with UID \"" + uid + "\" not found!");
			throw new EntryNotFoundException();
		}
		return dataObject;
	}

	/**
	 * Maps GETs from the ${api-uri} to list all entry URLs
	 * 
	 * Returns a payload of an array of all entry URLs
	 * 
	 * @param request
	 * 
	 * @return Payload of all entry URLs
	 */
	@RequestMapping(value = "${api-uri}", method = RequestMethod.GET, produces = "application/json")
	public URLListObject getAll(HttpServletRequest request) {
		logger.debug("GET: List unique entries");
		String baseUrl = request.getRequestURL().toString();
		List<String> urls = new ArrayList<>();
		for (DataObject dataObj : database.findAll()) {
			urls.add(urlUtils.appendURI(baseUrl, dataObj.getUid()));
		}
		return new URLListObject(urls);
	}

	/**
	 * Maps GETs from the ${api-uri}/{uid} delete the entry with the specified
	 * uid.
	 * 
	 * If entry with the given "uid" exists, deletes the entry and returns
	 * nothing.
	 * 
	 * if entry with the given "uid" doesn't exist, throws an exception.
	 * 
	 * @param uid
	 *            "uid" of the entry to delete
	 */
	@RequestMapping(value = "${api-uri}/{uid}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String uid) {
		logger.debug("DELETE: Deleting entry with uid \"" + uid + "\"");
		if (database.findByUid(uid) == null) {
			logger.error("DELETE: Requested UID \"" + uid + "\" not found!");
			throw new EntryNotFoundException();
		}
		database.delete(uid);
	}

	/**
	 * Maps all requests not captured by other handlers to an appropriate
	 * exception.
	 * 
	 * If GET, POST, DELETE, or PUT, throws an InvalidURIException, otherwise
	 * throws an UnsupportedMethodException.
	 * 
	 * @param request
	 *            HTTP Request object
	 * @param response
	 *            HTTP Response object
	 */
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
