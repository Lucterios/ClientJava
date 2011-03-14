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

package org.lucterios.client.setting;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.lucterios.client.application.ApplicationDescription;
import org.lucterios.engine.resources.Resources;
import org.lucterios.graphic.WebLabel;

public class AboutBox extends JDialog implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JEditorPane mTitleLbl;
	JEditorPane mVersionLbl;
	JEditorPane mCopyRigthLbl;
	JLabel mConfigMore;
	JLabel mImageLogo;

	private ApplicationDescription mDescription;

	private GridBagConstraints getConstraints(int x, int y, Insets aInset,
			int aGridwidth, int aGridheight) {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridwidth = aGridwidth;
		gridBagConstraints.gridheight = aGridheight;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		if (aInset != null)
			gridBagConstraints.insets = aInset;
		return gridBagConstraints;
	}

	public AboutBox(JFrame aOwner) throws HeadlessException {
		super(aOwner, true);
		setTitle("A propos");
		getContentPane().setLayout(new GridBagLayout());

		mImageLogo = new JLabel();
		getContentPane().add(mImageLogo,
				getConstraints(0, 0, new Insets(5, 5, 5, 5), 1, 2));

		mTitleLbl = new JEditorPane();
		mTitleLbl.setBorder(BorderFactory.createEmptyBorder());
		mTitleLbl.setEditable(false);
		mTitleLbl.setFocusable(false);
		mTitleLbl.setContentType("text/html");
		mTitleLbl.setBackground(this.getBackground());
		getContentPane().add(mTitleLbl, getConstraints(1, 0, null, 1, 1));

		mVersionLbl = new JEditorPane();
		mVersionLbl.setBorder(BorderFactory.createEmptyBorder());
		mVersionLbl.setEditable(false);
		mVersionLbl.setFocusable(false);
		mVersionLbl.setContentType("text/html");
		mVersionLbl.setBackground(this.getBackground());
		getContentPane().add(mVersionLbl, getConstraints(1, 1, null, 1, 1));

		mCopyRigthLbl = new JEditorPane();
		mCopyRigthLbl.setBorder(BorderFactory.createEmptyBorder());
		mCopyRigthLbl.setEditable(false);
		mCopyRigthLbl.setFocusable(false);
		mCopyRigthLbl.setContentType("text/html");
		mCopyRigthLbl.setBackground(this.getBackground());
		getContentPane().add(mCopyRigthLbl,
				getConstraints(0, 2, new Insets(0, 10, 0, 0), 2, 1));

		JLabel lucterios_logo = new JLabel(new javax.swing.ImageIcon(
				Resources.class.getResource("LucteriosImage.gif")));
		getContentPane().add(lucterios_logo, getConstraints(0, 3, null, 2, 1));

		JEditorPane lucterios = new JEditorPane();
		lucterios.setBorder(BorderFactory.createEmptyBorder());
		lucterios.setEditable(false);
		lucterios.setFocusable(false);
		lucterios.setBackground(this.getBackground());
		lucterios.setContentType("text/html");
		getContentPane().add(lucterios, getConstraints(0, 4, null, 2, 1));
		lucterios
				.setText("<font size='-1'><center><i>Outil de gestion presonnalisé sous licence GPL</i></center></font>");

		WebLabel lucterios_web = new WebLabel("http://www.lucterios.org");
		lucterios_web.setBackground(this.getBackground());
		getContentPane().add(lucterios_web, getConstraints(0, 5, null, 2, 1));

		JButton supportBtn = new JButton();
		supportBtn.setText("Ecrire au support");
		supportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aEvent) {
				mDescription
						.sendSupport("demande de support",
								"Décrivez le plus précisément possible votre problème.<br>");
			}
		});
		getContentPane().add(supportBtn,
				getConstraints(0, 6, new Insets(5, 5, 10, 10), 2, 1));

		mConfigMore = new JLabel();
		mConfigMore.setText("...");
		mConfigMore.addMouseListener(this);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
		getContentPane().add(mConfigMore, gridBagConstraints);

		setResizable(false);
		setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
	}

	public void show(ApplicationDescription aDescription) {
		mDescription = aDescription;
		mTitleLbl.setText("<center><h1>" + mDescription.getTitle()
				+ "</h1></center>");
		mImageLogo.setIcon(mDescription.getLogoIcon());
		mVersionLbl.setText("<table width='100%'>"
				+ "<tr><td><center>Version</center></td><td><center>"
				+ mDescription.getApplisVersion() + "</center></td></tr>"
				+ "<tr><td colspan='2'><font size='-1'><center><i>"
				+ mDescription.getCopyRigth() + "</i></center></font></td><td>"
				+ "</table>");

		mCopyRigthLbl
				.setText("<HR SIZE='2' WIDTH='100%' ALIGN=center>"
						+ "<table width='100%'>"
						+ "<tr><td colspan='2'><font size='+1'><center>Utilise le cadre d'application <i>Lucterios</i></center></font></td><td>"
						+ "<tr><td><center>Serveur</td><td><center>"
						+ mDescription.getServerVersion()
						+ "</center></td></tr>"
						+ "<tr><td><center>Client JAVA</td><td><center>"
						+ Constants.Version() + "</center></td></tr>"
						+ "</table>");
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 2);
		setVisible(true);
	}

	public void mouseClicked(MouseEvent aEvent) {
		JDialog config = new JDialog(this, true);
		config.setTitle("Configuration");
		config.getContentPane().setLayout(new GridBagLayout());

		JEditorPane text = new JEditorPane();
		text.setEditable(false);
		text.setFocusable(false);
		text.setBackground(this.getBackground());
		text.setContentType("text/html");
		text.setText(mDescription.getHTML(null));

		config.getContentPane().add(text,
				getConstraints(0, 0, new Insets(5, 5, 5, 5), 1, 1));

		config.pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		config.setLocation((screen.width - config.getSize().width) / 2,
				(screen.height - config.getSize().height) / 2);
		config.setVisible(true);
	}

	public void mouseEntered(MouseEvent aEvent) {
	}

	public void mouseExited(MouseEvent aEvent) {
	}

	public void mousePressed(MouseEvent aEvent) {
	}

	public void mouseReleased(MouseEvent aEvent) {
	}

}
