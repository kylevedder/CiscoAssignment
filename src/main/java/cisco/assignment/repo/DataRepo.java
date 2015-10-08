package cisco.assignment.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import cisco.assignment.model.DataObject;

public interface DataRepo extends MongoRepository<DataObject, String> {
	public DataObject findByUid(String uid);
}
