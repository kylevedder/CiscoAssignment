package cisco.assignment.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import cisco.assignment.model.DataObject;

/**
 * Data repository that interfaces with MongoDB. Yes, I did not write an
 * implementation for this, Spring implemented it for me. Thanks Spring!
 * 
 * @author kyle
 *
 */
public interface DataRepo extends MongoRepository<DataObject, String> {
	/**
	 * Returns an entry with the specified "uid"
	 * 
	 * This is implemented correctly by Spring simply because of the method
	 * name. So cool!
	 * 
	 * @param uid
	 *            "uid" of the entry to find
	 * @return DataObject representation of the entry with the specified "uid"
	 */
	public DataObject findByUid(String uid);
}
