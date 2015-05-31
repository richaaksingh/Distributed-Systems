import java.util.ArrayList;
import java.util.List;


public class test {
	static List<String> l = new ArrayList<String>();
	static List<String> lp = new ArrayList<String>();


public static void main(String args[]){


	l.add("richa");
	l.add("Umang");
	lp = l;
	System.out.println(l);
	System.out.println(lp);


}
}
