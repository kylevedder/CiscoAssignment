package cisco.assignment.serializer;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import cisco.assignment.model.DataObject;
import cisco.assignment.model.URLListObject;

public class SerializerTest {

	ObjectMapper objMapper;

	@Before
	public void setup() {
		objMapper = new ObjectMapper();
	}

	/**
	 * Serializes and deserializes a {@link DataObject} and then compares the two
	 * {@link DataObject}s
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDataObject() throws Exception {
		String uid = "sampleuid";
		Map<String, Object> sampleData = new HashMap<>();
		sampleData.put("I", "would");
		sampleData.put("like", "to");

		Map<String, Object> sampleDataSub = new HashMap<>();
		sampleDataSub.put("work", "for");
		sampleDataSub.put("Cisco", "StartUp");
		sampleData.put("please", sampleDataSub);

		DataObject dataObject = new DataObject(uid, sampleData);

		// serialize
		String deserializedString = objMapper.writeValueAsString(dataObject);
		// deserialize
		DataObject reformedDataObject = objMapper.readValue(deserializedString, DataObject.class);

		assertEquals(dataObject, reformedDataObject);

	}

	/**
	 * Serializes and deserializes a {@link URLListObject} and then compares the two {@link URLListObject}s
	 * @throws Exception
	 */
	@Test
	public void testURLListObject() throws Exception {

		List<String> urls = Arrays.asList("I", "want", "to", "work", "for", "Cisco", "Startup");

		URLListObject urlList = new URLListObject(urls);

		// serialize
		String deserializedString = objMapper.writeValueAsString(urlList);
		// deserialize
		URLListObject reformedUrlList = objMapper.readValue(deserializedString, URLListObject.class);

		assertEquals(urlList, reformedUrlList);

	}

}
