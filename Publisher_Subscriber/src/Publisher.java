import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


public class Publisher extends UnicastRemoteObject
implements PsInt, Serializable {

	public static int BROKERPORT = 9973;
	public static int PUBLISHERPORT = 7993;
	public static String BROKER = "glados";
	//public static String BACKUPBROKER = "buddy";
	private String address;
	public ConcurrentHashMap<String,Vector<String>> pendingMsg = new ConcurrentHashMap<String,Vector<String>>();
	private static Publisher obj = null;
	//public Subscribeer s;
	private BInt bobj;
	private Vector<String> ptopics = new Vector<String>();
	private Vector<String> stopics = new Vector<String>();

	public String getAddress() throws java.rmi.RemoteException {
		return address;
	}
	public void setAddress(String address) throws java.rmi.RemoteException {
		this.address = address;
	}
	private Publisher() throws RemoteException, ServerNotActiveException {
		super();
		InetAddress ip;
		try {
			/*obj = new Publisher();
			Registry reg = LocateRegistry.createRegistry( PUBLISHERPORT );
			reg.rebind( "Pub_Sub", obj );*/
			ip = InetAddress.getLocalHost();
			System.out.println( ( ip.getHostAddress() ).trim() );
			address = ( ip.getHostAddress() ).trim();
		} catch (UnknownHostException e) {
			e.printStackTrace();

		}

		Registry BSreg = 
				LocateRegistry.getRegistry( BROKER, BROKERPORT );
		try {
			bobj = ( BInt )BSreg.lookup( "Broker" );
			Vector<String> stemp = bobj.getTopics( address );
			System.out.println("stemp");
			if ( stemp != null ) {
				stopics = stemp;
			}
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void acknowledge( int members, String msg ) throws RemoteException {
		PublisherView p = new PublisherView();
		System.out.println( "The following message was received by " + members + " subscribers:" );
		System.out.println( msg );
		p.receiveAcknowledgement("The following message was received by " + members + " subscribers:");
	}

	public boolean msg( String topic, String msg ) {
		Subscribeer s = new Subscribeer();
		System.out.println( "Received following message on the topic: " + topic );
		System.out.println( msg );
		s.receiveNotification(topic, msg);
		//s.lblNewLabel_2.setText(msg+topic);
		return true;
	}

	public static Publisher getObject() {

		if ( obj == null ) {
			try {
				obj = new Publisher();
				System.out.println("publisher created");
				Registry reg = LocateRegistry.createRegistry( PUBLISHERPORT );
				reg.rebind( "Pub_Sub", obj );
				System.out.println("publisher registered");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServerNotActiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj;
	}

	public void bePublisher() throws RemoteException, ServerNotActiveException {
		Registry BSreg = 
				LocateRegistry.getRegistry( BROKER, BROKERPORT );
		try {
			bobj = ( BInt )BSreg.lookup( "Broker" );
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bobj.joinPub( address );
	}
	
	public void subscribe( String topic) throws RemoteException, ServerNotActiveException {
		boolean subscribed = false;
		stopics.add( topic );
		while ( !subscribed ) {
			Registry BSreg = 
					LocateRegistry.getRegistry( BROKER, BROKERPORT );
			try {
				bobj = ( BInt )BSreg.lookup( "Broker" );
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			subscribed = bobj.subscribe( topic, obj.address );
		}
	}

	public void unsubscribe( String topic) throws RemoteException, ServerNotActiveException {
		boolean unsubscribed = false;
		stopics.remove( topic );
		while ( !unsubscribed ) {
			Registry BSreg = 
					LocateRegistry.getRegistry( BROKER, BROKERPORT );
			try {
				bobj = ( BInt )BSreg.lookup( "Broker" );
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			unsubscribed = bobj.unsubscribe( topic, obj.address );
		}
	}

	public Set<String> showTopic() throws ServerNotActiveException, ClassNotFoundException, IOException{
		Registry BSreg = 
				LocateRegistry.getRegistry( BROKER, BROKERPORT );
		try {
			bobj = ( BInt )BSreg.lookup( "Broker" );
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bobj.showTopics();
	}

	public Vector<String> getTopics() {
		return stopics;
	}
	public boolean addTopic( String topic ) throws ServerNotActiveException, ClassNotFoundException, IOException {
		if ( !obj.ptopics.contains( topic ) ) {
			obj.ptopics.add( topic );
			Registry BSreg = 
					LocateRegistry.getRegistry( BROKER, BROKERPORT );
			try {
				bobj = ( BInt )BSreg.lookup( "Broker" );
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bobj.addTopic( topic, address );
			return true;
		}
		return false;
	}

	public boolean publish( String topicName, String msg ) throws ServerNotActiveException, ClassNotFoundException, IOException {
		if ( obj.ptopics.contains( topicName ) ) {
			try {	Registry BSreg = LocateRegistry.getRegistry( BROKER, BROKERPORT );
			bobj = ( BInt )BSreg.lookup( "Broker" );
			bobj.publish( topicName, obj.address, msg );
			} 
			catch(Exception e ){
				if(pendingMsg.contains(obj.address)){
					Vector<String> pm = pendingMsg.get(obj.address);
					String val = topicName + ":" + msg;
					pm.add(val);
					pendingMsg.put(obj.address, pm);
				}
				else{
					Vector<String> pm = new Vector<String>();
					String val = topicName + ":" + msg;
					pm.add(val);
					pendingMsg.put(obj.address, pm);
				}
				Thread mp = new MonitorPublisher(obj);
				mp.start();


			}

			return true;
		}
		return false;
	}
}




