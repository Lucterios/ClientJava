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

package org.lucterios.engine.setting;

import org.lucterios.engine.application.ApplicationDescription;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.engine.resources.Resources;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIFrame;
import org.lucterios.gui.GUIHyperText;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIButton.GUIActionListener;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class AboutBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUIDialog mDialog;
	private GUIHyperText m_TitleLbl;
	private GUIHyperText m_VersionLbl;
	private GUIHyperText m_CopyRigthLbl;
	private GUIHyperText m_ConfigMore;
	private GUILabel m_ImageLogo;
	
	private ApplicationDescription mDescription;


	public AboutBox(GUIFrame aOwner) {
		mDialog=Singletons.getWindowGenerator().newDialog(aOwner);
		mDialog.setTitle("A propos");
		mDialog.setResizable(false);
		
		m_ImageLogo=mDialog.getContainer().createLabel(new GUIParam(0, 0, 1, 2));
		m_TitleLbl = mDialog.getContainer().createHyperText(new GUIParam(1, 0));
		m_VersionLbl = mDialog.getContainer().createHyperText(new GUIParam(1, 1));
		m_CopyRigthLbl = mDialog.getContainer().createHyperText(new GUIParam(0, 2, 2, 1));
		GUILabel lucterios_logo = mDialog.getContainer().createLabel(new GUIParam(0, 3, 2, 1));
		lucterios_logo.setImage(Singletons.getWindowGenerator().CreateImage(Resources.class.getResource("LucteriosImage.gif")));	
		GUIHyperText lucterios = mDialog.getContainer().createHyperText(new GUIParam(0, 4, 2, 1));
		lucterios.setTextString("<font size='-1'><center><i>Outil de gestion presonnalisé sous licence GPL</i></center></font>");		

		GUIHyperText lucterios_web = mDialog.getContainer().createHyperText(new GUIParam(0, 5, 2, 1));		
		lucterios_web.setTextString("http://www.lucterios.org");
		lucterios_web.setHyperLink("http://www.lucterios.org");
				
		GUIButton supportBtn = mDialog.getContainer().createButton(new GUIParam(0, 6, 2, 1));
		supportBtn.setTextString("Ecrire au support");
		supportBtn.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				mDescription.sendSupport("demande de support",
					"Décrivez le plus précisément possible votre problème.<br>");
			}
		});
		

		m_ConfigMore = mDialog.getContainer().createHyperText(new GUIParam(2, 6, 2, 1, ReSizeMode.RSM_BOTH, FillMode.FM_NONE));	
		m_ConfigMore.setTextString("...");
		m_ConfigMore.addActionListener(new GUIActionListener() {		
			public void actionPerformed() {
				showConfigDialog();
			}
		});
	}	

	public void show(ApplicationDescription aDescription) {
		mDescription = aDescription;
		m_TitleLbl.setTextString("<center><h1>" + mDescription.getTitle()
				+ "</h1></center>");
		m_ImageLogo.setImage(mDescription.getLogoImage());
		m_VersionLbl.setTextString("<table width='100%'>"
				+ "<tr><td><center>Version</center></td><td><center>"
				+ mDescription.getApplisVersion() + "</center></td></tr>"
				+ "<tr><td colspan='2'><font size='-1'><center><i>"
				+ mDescription.getCopyRigth() + "</i></center></font></td><td>"
				+ "</table>");

		m_CopyRigthLbl.setTextString("<HR SIZE='2' WIDTH='100%' ALIGN=center>"
						+ "<table width='100%'>"
						+ "<tr><td colspan='2'><font size='+1'><center>Utilise le cadre d'application <i>Lucterios</i></center></font></td><td>"
						+ "<tr><td><center>Serveur</td><td><center>"
						+ mDescription.getServerVersion()
						+ "</center></td></tr>"
						+ "<tr><td><center>Client JAVA</td><td><center>"
						+ ApplicationDescription.gVersion + "</center></td></tr>"
						+ "</table>");
		mDialog.pack();
		mDialog.initialPosition();
		mDialog.setVisible(true);
	}

	private void showConfigDialog() {
		GUIDialog config = Singletons.getWindowGenerator().newDialog(mDialog,null);
		config.setTitle("Configuration");

		GUIHyperText text = config.getContainer().createHyperText(new GUIParam(0, 0));
		text.setTextString(mDescription.getHTML(null));

		config.pack();
		config.initialPosition();
		config.setVisible(true);
	}

}
