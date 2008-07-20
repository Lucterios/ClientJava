/*
 *    This file is part of Lucterios.
 *
 *    Lucterios is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    Lucterios is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with Lucterios; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *	Contributeurs: Fanny ALLEAUME, Pierre-Olivier VERSCHOORE, Laurent GAY
 */

package org.lucterios.client.application.observer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import org.lucterios.client.presentation.Singletons;
import org.lucterios.client.transport.HttpTransport;
import org.lucterios.client.utils.LucteriosConfiguration.Server;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.graphic.JAdvancePanel;
import org.lucterios.utils.graphic.Tools;

import java.awt.event.*;
import java.awt.*;
import java.net.URL;

public class LogonBox extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GridBagLayout editGridLayout = new GridBagLayout();
	GridBagLayout MainGridLayout = new GridBagLayout();
	GridBagLayout btnGridLayout = new GridBagLayout();
	JAdvancePanel editPnl = new JAdvancePanel();
	JAdvancePanel btnPnl = new JAdvancePanel();
	JButton btn_SetUp = new JButton();
	JButton btn_OK = new JButton();
	JButton btn_Cancel = new JButton();
	JLabel lb_Reason = new JLabel();
	JLabel lb_Server = new JLabel();
	JLabel lb_PassWord = new JLabel();
	JLabel lb_User = new JLabel();
	JTextField txt_Server = new JTextField();
	JComboBox cmp_Server = null;
	JTextField txt_User = new JTextField();
	JPasswordField txt_PassWord = new JPasswordField();

	static String mLastUserLogon = "";
	static Server LastServerIndex = null;

	public int mModalResult = 0;

	public ActionListener mActionSetUp = null;

	private void refresh() {
		btn_SetUp.setEnabled(mActionSetUp != null);
		if (cmp_Server != null)
			editPnl.remove(cmp_Server);
		if (Singletons.Configuration.ServerCount() == 0) {
			LastServerIndex = null;
			txt_Server.setText("");
			txt_Server.setVisible(true);
			btn_OK.setEnabled(false);
		} else if (Singletons.Configuration.ServerCount() == 1) {
			LastServerIndex = Singletons.Configuration.GetServer(0);
			txt_Server.setText(LastServerIndex.ServerName);
			txt_Server.setVisible(true);
			btn_OK.setEnabled(true);
		} else {
			if (LastServerIndex == null)
				LastServerIndex = Singletons.Configuration.GetServer(0);
			cmp_Server = new JComboBox(Singletons.Configuration.getServers());
			cmp_Server.setSelectedItem(LastServerIndex);
			cmp_Server.setVisible(true);
			cmp_Server.setMinimumSize(new java.awt.Dimension(150, 19));
			cmp_Server.setPreferredSize(new java.awt.Dimension(150, 19));
			cmp_Server.addActionListener(this);
			editPnl.add(cmp_Server, getCnt(2, 1, GridBagConstraints.REMAINDER,
					1, GridBagConstraints.HORIZONTAL, 0));
			btn_OK.setEnabled(true);
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screen.width - this.getSize().width) / 2,
				(screen.height - this.getSize().height) / 2);
		pack();
		Toolkit k = Toolkit.getDefaultToolkit();
		Dimension tailleEcran = k.getScreenSize();
		Dimension taille = getSize();
		setLocation((tailleEcran.width - taille.width) / 2,
				(tailleEcran.height - taille.height) / 3);
	}

	public void actionPerformed(ActionEvent event) {
		JComboBox cb = (JComboBox) event.getSource();
		LastServerIndex = (Server) cb.getSelectedItem();
	}

	private void setReason(String aReason) {
		lb_Reason.setVisible(true);
		if ("BADAUTH".equals( aReason ))
			lb_Reason.setText("Alias ou Mot de passe incorrect!");
		else if ("BADSESS".equals( aReason ))
			lb_Reason.setText("Session expir�e!");
		else if ("BADFROMLOCATION".equals( aReason ))
			lb_Reason.setText("Localisation de connection interdite!");
		else {
			lb_Reason.setText("");
			lb_Reason.setVisible(false);
		}
	}

	public void logon(String aReason) throws LucteriosException {
		setReason(aReason);
		URL url = this.getClass().getResource("ok.png");
		btn_OK.setIcon(new ImageIcon(url));
		btn_Cancel.setIcon(new ImageIcon(this.getClass().getResource(
				"cancel.png")));
		btn_SetUp.setIcon(new ImageIcon(this.getClass().getResource(
				"configure.png")));
		txt_User.setText(mLastUserLogon);
		txt_PassWord.setText("");
		String title = " Connexion";
		title = Singletons.Configuration.TitreDefault + title;
		this.setTitle(title.trim());
		refresh();
		setVisible(true);
		toFront();
		if (mModalResult == 1) {
			HttpTransport transp = Singletons.Transport();
			transp.setProxy(Singletons.Configuration.ProxyAdress,
					Singletons.Configuration.ProxyPort);
			transp.connectToServer(LastServerIndex.HostName,
					LastServerIndex.Directory, LastServerIndex.HostPort);
			mLastUserLogon = txt_User.getText();
			Singletons.Factory().setAuthentification(txt_User.getText(),
					new String(txt_PassWord.getPassword()));
		} else
			Singletons.exit();
	}

	public GridBagConstraints getCnt(int x, int y, int w, int h, int fill,
			double we) {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridheight = h;
		gridBagConstraints.gridwidth = w;
		gridBagConstraints.weightx = we;
		gridBagConstraints.weighty = we;
		gridBagConstraints.fill = fill;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		return gridBagConstraints;
	}

	public LogonBox() {
		super();
		this.setVisible(false);
		this.getContentPane().setLayout(MainGridLayout);
		this.setModal(true);
		this.setResizable(false);
		this.setTitle("Connexion");
		editPnl.setLayout(editGridLayout);
		btnPnl.setLayout(btnGridLayout);
		FocusTraversalPolicy ftp;
		ftp = new DefaultFocusTraversalPolicy();
		ftp.getLastComponent(txt_User);
		btn_OK.setFocusTraversalPolicy(ftp);
		btn_OK.setMnemonic('o');
		btn_OK.setText("Ok");
		btn_OK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btn_OK_actionPerformed(event);
				refresh();
			}
		});
		ftp = new DefaultFocusTraversalPolicy();
		ftp.getLastComponent(btn_Cancel);
		btn_Cancel.setFocusTraversalPolicy(ftp);
		btn_Cancel.setMnemonic('a');
		btn_Cancel.setText("Annuler");
		btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btn_Cancel_actionPerformed(e);
			}
		});

		btn_SetUp.setMnemonic('c');
		btn_SetUp.setText("Configurer");
		btn_SetUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mActionSetUp != null)
					mActionSetUp.actionPerformed(e);
				refresh();
			}
		});

		lb_Reason.setText("");
		lb_Reason.setForeground(Color.RED);
		lb_Reason.setAlignmentX(0.5f);
		lb_Reason.setAlignmentY(0.5f);
		lb_Server.setText("Serveur");
		lb_PassWord.setText("Mot de passe");
		lb_User.setText("Alias");

		txt_Server.setText("");
		txt_Server.setVisible(false);
		txt_Server.setEnabled(false);
		txt_Server.setMinimumSize(new java.awt.Dimension(150, 19));
		txt_Server.setPreferredSize(new java.awt.Dimension(150, 19));
		ftp = new DefaultFocusTraversalPolicy();
		ftp.getLastComponent(txt_PassWord);
		txt_User.setFocusTraversalPolicy(ftp);
		txt_User.setText("");
		txt_User.setMinimumSize(new java.awt.Dimension(150, 19));
		txt_User.setPreferredSize(new java.awt.Dimension(150, 19));
		ftp = new DefaultFocusTraversalPolicy();
		ftp.getLastComponent(txt_User);
		txt_PassWord.setFocusTraversalPolicy(ftp);
		txt_PassWord.setText("");
		txt_PassWord.setMinimumSize(new java.awt.Dimension(150, 19));
		txt_PassWord.setPreferredSize(new java.awt.Dimension(150, 19));
		this.getContentPane().add(editPnl,
				getCnt(0, 0, 1, 1, GridBagConstraints.BOTH, 1));
		this.getContentPane().add(btnPnl,
				getCnt(0, 1, 1, 1, GridBagConstraints.BOTH, 1));

		editPnl.add(lb_Reason, getCnt(1, 0, GridBagConstraints.REMAINDER, 1,
				GridBagConstraints.HORIZONTAL, 0));

		editPnl.add(lb_Server, getCnt(0, 1, 2, 1,
				GridBagConstraints.HORIZONTAL, 0));
		editPnl.add(lb_User, getCnt(0, 2, 2, 1, GridBagConstraints.HORIZONTAL,
				0));
		editPnl.add(lb_PassWord, getCnt(0, 3, 2, 1,
				GridBagConstraints.HORIZONTAL, 0));

		editPnl.add(txt_Server, getCnt(2, 1, GridBagConstraints.REMAINDER, 1,
				GridBagConstraints.HORIZONTAL, 0));
		editPnl.add(txt_User, getCnt(2, 2, GridBagConstraints.REMAINDER, 1,
				GridBagConstraints.HORIZONTAL, 0));
		editPnl.add(txt_PassWord, getCnt(2, 3, GridBagConstraints.REMAINDER, 1,
				GridBagConstraints.HORIZONTAL, 0));
		btnPnl.add(btn_OK, getCnt(0, 0, 1, 1, GridBagConstraints.NONE, 0));
		btnPnl.add(btn_SetUp, getCnt(1, 0, 1, 1, GridBagConstraints.NONE, 0));
		btnPnl.add(btn_Cancel, getCnt(2, 0, 1, 1, GridBagConstraints.NONE, 0));

		editPnl.setFontImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("ObserverFont.jpg")),
				JAdvancePanel.TEXTURE);
		btnPnl.setFontImage(Toolkit.getDefaultToolkit().getImage(
				this.getClass().getResource("ObserverFont.jpg")),
				JAdvancePanel.TEXTURE);

		pack();
		JButton[] btns = new JButton[] { btn_SetUp, btn_Cancel, btn_OK };
		Tools.calculBtnSize(btns);
		this.getRootPane().setDefaultButton(btn_OK);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screen.width - this.getSize().width) / 2,
				(screen.height - this.getSize().height) / 2);
		setResizable(false);
	}

	void btn_Cancel_actionPerformed(ActionEvent e) {
		mModalResult = 2;
		dispose();
	}

	void btn_OK_actionPerformed(ActionEvent e) {
		mModalResult = 1;
		dispose();
	}
}
