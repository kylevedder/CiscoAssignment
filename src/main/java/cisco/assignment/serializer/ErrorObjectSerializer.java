package cisco.assignment.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import cisco.assignment.model.ErrorObject;

public class ErrorObjectSerializer extends JsonSerializer<ErrorObject> {
	@Override
	public void serialize(ErrorObject dataObj, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("verb", dataObj.getVerb());
		jgen.writeStringField("url", dataObj.getUrl());
		jgen.writeStringField("message", dataObj.getMessage());
		jgen.writeEndObject();
	}
}