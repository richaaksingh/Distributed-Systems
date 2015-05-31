import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;


public class Subscriber extends UnicastRemoteObject
implements Serializable{

	public static int BROKERPORT = 9973;
	public static String BROKER;
	public static int SUBSCRIBERPORT = 7993;
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
		System.out.println(topics.size());
			return topics;
		
	}

}
