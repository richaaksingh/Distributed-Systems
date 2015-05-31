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
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File: Broker.java
 * 
 * This class is a as main server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */
public class Broker extends UnicastRemoteObject 
implements BInt, Serializable, Runnable {

	public static int BROKERPORT = 9972;
	public static int PUBLISHERPORT = 7990;
	public static int SUBSCRIBERPORT = 7990;

	private ConcurrentHashMap<String,Topic> topics = new ConcurrentHashMap<String,Topic>();
	private ConcurrentHashMap<String,Publishers> publisher = new ConcurrentHashMap<String,Publishers>();
	private ConcurrentHashMap<String,Subscriber> subscriber = new ConcurrentHashMap<String,Subscriber>();
	private ConcurrentHashMap<String,Vector<String>> topicList = new ConcurrentHashMap<String,Vector<String>>();

	private Vector<Msg> toPublish = new Vector<Msg>();
	private static Broker obj;
	private Vector<Object> toUpdate = new Vector<Object>();
	public static String Brokername = "glados";
	public static String Backupname = "buddy";

	private Broker() throws RemoteException {
		super();
		try {	
			Registry BSreg = LocateRegistry.getRegistry( Backupname, BROKERPORT );
			
			BsInt bsobj = ( BsInt )BSreg.lookup( "BackUp" );
			Update up = bsobj.getupdate();
			this.publisher = up.getPublisher();
			this.subscriber = up.getSubscriber();
			this.topics = up.getTopics();
			this.toPublish = up.getToPublish();
		} catch (NotBoundException e) {
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void addTopic( String topic, String add ) throws ServerNotActiveException, ClassNotFoundException, IOException {
		topic = topic.toLowerCase().trim();
		System.out.println( "Entered addTopic " + topic);
		//readTopics();
		Topic temp = new Topic( add.trim(), topic );
		if ( !topics.isEmpty() ) {
			if ( !topics.containsKey( topic ) ) {
				topics.put( topic, temp );
				toUpdate.addElement( temp );
				//writeTopics();
			}
		} else {
			topics.put( topic, temp );
			toUpdate.addElement( temp );
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
				toUpdate.addElement( m );
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
			readTopics();
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
				toUpdate.addElement( pub );
				/*try {
					writePublisher();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		} else {
			this.publisher.put( address, pub );
			toUpdate.addElement( pub );
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
			if ( !subscriber.containsKey( add ) ) {
				Subscriber s = new Subscriber( add, topicNm );
				subscriber.put( add, s );
				toUpdate.addElement( s );
				
			} else {
				Subscriber tmp =  subscriber.get( add );
				tmp.addTopic( topicNm ); 
				toUpdate.addElement( tmp );
			}
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

			Subscriber s = subscriber.get( add );
			s.remove(topicNm);
			toUpdate.addElement( s );
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
				/*File f = new File("BrokerObj" + ".ser");
				if (f.exists() && !f.isDirectory()) {
					//readObj();
				}
				else{
					
					//writeObj();
				}*/
				obj = new Broker();
				reg.rebind( "Broker", obj );
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
				if ( !toPublish.isEmpty() ) {
					/*try {
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
				if ( !toUpdate.isEmpty() ) {
					try {	
						Registry BSreg = LocateRegistry.getRegistry( Backupname, BROKERPORT );
						
						BsInt bsobj = ( BsInt )BSreg.lookup( "BackUp" );
						bsobj.update( new Update( this.topics, this.publisher, this.subscriber, this.toPublish ) );
						//bsobj.update( toUpdate.get( 0 ) );
System.out.println( toUpdate.get( 0 ) instanceof Subscriber);
					} catch (NotBoundException e) {
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ServerNotActiveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					toUpdate.remove( 0 );
				}
		}
	}

	public static Broker getObject() {
		/*try {
			readObj();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return obj;
	}
	/*public ConcurrentHashMap<String,Topic> updateTopics() {
		try {
			readTopics();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topics;
	}
	public ConcurrentHashMap<String,Publishers> updatePublishers() {
		try {
			readPublisher();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return publisher;
	}
	public Vector<Msg> updateMsgs() {
		try {
			readToPublish();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toPublish;
	}

	public void writeTopics() throws IOException {
		System.out.println(" i enterred writetopics");
		if (topics != null) {
			System.out.println(" i m writing");
			FileOutputStream fout = new FileOutputStream("backupTopic.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(topics);
			oos.close();
			fout.close();
		}
	}

	public void readTopics() throws ClassNotFoundException, IOException {
		if (topics != null) {
			System.out.println(" i enterred readtopics");
			File f = new File("backupTopic.ser");
			if (f.exists() && !f.isDirectory()) {
				System.out.println(" i ebterted readtopics");
				InputStream file = new FileInputStream("backupTopic.ser");
				InputStream buffer = new BufferedInputStream(file);
				ObjectInputStream input1 = new ObjectInputStream(buffer);
				topics = (ConcurrentHashMap) input1.readObject();

				input1.close();
				file.close();
			} else {
				writeTopics();
				System.out.println(" i ebterted writetopics becoz coundt read");
			}
		}
	}

	public void writePublisher() throws IOException {
		if (publisher != null) {
			System.out.println(" i enterred writePublsiher");
			FileOutputStream fout = new FileOutputStream("backupPublisher.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(publisher);
			oos.close();
			fout.close();
		}
	}

	public void readPublisher() throws ClassNotFoundException, IOException {
		System.out.println(" i enterred readPublsiher");
		if (publisher != null) {
			File f = new File("backupPublisher.ser");
			if (f.exists() && !f.isDirectory()) {
				InputStream file = new FileInputStream("backupPublisher.ser");
				InputStream buffer = new BufferedInputStream(file);
				ObjectInputStream input1 = new ObjectInputStream(buffer);
				publisher = (ConcurrentHashMap) input1.readObject();

				input1.close();
				file.close();
			} else {
				writePublisher();
			}
		}

	}
	public void writeSubscriber() throws IOException {
		System.out.println(" i enterred writesusbcriber");
		if (subscriber != null) {
			FileOutputStream fout = new FileOutputStream("backupSubscriber.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(subscriber);
			oos.close();
			fout.close();
		}
	}


	public void readSubscriber() throws ClassNotFoundException, IOException {
		System.out.println(" i enterred readsubscriber");
		if (subscriber != null) {
			File f = new File("backupSubscriber.ser");
			if (f.exists() && !f.isDirectory()) {
				InputStream file = new FileInputStream("backupSubscriber.ser");
				InputStream buffer = new BufferedInputStream(file);
				ObjectInputStream input1 = new ObjectInputStream(buffer);
				subscriber = (ConcurrentHashMap) input1.readObject();

				input1.close();
				file.close();
			} else {
				writeSubscriber();
			}
		}
	}

	public void writeToPublish() throws IOException {
		if (toPublish != null) {
			FileOutputStream fout = new FileOutputStream("backuptoPublish.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(toPublish);
			System.out.println("toPublish write");
			oos.close();
			fout.close();
		}
	}

	public void readToPublish() throws ClassNotFoundException, IOException {
		if (toPublish != null) {
			File f = new File("backuptoPublish.ser");
			if (f.exists() && !f.isDirectory()) {
				InputStream file = new FileInputStream("backuptoPublish.ser");
				InputStream buffer = new BufferedInputStream(file);
				ObjectInputStream input1 = new ObjectInputStream(buffer);
				toPublish = (Vector) input1.readObject();

				input1.close();
				file.close();
			} else {
				writeToPublish();
			}
		}
	}

	public static void writeObj() throws IOException {
		//		if (obj != null) {
		FileOutputStream fout = new FileOutputStream("BrokerObj" +".ser");
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(obj);
		oos.close();
		fout.close();
		//		}
	}

	public static void readObj() throws ClassNotFoundException, IOException {
		//		if (obj != null) {
		//			File f = new File("BrokerObj.ser");
		//			if (f.exists() && !f.isDirectory()) {
		InputStream file = new FileInputStream("BrokerObj" +".ser");
		InputStream buffer = new BufferedInputStream(file);
		ObjectInputStream input1 = new ObjectInputStream(buffer);
		obj = (Broker)input1.readObject();

		input1.close();
		file.close();
		//			} else {
		//				writeObj();
		//			}
		//		}
	}*/
}
