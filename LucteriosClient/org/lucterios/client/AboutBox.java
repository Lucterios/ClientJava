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

package org.lucterios.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.lucterios.utils.graphic.WebLabel;

public class AboutBox extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JEditorPane mTitleLbl;
	JEditorPane mVersionLbl;
	JEditorPane mCopyRigth;
	JLabel mImageLogo;

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
		mTitleLbl.setEditable(false);
		mTitleLbl.setFocusable(false);
		mTitleLbl.setContentType("text/html");
		mTitleLbl.setBackground(this.getBackground());
		getContentPane().add(mTitleLbl, getConstraints(1, 0, null, 1, 1));

		mVersionLbl = new JEditorPane();
		mVersionLbl.setEditable(false);
		mVersionLbl.setFocusable(false);
		mVersionLbl.setContentType("text/html");
		mVersionLbl.setBackground(this.getBackground());
		getContentPane().add(mVersionLbl, getConstraints(1, 1, null, 1, 1));

		mCopyRigth = new JEditorPane();
		mCopyRigth.setEditable(false);
		mCopyRigth.setFocusable(false);
		mCopyRigth.setContentType("text/html");
		mCopyRigth.setBackground(this.getBackground());
		getContentPane().add(mCopyRigth,
				getConstraints(0, 2, new Insets(0, 10, 0, 0), 2, 1));

		JLabel lucterios_logo = new JLabel(new javax.swing.ImageIcon(getClass()
				.getResource("resources/LucteriosImage.gif")));
		getContentPane().add(lucterios_logo, getConstraints(0, 3, null, 2, 1));

		JEditorPane lucterios = new JEditorPane();
		lucterios.setEditable(false);
		lucterios.setFocusable(false);
		lucterios.setContentType("text/html");
		lucterios.setBackground(this.getBackground());
		getContentPane().add(lucterios, getConstraints(0, 4, null, 2, 1));
		lucterios
				.setText("<font size='-1'><center><i>Outil de gestion presonnalisé sous licence GPL</i></center></font>");

		WebLabel lucterios_web = new WebLabel("http://www.lucterios.org");
		lucterios_web.setBackground(this.getBackground());
		getContentPane().add(lucterios_web, getConstraints(0, 5, null, 2, 1));

		setResizable(false);
		setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
	}

	public void show(String aTitle, String aCopyRigth, Image aLogoIcon,
			String aVersion, String aServerVersion) {
		mTitleLbl.setText("<center><h1>" + aTitle + "</h1></center>");
		mImageLogo.setIcon(new javax.swing.ImageIcon(aLogoIcon));
		mVersionLbl.setText("<table width='100%'>"
				+ "<tr><td><center>Version</center></td><td><center>"
				+ aVersion + "</center></td></tr>"
				+ "<tr><td colspan='2'><font size='-1'><center><i>"
				+ aCopyRigth + "</i></center></font></td><td>" + "</table>");

		mCopyRigth
				.setText("<HR SIZE='2' WIDTH='100%' ALIGN=center>"
						+ "<table width='100%'>"
						+ "<tr><td colspan='2'><font size='+1'><center>Utilise le cadre d'application <i>Lucterios</i></center></font></td><td>"
						+ "<tr><td><center>Serveur</td><td><center>"
						+ aServerVersion + "</center></td></tr>"
						+ "<tr><td><center>Client JAVA</td><td><center>"
						+ Constants.Version() + "</center></td></tr>"
						+ "</table>");
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 2);
		setVisible(true);
	}

}
