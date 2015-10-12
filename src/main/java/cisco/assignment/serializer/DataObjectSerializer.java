package cisco.assignment.serializer;

import java.io.IOException;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import cisco.assignment.model.DataObject;

/**
 * Serializes DataObject to JSON.
 * 
 * Referenced in annotation of {@link DataObject}.
 * 
 * @author kyle
 *
 */
public class DataObjectSerializer  extends JsonSerializer<DataObject> {
	@Override
	public void serialize(DataObject dataObj, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("uid", String.valueOf(dataObj.getUid()));		
		for(Entry<String, Object> entry : dataObj.getData().entrySet())
		{
			jgen.writeObjectField(entry.getKey(), entry.getValue());
		}
		jgen.writeEndObject();
	}
}