package cisco.assignment.serializer;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import cisco.assignment.exception.InvalidDataException;
import cisco.assignment.model.DataObject;

/**
 * Deserializes JSON to {@link DataObject}.
 * 
 * Referenced in annotation of {@link DataObject}.
 * 
 * @author kyle
 *
 */
public class DataObjectDeserializer extends JsonDeserializer<DataObject> {


	@Override
	public DataObject deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		// gets a map of data as String, Object map
		Map<String, Object> dataMap = jp.readValueAs(new TypeReference<Map<String, Object>>() {
		});
		Object uidVal = dataMap.remove("uid");
		if (uidVal == null || !(uidVal instanceof String)) {
			throw new InvalidDataException();
		}
		String uid = String.valueOf(uidVal);

		return new DataObject(uid, dataMap);
	}
}