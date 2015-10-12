package cisco.assignment.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import cisco.assignment.model.URLListObject;


/**
 * Serializes {@link URLListObject} to JSON.
 * 
 * Referenced in annotation of {@link URLListObject}.
 * 
 * @author kyle
 *
 */
public class URLListObjectSerializer extends JsonSerializer<URLListObject> {
	@Override
	public void serialize(URLListObject urlListObj, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeStartArray();
		for (String url : urlListObj.getUrlList()) {
			jgen.writeStartObject();
			jgen.writeObjectField("url", url);
			jgen.writeEndObject();
		}		
		jgen.writeEndArray();
	}
}
