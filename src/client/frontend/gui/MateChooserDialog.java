package client.frontend.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MateChooserDialog extends JDialog implements ActionListener {

	
	private static final long serialVersionUID = -141596827274641055L;
	
	private DefaultListModel<String> matesLst;
	private JList<String> lst;
	private JButton btnOK;
	private ArrayList<String> names;
	private String result;
	
	public MateChooserDialog(JFrame owner, boolean modale, ArrayList<String> names) {
		super(owner, "Mate chooser", modale);
		result = "";
		this.names = names;
		initComponents(names);
		pack();
		
	}

	private void initComponents(ArrayList<String> names) {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 10;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(3, 3, 3, 3);
		
		add(initNamePane(names), gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 2;
		add(initButPan(), gbc);
		
	}
	
	private JScrollPane initNamePane(ArrayList<String> names) {
		matesLst = new DefaultListModel<String>();
		lst = new JList<String>(matesLst);
		
		for(String s: names) {
			matesLst.addElement(s);
		}
		
		return new JScrollPane(lst);
	}
	
	private JPanel initButPan() {
		JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(this);
		
		pan.add(btnOK);
		
		return pan;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnOK)) {
			int i = lst.getSelectedIndex();
			if(i != -1) {
				result = names.get(lst.getSelectedIndex());
				dispose();
			}
			
		}

	}

	public String getResult() {
		return result;
	}
	
	

}
