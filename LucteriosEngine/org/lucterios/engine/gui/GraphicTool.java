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

package org.lucterios.engine.gui;

import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.ActionImpl;
import org.lucterios.engine.presentation.Observer;
import org.lucterios.engine.presentation.ObserverFactory;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class GraphicTool {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int OFFCET = 1;
	
	public static void fillButton(GUIButton btn,Action aAction) {
		btn.setTextString(aAction.getTitle());
		if (aAction.getMnemonic() != 0)
			btn.setMnemonic(aAction.getMnemonic());
		btn.setObject(aAction);
		btn.setImage(aAction.getIcon().resizeIcon(24, true));
		btn.addActionListener(aAction);
	}

	static public GUIButton fillPanelByButton(GUIContainer aButtonPanel, Observer aOwner,
			ObserverFactory aFactory, SimpleParsing aXml,
			boolean aOrientationPaysage) {
		GUIButton default_btn = null;
		if (aXml != null) {
			aButtonPanel.removeAll();
			SimpleParsing[] xml_actions = aXml.getSubTag("ACTION");
			int nb_btn = xml_actions.length;
			GUIButton[] btns = new GUIButton[nb_btn];
			for (int index = 0; index < nb_btn; index++) {
				Action act = new ActionImpl();
				act.initialize(aOwner, aFactory, xml_actions[index]);
				GUIParam param=new GUIParam(0,0);
				param.setPad(5);
				param.setReSize(ReSizeMode.RSM_NONE);
				param.setFill(FillMode.FM_NONE);
				if (aOrientationPaysage) {
					param.setX(index + OFFCET);
				} else {
					param.setY(index + OFFCET);
				}				
				GUIButton new_btn = aButtonPanel.createButton(param); 
				fillButton(new_btn,act);
				btns[index] = new_btn;
				if (default_btn==null)
					default_btn=new_btn;
			}
			aButtonPanel.calculBtnSize(btns);

			if (!aOrientationPaysage) {
				aButtonPanel.createContainer(ContainerType.CT_NORMAL,new GUIParam(0,nb_btn + OFFCET));
			}
		}
		return default_btn;
	}
}
