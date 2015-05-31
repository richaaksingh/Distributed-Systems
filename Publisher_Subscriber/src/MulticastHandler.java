import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * File: MulticastHandler.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */

public class MulticastHandler implements Runnable, Serializable {

	public static int PUBLISHERPORT = 7990;
	Msg msg;
	String sub;
	public MulticastHandler( Msg m, String add ) {
		msg = m;
		this.sub = add;
	}
	@Override
	public void run() {
		boolean received = false;
		PsInt psobj = null;
		int t = 5000, counter = 0;
		while ( !received ) {
			if ( counter > 9 ) {
				t = 60000;
			}
			try {
					Registry PSreg = 
	        			LocateRegistry.getRegistry( this.sub, PUBLISHERPORT );
					psobj = ( PsInt )PSreg.lookup( "Pub_Sub" );
				System.out.println( "Entered msg sending");
				System.out.println( msg.getTopic() +" "+ msg.getMsg());
				received = psobj.msg( msg.getTopic(), msg.getMsg() );
				counter++;
				System.out.println(received);
				if ( received ) {
					msg.received();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				try {
					Thread.sleep( t );
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					System.out.println("e");
					//e1.printStackTrace();
				}
			} catch (NotBoundException e) {
				System.out.println("e1");
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

}
