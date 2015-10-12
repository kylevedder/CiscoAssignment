package cisco.assignment.controller;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import cisco.assignment.App;
import cisco.assignment.exception.EntryNotFoundException;
import cisco.assignment.exception.InvalidDataException;
import cisco.assignment.exception.InvalidURIException;
import cisco.assignment.model.DataObject;
import cisco.assignment.model.ErrorObject;
import cisco.assignment.model.URLListObject;
import cisco.assignment.repo.DataRepo;
import cisco.assignment.util.URLUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@ComponentScan(basePackages = "cisco.assignment")
@IntegrationTest("server.port:0")
@WebAppConfiguration
public class APITests {

	private URLUtils urlUtils;

	@Value("${local.server.port}")
	private int port;

	@Value("${api-uri}")
	private String apiuri;

	@Autowired
	private DataRepo dataRepo;

	String compoundUrl = null;

	String baseUrl;
	private TestRestTemplate restTemplate = new TestRestTemplate();

	private Random r = null;

	/**
	 * Generates a random UID of upper case letters, lower case letters, and
	 * nums.
	 * 
	 * @return
	 */
	private String generateRandomUID() {
		int len = r.nextInt(5) + 15;
		String uid = "";
		for (int i = 0; i < len; i++) {
			int rand = r.nextInt(26 * 2 + 10);
			if (rand < 10) {// nums 0 thru 9
				uid += String.valueOf(rand);
			} else if (rand < 26 + 10) {// a thru z
				uid += String.valueOf((char) ('a' + (rand - 10)));
			} else if (rand < 26 * 2 + 10) {// A thru Z
				uid += String.valueOf((char) ('A' + (rand - 36)));
			}
		}
		return uid;
	}

