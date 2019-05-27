package launcher;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.frontend.gui.ClientManager;
import server.frontend.gui.ServerManager;

public class MessengerLauncher extends JFrame implements ActionListener {

	private static final long serialVersionUID = 8065878279113563874L;
	
	private JButton btnStartServer;
	private JButton btnStartClient;
	private JButton btnExit;
	
	public MessengerLauncher() {
		super("Messenger Launcher");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(700, 400));
		setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        // Move the window
        this.setLocation(x, y);
        initComponents();

		
	}

	private void initComponents() {
		setLayout(new GridBagLayout());
		JPanel pan = initButPan();
		
		add(pan, new GridBagConstraints());
		
	}
	
	public JPanel initButPan() {
		JPanel butPan = new JPanel(new GridLayout(3, 1));
		
		btnStartServer = new JButton("                                    Start as server                                    ");
		btnStartServer.addActionListener(this);
		
		btnStartClient = new JButton("                                    Start as client                                    ");
		btnStartClient.addActionListener(this);
		
		btnExit = new JButton("                                    Exit                                    ");
		btnExit.addActionListener(this);
		
		butPan.add(btnStartServer);
		butPan.add(btnStartClient);
		butPan.add(btnExit);
		
		
		return butPan;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnStartServer)) {
			new Thread(() -> new ServerManager().setVisible(true)).start();
			dispose();
		}
		else if(e.getSource().equals(btnStartClient)) {
			new Thread(() -> new ClientManager().setVisible(true)).start();
			dispose();
		}
		else if(e.getSource().equals(btnExit)) {
			System.exit(0);
		}

	}

	public static void main(String[] args) {
		new MessengerLauncher().setVisible(true);

	}

}
