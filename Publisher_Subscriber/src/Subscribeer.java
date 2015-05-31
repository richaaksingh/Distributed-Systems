import java.awt.EventQueue;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * File: Subscriber.java
 * 
 * This class is a as backup server
 * and join CAN
 * 
 * @author Richa Singh
 * @author Akshata Patil
 * @author  Sharvari Bharve
 */
public class Subscribeer extends JFrame{

	private JFrame frame;
	Publisher obj = Publisher.getObject();
	private JTextField textField_1;
	private JTextField textField;
	String topicSubscribe;
	String topicUnsubscribe;
	JLabel lblNewLabel;
	private static Subscribeer sobj = null;
	private BInt bobj;
	Subscriber s ;
	public static int BROKERPORT = 9973;
	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}

	public void setLblNewLabel(JLabel lblNewLabel) {
		this.lblNewLabel = lblNewLabel;
	}

	//public static int PUBLISHERPORT = 7990;
	public static String BROKER = "glados";
	public JComboBox<String> comboBox;
	DefaultComboBoxModel dropdown;


	/**
	 * Launch the application.
	 */
	public static void method() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Subscribeer window = new Subscribeer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Subscribeer() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Susbcriber");
		JLabel label = new JLabel();
		dropdown = new DefaultComboBoxModel();
		comboBox = new JComboBox<String>(dropdown);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(34, 61, -36, 16);
		frame.getContentPane().add(textArea);

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(21, 47, 1, 16);
		frame.getContentPane().add(textArea_1);

		/*Registry BSreg;
		try {
			BSreg = LocateRegistry.getRegistry( BROKER, BROKERPORT );
			bobj = ( BInt )BSreg.lookup( "Broker" );
		} catch (RemoteException | NotBoundException e2) {
			// TODO Auto-generated catch block
			//e2.printStackTrace();
		}*/

		Vector<String> topic = obj.getTopics();
		if(topic!=null){
			for(String v : topic){
				if(dropdown.getIndexOf(v) == -1){
					dropdown.addElement(v);
				}
			}
		} 





		JButton btnSubscribe = new JButton("Subscribe");
		btnSubscribe.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				Set<String> val = null;
				try {
					try {
						val = obj.showTopic();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				topicSubscribe = textField_1.getText();

				System.out.println(topicSubscribe);
				try {
					if(val.contains(topicSubscribe)){
						obj.subscribe(topicSubscribe);
						//JOptionPane.showMessageDialog(null,"Successfully subscribed to topic");
						System.out.println(" it will susbcribe ");
						//comboBox = new JComboBox<String>();
						if(dropdown.getIndexOf(topicSubscribe) == -1){
							dropdown.addElement(topicSubscribe);
							textField_1.setText("");
							System.out.println("added to dropdown ");
							JOptionPane.showMessageDialog(null,"Successfully subscribed to topic");
						}
						else{
							JOptionPane.showMessageDialog(null,"topic already subscribed");
							textField_1.setText("");
						}

					} 
					else{
						JOptionPane.showMessageDialog(null,"topic does not exists");
						textField_1.setText("");
					}
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block




			}
		}


				);
		btnSubscribe.setBounds(6, 220, 133, 29);
		frame.getContentPane().add(btnSubscribe);
		JLabel lblEnterTopicName = new JLabel("Enter topic Name");
		lblEnterTopicName.setBounds(6, 164, 133, 16);
		frame.getContentPane().add(lblEnterTopicName);

		textField_1 = new JTextField();
		textField_1.setBounds(6, 181, 134, 28);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(250, 61, 1, 16);
		frame.getContentPane().add(textPane);

		JButton btnUnsubscribe = new JButton("Unsubscribe");

		btnUnsubscribe.setBounds(264, 220, 134, 29);
		frame.getContentPane().add(btnUnsubscribe);
		final JLabel lblEnterTopicName_1 = new JLabel("Enter topic Name");
		lblEnterTopicName_1.setBounds(286, 164, 129, 16);
		frame.getContentPane().add(lblEnterTopicName_1);
		btnUnsubscribe.addActionListener(new ActionListener()

		{

			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> val = null;
				val =obj.getTopics();
				topicUnsubscribe= textField.getText();
				try {
					if(val.contains(topicUnsubscribe)){
						obj.unsubscribe(topicUnsubscribe);
						JOptionPane.showMessageDialog(null,"unsubscription succesful");
						if(dropdown.getIndexOf(topicUnsubscribe) != -1){
							dropdown.removeElement(topicUnsubscribe);
							textField.setText("");
							System.out.println("removed to dropdown ");
							java.util.Iterator<String> i = val.iterator();



						}
						else{
							textField.setText("");
						}
					} 
					else{
						JOptionPane.showMessageDialog(null,"topic does not exists");
						textField.setText("");
					}
				}	
				catch (Exception e1) {

					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		textField = new JTextField();
		textField.setBounds(264, 181, 134, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(250, 206, 176, 16);
		frame.getContentPane().add(lblNewLabel);

		JButton btnNewButton = new JButton("Publisher View");
		btnNewButton.setBounds(16, 30, 133, 29);
		frame.getContentPane().add(btnNewButton);


		comboBox.setBounds(264, 114, 140, 29);
		frame.getContentPane().add(comboBox);

		JLabel lblNewLabel_1 = new JLabel("Topics");
		lblNewLabel_1.setBounds(300, 97, 84, 16);
		frame.getContentPane().add(lblNewLabel_1);

		JButton btnNewButton_1 = new JButton("Show Topics");
		btnNewButton_1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				Set<String> topics = new HashSet<String>();
				try {
					try {
						topics = obj.showTopic();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String newVal = "";
				for( String s : topics){
					newVal = newVal + s + "\n";
				}
				JOptionPane.showMessageDialog(null,newVal);
				//label.setText(newVal+ "</html>");
			}


		}
				);
		btnNewButton_1.setBounds(10, 114, 130, 29);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				PublisherView s;
				try {
					s = new PublisherView();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				PublisherView.method();

			}

		});
	}

	public void receiveNotification(String topic, String msg) {
		System.out.println("new message " + msg + "released on " + topic);
		String s = "New article "+ msg + " released on "+ topic;
		JOptionPane.showMessageDialog(null,s);


	}


}
