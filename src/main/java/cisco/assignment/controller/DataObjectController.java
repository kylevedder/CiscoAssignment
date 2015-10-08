package cisco.assignment.controller;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cisco.assignment.model.DataObject;

//http://www.davismol.net/2015/06/05/jackson-using-jsonserialize-or-jsondeserialize-annotation-to-rsegister-a-custom-serializer-or-deserializer/
@RestController
public class DataObjectController {
	private final AtomicLong counter = new AtomicLong();

	// http://stackoverflow.com/questions/7312436/spring-mvc-how-to-get-all-request-params-in-a-map-in-spring-controller/18489124#18489124
	@RequestMapping(value = "/api/objects", method = RequestMethod.POST, consumes="application/json")
	public DataObject post(@RequestBody Map<String, Object> data) {
		System.out.println("LEN: " + data.size());
		return new DataObject(counter.incrementAndGet(), data);
	}
}
