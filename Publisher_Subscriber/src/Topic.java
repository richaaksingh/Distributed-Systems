import java.io.Serializable;
import java.util.Vector;

/**
 * File: Topic.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */
public class Topic implements Serializable {
	int count = 0;
	private String topic;
	private Vector<String> msgs = new Vector<String>();
	private Vector<String> publisher = new Vector<String>();
	private Vector<String> subscriber = new Vector<String>();
	public int lastUpdate = 0;
	public Topic( String pub, String t ) {
		if ( !publisher.isEmpty() ) {
			if ( !publisher.contains( pub ) ) {
				publisher.add( pub );
			}
		}else {
			publisher.add( pub);
		}
		this.setTopic(t);
	}
	public void addSubscriber( String add ) {
		if ( !subscriber.isEmpty() ) {	
			if ( !subscriber.contains( add ) ) {
				subscriber.add( add );
			}
		} else {
			subscriber.add( add );
		}
	}
	
	public void removeSubscriber( String add ) {
		if ( !subscriber.isEmpty() ) {	
			if ( subscriber.contains( add ) ) {
				subscriber.remove(add);
			}
		} 
	}
	
	public Vector<String> getPublishers() {
		return publisher;
	}
	public Vector<String> getMsgs() {
		return msgs;
	}
	public Vector<String> getSubscribers() {
		return subscriber;
	}
	
	public void addMsg( String m ) {
		msgs.add( m );
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
}
