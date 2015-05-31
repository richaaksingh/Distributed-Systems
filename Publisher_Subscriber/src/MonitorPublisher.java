import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;


public class MonitorPublisher extends Thread {
	private Publisher obj;
	private Registry register;
	//private iSubscriber subscriber;
	private BInt bobj;

	public MonitorPublisher(Publisher obj){
		this.obj = obj;
	}


	public void run(){
		while(!obj.pendingMsg.isEmpty()){
			try{
				Set<String> key = obj.pendingMsg.keySet();
				Iterator<String> pi =  key.iterator();
				while(pi.hasNext()){
					String publisherEntry = pi.next();
					//System.out.println(publisherEntry.getKey() +" :: "+ publisherEntry.getValue());
					Vector<String> newVal = obj.pendingMsg.get(publisherEntry);
					Iterator<String> i =  newVal.iterator();
					while(i.hasNext()){
						String s = i.next();
						String[] sc = s.split(":");
						Registry BSreg = LocateRegistry.getRegistry( obj.BROKER, obj.BROKERPORT );
						bobj = ( BInt )BSreg.lookup( "Broker1" );
						bobj.publish( sc[0], publisherEntry , sc[1] );
						i.remove();
						if(newVal.isEmpty()){
							pi.remove();

						}

					}
				}
			}
			catch( Exception e){
				System.out.println("machine not available");
			}
		}
	}





}

