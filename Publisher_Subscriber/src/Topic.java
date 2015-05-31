import java.io.Serializable;
import java.util.Vector;


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
		}
		this.topic = t;
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
}
