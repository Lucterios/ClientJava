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

package org.lucterios.client.application.comp;

import org.lucterios.client.gui.GraphicTool;
import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.GUIButton;
import org.lucterios.ui.GUIActionListener;

public class CmpButton extends CmpAbstractEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GUIButton actbtn;
	private String m_clickName="";
	private String m_clickValue="";
	private boolean m_isMini=false;
	private boolean m_hasBeenClicked=false;

	public CmpButton() {
		super();
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		if (m_hasBeenClicked && (m_clickName.length()>0)) {
			tree_map.put(m_clickName, m_clickValue);
		}
		return tree_map;
	}

	protected void initComponent() {
		actbtn = null;
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		if (actbtn != null)
			actbtn.setEnabled(aEnabled);
	}

	protected void refreshComponent() {
		super.refreshComponent();
		m_clickName = getXmlItem().getAttribut("clickname", "");
		m_clickValue = getXmlItem().getAttribut("clickvalue", "");
		m_isMini = (getXmlItem().getAttributInt("isMini", 0)==1);
		actbtn = null;
		if (mEventAction != null) {
			if (m_isMini && mEventAction.getTitle().length()==0) {
				mParam.setPrefSizeX(26);
				mParam.setPrefSizeY(22);
			}
			actbtn = mPanel.createButton(mParam);
			GraphicTool.fillButton(actbtn,mEventAction);
			actbtn.addActionListener(new GUIActionListener() {
				public void actionPerformed() {
					m_hasBeenClicked=true;
				}
			});
		}
	}

	protected boolean hasChanged() {
		return false;
	}
}
