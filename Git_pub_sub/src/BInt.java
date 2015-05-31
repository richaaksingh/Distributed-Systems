import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.Set;
import java.util.Vector;


public interface BInt extends java.rmi.Remote {

	public void addTopic( String topic, String add ) throws java.rmi.RemoteException, ServerNotActiveException, ClassNotFoundException, IOException;
	public void publish( String topic, String address, String msg ) throws java.rmi.RemoteException, ServerNotActiveException, ClassNotFoundException, IOException;
	public Vector<String> getTopics(  String add ) throws java.rmi.RemoteException,ServerNotActiveException;
	public Set<String> showTopics( ) throws java.rmi.RemoteException,ServerNotActiveException;
	public void joinPub( String address ) throws java.rmi.RemoteException, ServerNotActiveException;
	public boolean subscribe( String topicNm, String add ) throws java.rmi.RemoteException, ServerNotActiveException;
	public Vector<String> susbcriberTopic(String ip) throws java.rmi.RemoteException, ServerNotActiveException;
	public boolean unsubscribe(String topic, String address) throws RemoteException, ServerNotActiveException;
}