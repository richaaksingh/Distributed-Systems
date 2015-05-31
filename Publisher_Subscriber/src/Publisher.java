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

/**
 * File: Publisher.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */
public class Publisher extends UnicastRemoteObject
implements PsInt, Serializable {

	public static int BROKERPORT = 9972;
	public static int PUBLISHERPORT = 7990;
	public static String BROKER = "glados";
	public static String BACKUPBROKER = "buddy";
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
	private Publisher() throws RemoteException {
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

		try {	
			Registry BSreg = LocateRegistry.getRegistry( BROKER, BROKERPORT );

			bobj = ( BInt )BSreg.lookup( "Broker" );
			Vector<String> stemp = bobj.getTopics( address );
			System.out.println("stemp");
			if ( stemp != null ) {
				stopics = stemp;
			}
		} catch (Exception e) {
			
			try {
				Registry BSreg = LocateRegistry.getRegistry( BACKUPBROKER, BROKERPORT );
				bobj = ( BInt )BSreg.lookup( "Backup" );
				Vector<String> stemp = bobj.getTopics( address );
				System.out.println("stemp");
				if ( stemp != null ) {
					stopics = stemp;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} 
		}
		return obj;
	}

	public void bePublisher() {
		try {
			Registry BSreg = LocateRegistry.getRegistry( BROKER, BROKERPORT );
			bobj = ( BInt )BSreg.lookup( "Broker" );
			bobj.joinPub( address );
		} catch (Exception e) {
			
			try {
				Registry BSreg1 = LocateRegistry.getRegistry( BACKUPBROKER, BROKERPORT );
				BsInt bsobj = ( BsInt )BSreg1.lookup( "BackUp" );
				bsobj.joinPub( address );
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	public void subscribe( String topic){
		boolean subscribed = false;
		stopics.add( topic );
		while ( !subscribed ) {
			try{Registry BSreg = 
					LocateRegistry.getRegistry( BROKER, BROKERPORT );

			bobj = ( BInt )BSreg.lookup( "Broker" );
			subscribed = bobj.subscribe( topic, obj.address );
			} catch (Exception e) {
				
				try {
					Registry BSreg1 = LocateRegistry.getRegistry( BACKUPBROKER, BROKERPORT );
					BsInt bsobj = ( BsInt )BSreg1.lookup( "BackUp" );
					subscribed = bsobj.subscribe( topic, obj.address );
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		}
	}

	public void unsubscribe( String topic) {
		boolean unsubscribed = false;
		stopics.remove( topic );
		while ( !unsubscribed ) {
			try {	
				Registry BSreg = LocateRegistry.getRegistry( BROKER, BROKERPORT );
				bobj = ( BInt )BSreg.lookup( "Broker" );
				unsubscribed = bobj.unsubscribe( topic, obj.address );
			} catch (Exception e) {
				
				try {
					Registry BSreg1 = LocateRegistry.getRegistry( BACKUPBROKER, BROKERPORT );
					BsInt bsobj = ( BsInt )BSreg1.lookup( "BackUp" );
					unsubscribed = bsobj.unsubscribe( topic, obj.address );
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
	}

	public Set<String> showTopic() {
		try {
			Registry BSreg = 
					LocateRegistry.getRegistry( BROKER, BROKERPORT );
			bobj = ( BInt )BSreg.lookup( "Broker" );
			return bobj.showTopics();
		} catch (Exception e) {
			
			try {
				Registry BSreg1 = LocateRegistry.getRegistry( BACKUPBROKER, BROKERPORT );
				System.out.println("entered");
				BsInt bsobj = ( BsInt )BSreg1.lookup( "BackUp" );
				return bsobj.showTopics();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	public Vector<String> getTopics() {
			return stopics;
	}
	public boolean addTopic( String topic ) {
		if ( !ptopics.contains( topic ) ) {
			ptopics.add( topic );
			try {	Registry BSreg = 
					LocateRegistry.getRegistry( BROKER, BROKERPORT );

			bobj = ( BInt )BSreg.lookup( "Broker" );
			bobj.addTopic( topic, address );
			} catch (Exception e) {
				try {	Registry BSreg1 = LocateRegistry.getRegistry( BACKUPBROKER, BROKERPORT );

				BsInt bsobj = ( BsInt )BSreg1.lookup( "BackUp" );
				bsobj.addTopic( topic, address );
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			return true;
		}
		return false;
	}
	
	public boolean publish( String topicName, String msg )  {
		if (  ptopics.contains( topicName ) ) {
			try {	Registry BSreg = LocateRegistry.getRegistry( BROKER, BROKERPORT );
			bobj = ( BInt )BSreg.lookup( "Broker" );
			bobj.publish( topicName, obj.address, msg );
			return true;
			} 
			catch(Exception e ){
				try {	Registry BSreg1 = LocateRegistry.getRegistry( BACKUPBROKER, BROKERPORT );
				BsInt bsobj = ( BsInt )BSreg1.lookup( "BackUp" );
				bsobj.publish( topicName, obj.address, msg );
				return true;
				} catch (Exception e1) {
					/*if(pendingMsg.contains(obj.address)){
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
					mp.start();*/
				}
				

				/*
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


				 */}

			
		}
		return false;
	}



}
