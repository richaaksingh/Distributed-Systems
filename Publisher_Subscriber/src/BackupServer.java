import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
/**
 * File: BackupServer.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */

public class BackupServer extends UnicastRemoteObject
implements BsInt, Serializable, Runnable {
	public static int BROKERPORT = 9972;
	public static int PUBLISHERPORT = 7990;
	public static int SUBSCRIBERPORT = 7990;

	private ConcurrentHashMap<String,Topic> topics = new ConcurrentHashMap<String,Topic>();
	private ConcurrentHashMap<String,Publishers> publisher = new ConcurrentHashMap<String,Publishers>();
	private ConcurrentHashMap<String,Subscriber> subscriber = new ConcurrentHashMap<String,Subscriber>();
	private ConcurrentHashMap<String,Vector<String>> topicList = new ConcurrentHashMap<String,Vector<String>>();

	private Vector<Msg> toPublish = new Vector<Msg>();
	
	private static BackupServer obj;
	public static String Brokername = "glados";
	public static String Backpname = "buddy";

	private BackupServer() throws RemoteException {
		super();
	}

	public void addTopic( String topic, String add ) throws ServerNotActiveException, ClassNotFoundException, IOException {
		topic = topic.toLowerCase().trim();
		System.out.println( "Entered addTopic " + topic);
		//readTopics();
		if ( !topics.isEmpty() ) {
			if ( !topics.contains( topic ) ) {
				topics.put( topic, new Topic( add.trim(), topic ) );
				//writeTopics();
			}
		} else {
			topics.put( topic, new Topic( add.trim(), topic ) );
			//writeTopics();
		}
		//readPublisher();
		Publishers pub = publisher.get( add.trim() );
		pub.addTopic( topic );
		//writePublisher();
	}



	public void publish( String topic, String address, String msg ) throws ServerNotActiveException, ClassNotFoundException, IOException {
		topic = topic.toLowerCase().trim();
		System.out.println( topics.isEmpty() );
		//readTopics();
		if ( !topics.isEmpty() ) {
			System.out.println( "Entered if" );
			if ( topics.containsKey( topic ) ) {
				System.out.println( "Entered publish" );
				Msg m = new Msg( msg, address, topic, topics.get( topic ).getSubscribers() );
				synchronized(obj){
					//readToPublish();
					toPublish.add( m );
					//writeToPublish();
					System.out.println( "Written to publish " + toPublish.isEmpty());
				}
				//readPublisher();
				publisher.get( address ).addMsg(m);
				//writePublisher();
				//readTopics();
				topics.get( topic ).addMsg( msg );
				//writeTopics();

			}
		}

	}
	public Set<String> showTopics() throws java.rmi.RemoteException{
		/*try {
			//readTopics();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Set<String> tpcs = topics.keySet();
		System.out.println( " Entered getTopic");
		return tpcs;
	}

	public Vector<String> getTopics( String add ) throws java.rmi.RemoteException {
		/*try {
			readSubscriber();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if ( subscriber.containsKey( add ) ) {
			return this.subscriber.get( add ).getTpcs();
		}
		return null;
	}

	public void joinPub( String address ) throws java.rmi.RemoteException, ServerNotActiveException  {
		Publishers pub = new Publishers( address ); 
		/*try {
			readPublisher();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if ( !publisher.isEmpty() ) {
			if ( !publisher.containsKey( address ) ) {
				this.publisher.put( address, pub );
			}
		} else {
			this.publisher.put( address, pub );
			/*try {
				writePublisher();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}

	public boolean subscribe( String topicNm, String add ) throws java.rmi.RemoteException, ServerNotActiveException {
		/*try {
			readTopics();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if ( topics.containsKey( topicNm ) ) {
			Topic t = topics.get( topicNm );
			t.addSubscriber( add );
			/*try {
				writeTopics();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				readSubscriber();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			subscriber.put( add, new Subscriber( add, topicNm ) );
			/*try {
				writeSubscriber();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			return true;
		}
		return false;
	}

	public boolean unsubscribe( String topicNm, String add ) throws java.rmi.RemoteException, ServerNotActiveException {
		/*try {
			readTopics();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if ( topics.containsKey( topicNm ) ) {
			Topic t = topics.get( topicNm );
			t.removeSubscriber( add );
			/*try {
				writeTopics();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				readSubscriber();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			Subscriber s = subscriber.get( add);
			s.remove(topicNm);
			/*try {
				writeSubscriber();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			return true;
		}
		return false;
	}

	public Vector<String> susbcriberTopic(String ip) throws java.rmi.RemoteException, ServerNotActiveException{
		Subscriber s = subscriber.get(ip);
		return s.getTpcs();

	}



	public static void main( String[] args ) {


		BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );

		try {
			
				Registry reg = LocateRegistry.createRegistry( BROKERPORT );
				/*File f = new File("BrokerObja" + ".ser");
				if (f.exists() && !f.isDirectory()) {
				//	readObj();
				}
				else{
					
					//writeObj();
				}*/
				obj = new BackupServer();
				reg.rebind( "BackUp", obj );
				Thread t = new Thread( obj );
				t.start();
			

		} catch ( RemoteException e ) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while ( true ) {
			//synchronized(obj){
				/*if ( !toPublish.isEmpty() ) {
					try {
						readToPublish();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					//System.out.println( "Entered brun" );
					if ( !toPublish.isEmpty() ) {
						toPublish.get( 0 ).start();
						toPublish.remove(0);
						/*try {
							writeToPublish();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
					}
				}
			//}
			
		}
	

	public static BackupServer getObject() {
		return obj;
	}
	public ConcurrentHashMap<String,Topic> updateTopics() {
		return topics;
	}
	public ConcurrentHashMap<String,Publishers> updatePublishers() {
		return publisher;
	}
	public Vector<Msg> updateMsgs() {
		return toPublish;
	}


	public void update( Update up ) throws java.rmi.RemoteException, ServerNotActiveException  {
		System.out.println("Entered update");
		this.publisher = up.getPublisher();
		this.subscriber = up.getSubscriber();
		this.topics = up.getTopics();
		this.toPublish = up.getToPublish();
		/*System.out.println( o instanceof Subscriber );
		System.out.println( o instanceof Publishers );
		System.out.println( o instanceof Topic );
		System.out.println( o instanceof Msg );
		System.out.println( o.getClass());
		if ( o instanceof Subscriber ) {
			System.out.println("Subscriber");
			Subscriber sub = ( Subscriber ) o;
			String add = sub.getAddress();
			if ( subscriber.containsKey( add ) ) {
				Subscriber s = subscriber.get( add );
				s.check( sub.getTpcs() );
			} else {
				subscriber.put( add, sub );
			}
		} else if ( o instanceof Publishers ) {
			System.out.println("Pub");
			Publishers pub = ( Publishers ) o;
			String add = pub.getAddress();
			if ( publisher.containsKey( pub ) ) {
				publisher.replace( add, pub );
			} else{
				publisher.put(add, pub);
			}
		} else if ( o instanceof Msg ) {
			System.out.println("Subscriber");
			Msg m = ( Msg ) o;
			toPublish.addElement( m );
		} else if ( o instanceof Topic ) {
			Topic t  = ( Topic ) o;
			System.out.println("topic"+t.getTopic());
			
			topics.put(t.getTopic(), t);
		}*/
	}
	public Update getupdate() throws java.rmi.RemoteException, ServerNotActiveException {
		return new Update( this.topics, this.publisher, this.subscriber, this.toPublish );
	}
}
