package cisco.assignment.model;

import java.util.Map;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cisco.assignment.serializer.DataObjectDeserializer;
import cisco.assignment.serializer.DataObjectSerializer;

@JsonDeserialize(using = DataObjectDeserializer.class)
@JsonSerialize(using = DataObjectSerializer.class)
public class DataObject {

	// https://spring.io/guides/gs/accessing-data-mongodb/
	@Id
	private String uid;
	private Map<String, Object> data;

	public DataObject() {
	}

	public DataObject(String uid, Map<String, Object> data) {
		this.uid = uid;
		this.data = data;
	}

	public DataObject(Map<String, Object> data) {
		this(null, data);
	}

	public String getUid() {
		return uid;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataObject)) {
			return false;
		}
		DataObject dataObj = (DataObject) obj;
		// compare the equality of the uid and the data
		return (this.getUid().equals(dataObj.getUid()) && this.getData().equals(dataObj.getData()));
	}

}
