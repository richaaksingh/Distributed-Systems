import java.io.Serializable;
import java.util.Vector;
/**
 * File: Publsihers.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */

public class Publishers implements Serializable {
	private String address;
	private Vector<Msg> msgs = new Vector<Msg>();
	private Vector<String> topics  = new Vector<String>();
	private int mId;
	
	public Publishers( String add) {
		this.address = add;
	}
	
	public void addTopic( String t ) {
		if ( !topics.isEmpty() ) {
			if ( !topics.contains( t ) ) {
				topics.add( t );
			}
		}
	}
	
	public void addMsg( Msg m ) {
		msgs.add( m );
		mId++;
	}
	
	public int getMId() {
		return mId;
	}
	public String getAddress() {
		return this.address;
	}
}
