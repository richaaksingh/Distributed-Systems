import java.io.Serializable;
import java.util.Vector;


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
}