	/**
	 * Sends a request to the specified URL with the specified payload, using
	 * the specified method, and returning a specified class.
	 * 
	 * Horray for generics!
	 * 
	 * @param requestPayload
	 * @param requestURL
	 * @param requestMethod
	 * @param responseClass
	 * @return
	 */
	private <S, T> T sendRequest(S requestPayload, String requestURL, HttpMethod requestMethod,
			Class<T> responseClass) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<S> requestEntity = new HttpEntity<>(requestPayload, headers);
		ResponseEntity<T> responseEntity = restTemplate.exchange(requestURL, requestMethod, requestEntity,
				responseClass);
		return responseEntity.getBody();
	}

	@Before
	public void setup() {
		urlUtils = new URLUtils();
		baseUrl = "http://localhost:" + port + "/";
		r = new Random(System.currentTimeMillis());
		compoundUrl = urlUtils.appendURI(baseUrl, apiuri);
		dataRepo.deleteAll();
	}

	@After
	public void teardown() {
		dataRepo.deleteAll();
	}

	/**
	 * Performs a POST and then checks that the response is the same, but with a
	 * "uid" field
	 * 
	 * @throws Exception
	 */
	@Test
	public void postCheckResponse() throws Exception {

		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);
		// ======
		// Setup POST
		// ======

		DataObject postResponse = sendRequest(sampleData, compoundUrl, HttpMethod.POST, DataObject.class);

		// ======
		// Check POST
		// ======
		// got a response
		assertNotNull(postResponse);
		assertNotNull(postResponse.getUid());
		assertNotNull(postResponse.getData());

		// generate compare data
		DataObject sampleDataObj = new DataObject(postResponse.getUid(), sampleData);

		// compare against known good
		assertEquals(postResponse, sampleDataObj);
	}

	/**
	 * Performs a POST and then checks that the response is stored in the
	 * database correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postCheckDataInsertion() throws Exception {

		// ======
		// Setup Data
		// ======
		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		// ======
		// Setup POST
		// ======

		DataObject postResponse = sendRequest(sampleData, compoundUrl, HttpMethod.POST, DataObject.class);

		// ======
		// Check POST
		// ======

		// got a response
		assertNotNull(postResponse);
		assertNotNull(postResponse.getUid());
		assertNotNull(postResponse.getData());

		// generate sample
		DataObject sampleDataObj = new DataObject(postResponse.getUid(), sampleData);

		// compare response against sample
		assertEquals(postResponse, sampleDataObj);

		// ======
		// Check database
		// ======

		// pull data from database
		DataObject databaseRecord = dataRepo.findByUid(postResponse.getUid());

		// check that database record matches posted response
		assertEquals(postResponse, databaseRecord);
	}

	/**
	 * Performs a PUT and then checks that the response is the same.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putBasic() throws Exception {

		// ======
		// Setup Data
		// ======

		String sampleUID = generateRandomUID();
		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		DataObject sampleDataObject = new DataObject(sampleUID, sampleData);

		// ======
		// Send PUT
		// ======

		DataObject putResponse = sendRequest(sampleDataObject, urlUtils.appendURI(compoundUrl, sampleUID),
				HttpMethod.PUT, DataObject.class);

		// ======
		// Check PUT
		// ======
		assertNotNull(putResponse);
		assertNotNull(putResponse.getUid());
		assertNotNull(putResponse.getData());

		// compare against known good
		assertEquals(putResponse, sampleDataObject);
	}

	/**
	 * Performs a PUT and then checks that the data in the database is the same.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putAndGet() throws Exception {

		// ======
		// Setup Data
		// ======

		String sampleUID = generateRandomUID();
		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		DataObject sampleDataObject = new DataObject(sampleUID, sampleData);

		// ======
		// Send PUT
		// ======

		DataObject putResponse = sendRequest(sampleDataObject, urlUtils.appendURI(compoundUrl, sampleUID),
				HttpMethod.PUT, DataObject.class);

		// ======
		// Check PUT
		// ======

		// got a response
		assertNotNull(putResponse);
		assertNotNull(putResponse.getUid());
		assertNotNull(putResponse.getData());

		// compare against known good
		assertEquals("PUT response does not match the expected response", putResponse, sampleDataObject);

		// ======
		// Check in database
		// ======
		DataObject databaseRecord = dataRepo.findByUid(sampleUID);

		// compare put
		assertEquals("PUT response does not match the database record", putResponse, databaseRecord);
	}

	/**
	 * Tests that PUT is idempotent.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putIdempotent() throws Exception {

		// ======
		// Setup Data
		// ======

		String sampleUID = generateRandomUID();
		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		DataObject sampleDataObject = new DataObject(sampleUID, sampleData);

		// ======
		// Setup PUT
		// ======
		DataObject putResponse1 = sendRequest(sampleDataObject, urlUtils.appendURI(compoundUrl, sampleUID),
				HttpMethod.PUT, DataObject.class);
		DataObject putResponse2 = sendRequest(sampleDataObject, urlUtils.appendURI(compoundUrl, sampleUID),
				HttpMethod.PUT, DataObject.class);

		// ======
		// Check PUT
		// ======

		// got a response
		assertNotNull(putResponse1);
		assertNotNull(putResponse1.getUid());
		assertNotNull(putResponse1.getData());

		assertNotNull(putResponse2);
		assertNotNull(putResponse2.getUid());
		assertNotNull(putResponse2.getData());

		// compare against known good
		assertEquals("PUT response does not match sample response", putResponse1, sampleDataObject);
		assertEquals("PUT is not idempotent", putResponse1, putResponse2);
	}

	/**
	 * Performs a PUT and then checks that the response is the same.
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteTest() throws Exception {

		// ======
		// Setup Data
		// ======

		String sampleUID = generateRandomUID();
		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		DataObject sampleDataObject = new DataObject(sampleUID, sampleData);

		dataRepo.save(sampleDataObject);
		// ======
		// Send PUT
		// ======

		sendRequest(sampleDataObject, urlUtils.appendURI(compoundUrl, sampleUID), HttpMethod.DELETE, DataObject.class);

		// ======
		// Check PUT
		// ======

		DataObject databaseRecord = dataRepo.findByUid(sampleUID);

		assertNull("Database still holds UID", databaseRecord);
	}

	/**
	 * Tests that GET of all URLs works by populating database with data and
	 * then GETing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAll() throws Exception {
		// ======
		// Setup Data
		// ======

		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		DataObject s1 = new DataObject(generateRandomUID(), sampleData);
		DataObject s2 = new DataObject(generateRandomUID(), sampleData);
		DataObject s3 = new DataObject(generateRandomUID(), sampleData);

		URLListObject sampleResponse = new URLListObject(Arrays.asList(urlUtils.appendURI(compoundUrl, s1.getUid()),
				urlUtils.appendURI(compoundUrl, s2.getUid()), urlUtils.appendURI(compoundUrl, s3.getUid())));

		// populate database with items
		dataRepo.save(s1);
		dataRepo.save(s2);
		dataRepo.save(s3);

		// ======
		// Send GET
		// ======
		URLListObject getResponse = sendRequest(s3, compoundUrl, HttpMethod.GET, URLListObject.class);

		// ======
		// Check GET
		// ======

		// got a response
		assertNotNull(getResponse);

		// compare against known good
		assertEquals(getResponse, sampleResponse);
	}

	/**
	 * Performs a POST, then PUT, then GET, then DELETEs the entity, each time
	 * polling the database to ensure the data is there.
	 * 
	 * @throws Exception
	 */
	@Test
	public void fullLoopTest() throws Exception {

		// ======
		// Setup Data
		// ======
		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		// ======
		// POST Data
		// ======
		DataObject postResponse = sendRequest(sampleData, compoundUrl, HttpMethod.POST, DataObject.class);

		assertNotNull("POST returned null response", postResponse);
		assertNotNull("POST response uid is null", postResponse.getUid());

		DataObject databaseRecordPost = dataRepo.findByUid(postResponse.getUid());

		assertEquals("Record in database does not match the record returned by POST.", postResponse,
				databaseRecordPost);

		// ======
		// PUT Data
		// ======
		DataObject samplePayload = new DataObject(postResponse.getUid(), sampleData);
		DataObject putResponse = sendRequest(samplePayload, urlUtils.appendURI(compoundUrl, postResponse.getUid()),
				HttpMethod.PUT, DataObject.class);

		DataObject databaseRecordPut = dataRepo.findByUid(postResponse.getUid());

		assertNotNull("PUT response was null", putResponse);
		assertEquals("PUT response not as expected", putResponse, samplePayload);
		assertEquals("PUT response does not match database", putResponse, databaseRecordPut);

		// ======
		// GET Data
		// ======

		DataObject getResponse = sendRequest("", urlUtils.appendURI(compoundUrl, postResponse.getUid()), HttpMethod.GET,
				DataObject.class);

		DataObject databaseRecordGet = dataRepo.findByUid(postResponse.getUid());

		assertNotNull("PUT response was null", getResponse);
		assertEquals("PUT response not as expected", getResponse, samplePayload);
		assertEquals("PUT response does not match database", getResponse, databaseRecordGet);

		// ======
		// DELETE Data
		// ======

		sendRequest(samplePayload, urlUtils.appendURI(compoundUrl, samplePayload.getUid()), HttpMethod.DELETE,
				DataObject.class);

		DataObject databaseRecordDelete = dataRepo.findByUid(postResponse.getUid());
		assertNull(databaseRecordDelete);
	}

	/**
	 * GETs a non-existent entity to ensure it throws an appropriate error.
	 * 
	 * @throws Exception
	 */
	@Test
	public void errorGet() throws Exception {

		String uid = generateRandomUID();
		ErrorObject errorResponse = sendRequest("", urlUtils.appendURI(compoundUrl, uid), HttpMethod.GET,
				ErrorObject.class);

		ErrorObject sampleErrorResponse = new ErrorObject("GET", urlUtils.appendURI(compoundUrl, uid),
				new EntryNotFoundException().getMessage());

		assertNotNull(errorResponse);
		assertEquals(errorResponse, sampleErrorResponse);
	}

	/**
	 * POSTs empty payload to ensure it throws an appropriate error.
	 * 
	 * @throws Exception
	 */
	@Test
	public void errorPost() throws Exception {

		String uid = generateRandomUID();
		ErrorObject errorResponse = sendRequest("", compoundUrl, HttpMethod.POST, ErrorObject.class);

		ErrorObject sampleErrorResponse = new ErrorObject("POST", compoundUrl, new InvalidDataException().getMessage());

		assertNotNull(errorResponse);
		assertEquals(errorResponse, sampleErrorResponse);
	}

	/**
	 * GETs, POSTs, and DELETEs to invalid URIs to ensure it throws an
	 * appropriate error.
	 * 
	 * @throws Exception
	 */
	@Test
	public void errorBadURI() throws Exception {

		String randomURL = urlUtils.appendURI(baseUrl, generateRandomUID());
		ErrorObject getResponse = sendRequest("", randomURL, HttpMethod.GET, ErrorObject.class);
		ErrorObject postResponse = sendRequest("", randomURL, HttpMethod.POST, ErrorObject.class);
		ErrorObject deleteResponse = sendRequest("", randomURL, HttpMethod.DELETE, ErrorObject.class);

		ErrorObject sampleGetResponse = new ErrorObject("GET", randomURL, new InvalidURIException().getMessage());
		ErrorObject samplePostResponse = new ErrorObject("POST", randomURL, new InvalidURIException().getMessage());
		ErrorObject sampleDeleteResponse = new ErrorObject("DELETE", randomURL, new InvalidURIException().getMessage());

		assertNotNull(getResponse);
		assertNotNull(postResponse);
		assertNotNull(deleteResponse);

		assertEquals("GET response does not match sample response", getResponse, sampleGetResponse);
		assertEquals("POST response does not match sample response", postResponse, samplePostResponse);
		assertEquals("DELETE response does not match sample response", deleteResponse, sampleDeleteResponse);
	}

}