import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * File: iBroker.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */
public interface iBroker extends Remote  {

	void receiveTopic(String Topic, String ipaddress)throws RemoteException, UnknownHostException, NotBoundException;

	void advertise(String topic)throws RemoteException, UnknownHostException, NotBoundException;

	//void notifyBroker();
	public ArrayList<String> getTopics() throws RemoteException, UnknownHostException, NotBoundException ;
	public void setTopics(ArrayList<String> topics) throws RemoteException, UnknownHostException, NotBoundException ;
	public void subscriberList(String topic, String ipAddress) throws RemoteException, UnknownHostException, NotBoundException;

	void event(String event, String topic) throws NoSuchAlgorithmException, RemoteException, UnknownHostException, NotBoundException;


	void receiveAcknowledgement(String subscriberIp, String event)throws NoSuchAlgorithmException, RemoteException, UnknownHostException, NotBoundException;

}
