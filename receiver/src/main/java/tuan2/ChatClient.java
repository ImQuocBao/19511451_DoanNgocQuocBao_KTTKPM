package tuan2;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;

import javax.swing.JLabel;
import java.awt.Font;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.BoxLayout;
import java.awt.Button;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;


public class ChatClient extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtMsg;
	private Button btnSend;
	private JTextArea textArea;
	
	public void makeUi() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("Enter Text");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblNewLabel);
		
		txtMsg = new JTextField();
		txtMsg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(txtMsg);
		txtMsg.setColumns(10);
		
		
		btnSend = new Button("Send");
		panel.add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
	}
	
	public void showUi() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(700, 500);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setTitle("TÃ¨o Chat ChÃ­t");
	}
	
	public ChatClient() {
		makeUi();
		handleActions();
		showUi();
		txtMsg.requestFocus();
	}
	
	public static void main(String[] args) throws Exception {
		new ChatClient();
		
		BasicConfigurator.configure();
		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		Context ctx = new InitialContext(settings);
		Object obj = ctx.lookup("TopicConnectionFactory");
		ConnectionFactory factory = (ConnectionFactory) obj;
		Connection con = factory.createConnection("admin", "admin");
		con.start();
		Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
		Destination destination = (Destination) ctx.lookup("dynamicTopics/thanthidet");
		MessageProducer producer = session.createProducer(destination);
		// Tạo 1 message
		Message msg = session.createTextMessage("xin chào người ẹp");
		// gửi
		producer.send(msg);
		// shutdown connection
		session.close();
		con.close();		
	}
	
	public void handleActions() {
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String newMsg = txtMsg.getText();
				
				textArea.append( newMsg + "\n" );
				txtMsg.setText("");
				txtMsg.requestFocus();
			}
		});
	}
}
