


import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class mainView {

	private JFrame frame;
	Publisher obj = Publisher.getObject();
	JButton btnNewButton;
	String optionSelected;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainView window = new mainView();
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
	public mainView() throws RemoteException {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 * @throws RemoteException 
	 */
	private void initialize() throws RemoteException {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Select one of the options");
		ButtonGroup b = new ButtonGroup();

		lblNewLabel.setBounds(103, 87, 204, 16);
		frame.getContentPane().add(lblNewLabel);
		Subscribeer view1= new Subscribeer();
		PublisherView view=new PublisherView();
		JLabel sp = new JLabel("PUBSUB");
		sp.setBounds(161, 29, 61, 16);
		frame.getContentPane().add(sp);

		final JRadioButton rdbtnPublisher = new JRadioButton("Publisher");
		rdbtnPublisher.setBounds(101, 111, 141, 23);
		frame.getContentPane().add(rdbtnPublisher);
		rdbtnPublisher.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnPublisher.isSelected())
				{
					try {
						obj.bePublisher();
						
					} catch (RemoteException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (ServerNotActiveException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}	
					PublisherView pub;
					try {
						pub = new PublisherView();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					PublisherView.method();
					frame.dispose();
				}

			}

		});


		final JRadioButton rdbtnSubscriber = new JRadioButton("Subscriber");
		rdbtnSubscriber.setBounds(101, 156, 141, 23);
		frame.getContentPane().add(rdbtnSubscriber);
		rdbtnSubscriber.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnSubscriber.isSelected())
				{Subscribeer s= new Subscribeer();
				s.method();
				frame.dispose();
				}

			}

		});


		b.add(rdbtnSubscriber);	
		b.add(rdbtnPublisher);	




	}
}
