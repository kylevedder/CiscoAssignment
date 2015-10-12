package cisco.assignment.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
public class IntegrationTests {

	private URLUtils urlUtils;

	@Value("${local.server.port}")
	private int port;

	@Value("${api-uri}")
	private String apiuri;

	@Autowired
	private DataRepo dataRepo;

	private List<DataObject> starterData;

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
	private String randomUID() {
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

	private <S, T> T sendRequest(S sampleData, String url, HttpMethod method, Class<T> clazz) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<S> requestEntity = new HttpEntity<>(sampleData, headers);
		ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, clazz);
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
	public void postBasic() throws Exception {

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

		// generate compare data
		DataObject sampleDataObj = new DataObject(postResponse.getUid(), sampleData);

		// compare against known good
		assertEquals(postResponse, sampleDataObj);
	}

	/**
	 * Performs a POST and then checks that the response is stored in the
	 * database with a GET.
	 * 
	 * @throws Exception
	 */
	@Test
	public void postAndGet() throws Exception {

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

		// generate compare data
		DataObject sampleDataObj = new DataObject(postResponse.getUid(), sampleData);

		// compare against known good
		assertEquals(postResponse, sampleDataObj);

		// ======
		// Check GET
		// ======
		HttpHeaders getHeaders = new HttpHeaders();
		getHeaders.setContentType(MediaType.APPLICATION_JSON);

		DataObject getResponse = sendRequest(sampleData, urlUtils.appendURI(compoundUrl, postResponse.getUid()),
				HttpMethod.GET, DataObject.class);

		// check to see if request from database is same as the data POSTed
		assertEquals(postResponse, getResponse);
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

		String sampleUID = randomUID();
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
		assertEquals(putResponse, sampleDataObject);
	}

	/**
	 * Performs a PUT and then checks that the response from GET is the same.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putAndGet() throws Exception {

		// ======
		// Setup Data
		// ======

		String sampleUID = randomUID();
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
		assertEquals(putResponse, sampleDataObject);

		// ======
		// Check with GET from database
		// ======

		DataObject getResponse = sendRequest("", urlUtils.appendURI(compoundUrl, sampleUID), HttpMethod.GET,
				DataObject.class);

		assertEquals(sampleDataObject, getResponse);
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

		String sampleUID = randomUID();
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
		assertEquals(putResponse1, sampleDataObject);
		assertEquals(putResponse1, putResponse2);
	}

	/**
	 * Tests that GET of all URLs works
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

		DataObject s1 = new DataObject(randomUID(), sampleData);
		DataObject s2 = new DataObject(randomUID(), sampleData);
		DataObject s3 = new DataObject(randomUID(), sampleData);

		List<String> urls = Arrays.asList(urlUtils.appendURI(compoundUrl, s1.getUid()),
				urlUtils.appendURI(compoundUrl, s2.getUid()), urlUtils.appendURI(compoundUrl, s3.getUid()));

		URLListObject sampleResponse = new URLListObject(urls);

		sendRequest(s1, urlUtils.appendURI(compoundUrl, s1.getUid()), HttpMethod.PUT, DataObject.class);
		sendRequest(s2, urlUtils.appendURI(compoundUrl, s2.getUid()), HttpMethod.PUT, DataObject.class);
		sendRequest(s3, urlUtils.appendURI(compoundUrl, s3.getUid()), HttpMethod.PUT, DataObject.class);

		// ======
		// Setup GET
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
	 * Performs a PUT, DELETEs the PUT entity, and then GETs the deleted entity
	 * to ensure the entity doesn't exist.
	 * 
	 * @throws Exception
	 */
	@Test
	public void putDeleteGet() throws Exception {

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

		DataObject sampleDataObject = new DataObject(randomUID(), sampleData);

		DataObject putResponse = sendRequest(sampleDataObject,
				urlUtils.appendURI(compoundUrl, sampleDataObject.getUid()), HttpMethod.PUT, DataObject.class);

		assertNotNull(putResponse);

		// ======
		// Delete
		// ======

		sendRequest(sampleDataObject, urlUtils.appendURI(compoundUrl, sampleDataObject.getUid()), HttpMethod.DELETE,
				DataObject.class);

		ErrorObject errorResponse = sendRequest(sampleDataObject,
				urlUtils.appendURI(compoundUrl, sampleDataObject.getUid()), HttpMethod.GET, ErrorObject.class);

		assertNotNull(errorResponse);

		ErrorObject sampleErrorResponse = new ErrorObject("GET",
				urlUtils.appendURI(compoundUrl, sampleDataObject.getUid()), new EntryNotFoundException().getMessage());

		assertEquals(errorResponse, sampleErrorResponse);

	}

	/**
	 * GETs a non-existent entity to ensure it throws an appropriate error.
	 * 
	 * @throws Exception
	 */
	@Test
	public void errorGet() throws Exception {

		String uid = randomUID();
		ErrorObject errorResponse = sendRequest("", urlUtils.appendURI(compoundUrl, uid), HttpMethod.GET,
				ErrorObject.class);

		ErrorObject sampleErrorResponse = new ErrorObject("GET", urlUtils.appendURI(compoundUrl, uid),
				new EntryNotFoundException().getMessage());

		assertNotNull(errorResponse);
		assertEquals(errorResponse, sampleErrorResponse);
	}

	/**
	 * POSTs empty payload to ensure it throws an appropriate error.
	 * @throws Exception
	 */
	@Test
	public void errorPost() throws Exception {

		String uid = randomUID();
		ErrorObject errorResponse = sendRequest("", compoundUrl, HttpMethod.POST, ErrorObject.class);

		ErrorObject sampleErrorResponse = new ErrorObject("POST", compoundUrl, new InvalidDataException().getMessage());

		assertNotNull(errorResponse);
		assertEquals(errorResponse, sampleErrorResponse);
	}

	/**
	 * GETs, POSTs, and DELETEs to invalid URIs to ensure it throws an appropriate error.
	 * @throws Exception
	 */
	@Test
	public void errorBadURI() throws Exception {

		String randomURL = urlUtils.appendURI(baseUrl, randomUID());
		ErrorObject getResponse = sendRequest("", randomURL, HttpMethod.GET, ErrorObject.class);
		ErrorObject postResponse = sendRequest("", randomURL, HttpMethod.POST, ErrorObject.class);
		ErrorObject deleteResponse = sendRequest("", randomURL, HttpMethod.DELETE, ErrorObject.class);

		ErrorObject sampleGetResponse = new ErrorObject("GET", randomURL, new InvalidURIException().getMessage());
		ErrorObject samplePostResponse = new ErrorObject("POST", randomURL, new InvalidURIException().getMessage());
		ErrorObject sampleDeleteResponse = new ErrorObject("DELETE", randomURL, new InvalidURIException().getMessage());

		assertNotNull(getResponse);
		assertNotNull(postResponse);
		assertNotNull(deleteResponse);

		assertEquals(getResponse, sampleGetResponse);
		assertEquals(postResponse, samplePostResponse);
		assertEquals(deleteResponse, sampleDeleteResponse);
	}

}