package cisco.assignment.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import cisco.assignment.exception.InvalidDataException;
import cisco.assignment.model.URLListObject;

public class URLListObjectDeserializer extends JsonDeserializer<URLListObject> {

	@Override
	public URLListObject deserialize(JsonParser jsonParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
			List<String> urls = new ArrayList<>();
			while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
				String url = jsonParser.getValueAsString();
				if (url != null) {
					urls.add(url);
				}
			}
			return new URLListObject(urls);
		} else {
			throw new InvalidDataException();
		}
	}
}
