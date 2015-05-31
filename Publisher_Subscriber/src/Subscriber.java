import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * File: Susbcriber.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */
public class Subscriber implements Serializable {

	public static int BROKERPORT = 9972;
	public static String BROKER;
	public static int SUBSCRIBERPORT = 7990;
	private String address;
	private Vector<String> topics = new Vector<String>();
	
	
	public Subscriber( String add, String t ) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		this.address = add;
		this.topics.add( t );
	}
	
	public void remove(String t){
		this.topics.remove(t);
	}
	public Vector<String> getTpcs() {
			return topics;
	}
	public void addTopic( String topic ) {
		if ( !topics.contains( topic ) ) {
			topics.addElement( topic );
		}
	}
	public void check( Vector<String> tpcs ) {
		if ( this.topics.size() != tpcs.size() ) {
			for ( int i = topics.size(); i < tpcs.size(); i++ ) {
				topics.addElement( tpcs.get( i ) );
			}
		}
	}
	public String getAddress() {
		return this.address;
	}
public int topicSize(){
	if(topics == null){
		return 0;
	}
	return topics.size();
}
}
