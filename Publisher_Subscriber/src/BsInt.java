import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Set;
import java.util.Vector;

/**
 * File: BsInt.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */
public interface BsInt extends java.rmi.Remote {
	public void update( Update o ) throws java.rmi.RemoteException, ServerNotActiveException;
	public Update getupdate() throws java.rmi.RemoteException, ServerNotActiveException;
	public void addTopic( String topic, String add ) throws java.rmi.RemoteException, ServerNotActiveException, ClassNotFoundException, IOException;
	public void publish( String topic, String address, String msg ) throws java.rmi.RemoteException, ServerNotActiveException, ClassNotFoundException, IOException;
	public Vector<String> getTopics(  String add ) throws java.rmi.RemoteException;
	public Set<String> showTopics( ) throws java.rmi.RemoteException;
	public void joinPub( String address ) throws java.rmi.RemoteException, ServerNotActiveException;
	public boolean subscribe( String topicNm, String add ) throws java.rmi.RemoteException, ServerNotActiveException;
	public Vector<String> susbcriberTopic(String ip) throws java.rmi.RemoteException, ServerNotActiveException;
	public boolean unsubscribe(String topic, String address) throws RemoteException, ServerNotActiveException;
}
