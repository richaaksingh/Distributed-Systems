import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;


public class Msg extends Thread
		implements Serializable {
	public static int PUBLISHERPORT = 7993;
	private String topic;
	private String publisher;
	private String msg;
	private Vector<String> subscribers;
	int sent = 0, received = 0;
	
	public Msg( String msg, String publisher, String t, Vector<String> sub ) {
		this.msg = msg;
		this.publisher = publisher;
		this.topic = t;
		this.subscribers = sub;
	}
	public synchronized void received() {
		this.received++;
	}
	public void multicast( ) {
		System.out.println( "Entered multicast" );
		for ( int i = 0; i < subscribers.size(); i++ ) {
			System.out.println( "Entered"+ subscribers.get( i ) );
			MulticastHandler mh = new MulticastHandler( this, subscribers.get( i ) );
			Thread t = new Thread( mh );
			t.start();
			sent++;
		}
	}
	public String getTopic() {
		return this.topic;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	@Override
	public void run() {
		System.out.println( "Entered mrun" );
		this.multicast();
		while ( this.sent != this.received ) {
			try {
				sleep( 5000 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
        	Registry PSreg = 
        			LocateRegistry.getRegistry( this.publisher, PUBLISHERPORT );
    		PsInt psobj = ( PsInt )PSreg.lookup( "Pub_Sub" );
    		psobj.acknowledge( this.received, this.msg );
		} catch ( Exception e) {
			System.out.println( "ack");
			e.printStackTrace();
		}
	}
}
