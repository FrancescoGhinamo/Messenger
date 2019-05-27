package client.frontend.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.backend.beam.Communicator;
import common.Commons;

public class ChatPanel extends JPanel implements ActionListener {


	private static final long serialVersionUID = 4223717000021206512L;

	private String name;

	private JTextArea txtHistory;
	private JTextArea txtMes;

	private JButton btnSend;




	public ChatPanel(String name) {
		super();
		this.name = name;
		initComponents();
	}



	private void initComponents() {
		txtHistory = new JTextArea(20, 20);
		txtHistory.setEditable(false);
		JScrollPane scrlH = new JScrollPane(txtHistory);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = 10;
		gbc.weighty = 15;
		gbc.fill = GridBagConstraints.BOTH;
		
		add(scrlH, gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 2;
		add(initSendMessagePanel(), gbc);


	}

	private JPanel initSendMessagePanel() {
		JPanel pan = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		txtMes = new JTextArea(3, 20);
		JScrollPane scrlM = new JScrollPane(txtMes);

		btnSend = new JButton("Send");
		btnSend.setFocusable(true);
		btnSend.addActionListener(this);
		btnSend.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					performSendMessage();
				}
			}
		});
		
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = 10;
		gbc.weighty = 2;
		gbc.fill = GridBagConstraints.BOTH;
		
		pan.add(scrlM, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 2;
		pan.add(btnSend, gbc);

		return pan;
	}



	public void performSendMessage() {
		
		String text = txtMes.getText();
		txtHistory.append("Me: "+ text + "\n");
		Communicator.getInstance().sendMessage(name, text);
		txtMes.setText("");
	}

	public void addMessage(String entMess) {
		String[] info = Commons.getData(entMess);
		String entry = info[0] + ": " + info[1];
		txtHistory.append(entry + "\n");
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnSend.setEnabled(enabled);
		txtMes.setEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnSend)) {
			performSendMessage();
		}

	}

}
