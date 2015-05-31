
public interface PsInt extends java.rmi.Remote {
	public void acknowledge( int members, String msg ) throws java.rmi.RemoteException;
	public boolean msg( String topic, String msg ) throws java.rmi.RemoteException;
	public String getAddress()throws java.rmi.RemoteException;
	public void setAddress(String ip)throws java.rmi.RemoteException;
}
