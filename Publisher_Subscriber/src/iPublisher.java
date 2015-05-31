import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;


public interface iPublisher extends Remote{
	public void sendtopic(String A) throws RemoteException, NotBoundException, UnknownHostException;
	public void lookupBroker() throws AccessException, RemoteException, NotBoundException, UnknownHostException;
	public void sendEvent(String string, String string2) throws RemoteException, NotBoundException, NoSuchAlgorithmException, UnknownHostException;
	

}
