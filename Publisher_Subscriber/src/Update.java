import java.io.Serializable;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
/**
 * File: .java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */

public class Update implements Serializable{
	private ConcurrentHashMap<String,Topic> topics = new ConcurrentHashMap<String,Topic>();
	private ConcurrentHashMap<String,Publishers> publisher = new ConcurrentHashMap<String,Publishers>();
	private ConcurrentHashMap<String,Subscriber> subscriber = new ConcurrentHashMap<String,Subscriber>();
	//private ConcurrentHashMap<String,Vector<String>> topicList = new ConcurrentHashMap<String,Vector<String>>();

	private Vector<Msg> toPublish = new Vector<Msg>();
	
	public Update( ConcurrentHashMap<String,Topic> topics, ConcurrentHashMap<String,Publishers> publisher, ConcurrentHashMap<String,Subscriber> subscriber, Vector<Msg> toPublish ) {
		this.setTopics(topics);
		this.setPublisher(publisher);
		this.setSubscriber(subscriber);
		this.toPublish = toPublish;
	}

	public ConcurrentHashMap<String,Publishers> getPublisher() {
		return publisher;
	}

	public void setPublisher(ConcurrentHashMap<String,Publishers> publisher) {
		this.publisher = publisher;
	}

	public ConcurrentHashMap<String,Topic> getTopics() {
		return topics;
	}

	public void setTopics(ConcurrentHashMap<String,Topic> topics) {
		this.topics = topics;
	}

	public ConcurrentHashMap<String,Subscriber> getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(ConcurrentHashMap<String,Subscriber> subscriber) {
		this.subscriber = subscriber;
	}
	
	public Vector<Msg> getToPublish() {
		return this.toPublish;
	}

	public void setToPublish( Vector<Msg> toPublish) {
		this.toPublish = toPublish;
	}
}
