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

package org.lucterios.client.application;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionConstantes;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.graphic.Tools;

public class Button extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int OFFCET = 1;
	public Action mAction;

	public Button(Action aAction) {
		super();
		mAction = aAction;
		this.setText(mAction.getTitle());
		if (mAction.getMnemonic() != 0)
			this.setMnemonic(mAction.getMnemonic());
		if (mAction.getIcon() != null)
			this.setIcon(Tools.resizeIcon((ImageIcon)mAction.getIcon().getData(), 24, true));
		else
			this.setIcon(null);
		this.addActionListener((ActionListener) mAction);
	}

	public Action getCurrentAction() {
		return mAction;
	}

	public void setShowingMode(boolean aItemSeleted) {
		if (mAction.getSelect() == ActionConstantes.SELECT_NONE)
			this.setEnabled(aItemSeleted);
		else
			this.setEnabled(true);
	}

	static public int fillPanelByButton(JPanel aButtonPanel, Observer aOwner,
			ObserverFactory aFactory, SimpleParsing aXml,
			boolean aOrientationPaysage) {
		int nb_btn = 0;
		if (aXml != null) {
			GridBagConstraints gdbConstr_btn = new GridBagConstraints();
			aButtonPanel.removeAll();
			aButtonPanel.setLayout(new GridBagLayout());
			SimpleParsing[] xml_actions = aXml.getSubTag("ACTION");
			nb_btn = xml_actions.length;
			javax.swing.JButton[] btns = new javax.swing.JButton[nb_btn];
			for (int index = 0; index < nb_btn; index++) {
				Action act = new ActionImpl();
				act.initialize(aOwner, aFactory, xml_actions[index]);
				Button new_btn = new Button(act);
				if (aOrientationPaysage) {
					gdbConstr_btn.gridx = index + OFFCET;
					gdbConstr_btn.gridy = 0;
					gdbConstr_btn.insets = new Insets(10, 0, 10, 0);
				} else {
					gdbConstr_btn.gridx = 0;
					gdbConstr_btn.gridy = index + OFFCET;
					gdbConstr_btn.insets = new Insets(2, 5, 2, 5);
				}
				gdbConstr_btn.gridwidth = 1;
				gdbConstr_btn.gridheight = 1;
				gdbConstr_btn.fill = GridBagConstraints.NONE;
				gdbConstr_btn.anchor = GridBagConstraints.CENTER;
				gdbConstr_btn.weightx = 1.0;
				gdbConstr_btn.weighty = 0.0;
				btns[index] = new_btn;
				aButtonPanel.add(new_btn, gdbConstr_btn);
			}
			Tools.calculBtnSize(btns);

			if (!aOrientationPaysage) {
				gdbConstr_btn = new GridBagConstraints();
				gdbConstr_btn.gridx = 0;
				gdbConstr_btn.gridy = nb_btn + OFFCET;
				gdbConstr_btn.gridwidth = 1;
				gdbConstr_btn.gridheight = 1;
				gdbConstr_btn.fill = GridBagConstraints.NONE;
				gdbConstr_btn.anchor = GridBagConstraints.NORTH;
				gdbConstr_btn.weightx = 1.0;
				gdbConstr_btn.weighty = 1.0;
				gdbConstr_btn.insets = new Insets(10, 0, 10, 0);
				javax.swing.JLabel lbl = new javax.swing.JLabel();
				if (nb_btn == 0) {
					int wbtn = 0;
					int hbtn = 100;
					lbl.setPreferredSize(new Dimension(wbtn, hbtn));
					lbl.setMinimumSize(new Dimension(wbtn, hbtn));
				}
				aButtonPanel.add(lbl, gdbConstr_btn);
			}
			aButtonPanel.repaint();
		}
		return nb_btn;
	}
}
