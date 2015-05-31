import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;


public interface iSubscriber extends Remote {

	void showTopics() throws RemoteException, NotBoundException,UnknownHostException;

	public void addTopic(String newTopic) throws RemoteException, NotBoundException, UnknownHostException;

	boolean receiveNotification(String event, String topic)throws AccessException, UnknownHostException, RemoteException, NotBoundException, NoSuchAlgorithmException;

}
