package cisco.assignment.model;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cisco.assignment.serializer.DataObjectSerializer;

@JsonSerialize(using = DataObjectSerializer.class)
public class DataObject {
	private final long uid;
	private final Map<String, Object> data;

	public DataObject(long uid, Map<String, Object> data) {		
		this.uid = uid;
		this.data = data;
	}

	public long getUid() {
		return uid;
	}

	public Map<String, Object> getData() {
		return data;
	}

}
