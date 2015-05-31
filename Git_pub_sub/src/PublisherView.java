
import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

import javax.swing.Timer;

public class PublisherView  implements ActionListener{

	private JFrame frame;
	//private BInt bobj;
	//private Publisher obj;
	Publisher obj = Publisher.getObject();
	/**
	 * @wbp.nonvisual location=234,481
	 */
	private final JComboBox comboBox = new JComboBox();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	JButton topicb;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void method() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PublisherView window = new PublisherView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws RemoteException 
	 */
	public PublisherView() throws RemoteException {
		initialize();
		topicb.addActionListener(this);


	}

	/**
	 * Initialize the contents of the frame.
	 * @throws RemoteException 
	 */
	private void initialize() throws RemoteException {
		//final JLabel lblNewLabel = new JLabel();
		//final JLabel lblNewLabel_1 = new JLabel();
		frame = new JFrame("Publisher");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblAddTopic = new JLabel("Add Topic");
		lblAddTopic.setBounds(6, 90, 86, 16);
		frame.getContentPane().add(lblAddTopic);

		topicb = new JButton("add Topic");
		topicb.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String value = textField.getText();
				try {
					try {
						if(obj.addTopic(value)){
							JOptionPane.showMessageDialog(null," topic successfully added");
							textField.setText("");

						}
						else{
							JOptionPane.showMessageDialog(null," topic already exists");
							textField.setText("");
						}
					} catch (HeadlessException | ClassNotFoundException
							| IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (ServerNotActiveException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
				);
		topicb.setBounds(6, 150, 126, 29);
		frame.getContentPane().add(topicb);

		textField = new JTextField();
		textField.setBounds(6, 110, 147, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);


		JLabel lblPublish = new JLabel("Publish");
		lblPublish.setBounds(261, 90, 61, 16);
		frame.getContentPane().add(lblPublish);

		textField_1 = new JTextField();
		textField_1.setBounds(261, 110, 147, 28);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JButton publishButton = new JButton("Publish Message");
		publishButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String topic = textField_3.getText();
				String message = textField_1.getText();
				try {
					if(obj.publish(topic,message)){
						//JOptionPane.showMessageDialog(null,"Message sucessfully sent");

					}
					else{
						JOptionPane.showMessageDialog(null,"Topic does not exists");
					}
					textField_1.setText("");
					textField_3.setText("");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ServerNotActiveException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}});
		publishButton.setBounds(261, 150, 140, 29);
		frame.getContentPane().add(publishButton);



		//final JLabel lblNewLabel = new JLabel();


		JButton btnBack = new JButton("Subscriber view");
		btnBack.setBounds(6, 21, 198, 29);
		frame.getContentPane().add(btnBack);

		JLabel lblTopic = new JLabel("Topic");
		lblTopic.setBounds(261, 26, 61, 16);
		frame.getContentPane().add(lblTopic);

		textField_3 = new JTextField();
		textField_3.setBounds(261, 54, 140, 30);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		btnBack.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {

				Subscribeer s= new Subscribeer();
				s.method();
			}

		});
		ActionListener actionListener=new ActionListener(){

			public void actionPerformed()
			{

			}

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		};

		final Timer s=new Timer(5000,actionListener);


		topicb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String topicName = textField.getText();
				//lblNewLabel.setText(topicName+" topic successfully added");
				s.start();


				//JDialog dialogBox=new JDialog(frame,"Succesfuly subscribed to topic"+topicName,true);

				//dialogBox.setVisible(true);
			}
		});


		publishButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//	lblNewLabel_1.setText("sent Message");

			}

		}
				);


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void receiveAcknowledgement(String s){
		System.out.println("err");
		JOptionPane.showMessageDialog(null,s);


	}
}
