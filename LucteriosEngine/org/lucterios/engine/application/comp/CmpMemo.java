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

package org.lucterios.engine.application.comp;

import java.io.UnsupportedEncodingException;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUIMenu;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.utils.GCTools;
import org.lucterios.utils.Tools;

public class CmpMemo extends CmpAbstractEvent implements MemoAction.Insert {
	private GUIMemo cmp_text;
	private GUIMenu SubMenu;
	private boolean mEncode = false;

	public CmpMemo() {
		super();
		mFill = FillMode.FM_BOTH;
		setWeightx(1.0);
		setWeighty(1.0);
	}
	
	public void insertText(String text){
		cmp_text.insertText(text);
	}

	public void requestFocus() {
		cmp_text.requestFocusGUI();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		cmp_text.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		String out_text = cmp_text.getValue().trim();
		if (mEncode)
			try {
				out_text = java.net.URLEncoder.encode(out_text.trim(), MemoAction.ENCODE);
			} catch (UnsupportedEncodingException e) {
				out_text = "";
				e.printStackTrace();
			}
		else
			out_text = Tools.replace(out_text, "\n", "{[newline]}");
		tree_map.put(getName(), out_text);
		return tree_map;
	}

	public boolean isEmpty() {
		return mNeeded && (cmp_text.getValue().trim().length() == 0);
	}

	public void forceFocus() {
		cmp_text.requestFocusGUI();
	}

	protected void initComponent() {
		cmp_text = mPanel.createMemo(mParam);

		SubMenu = cmp_text.getPopupMenu();
	}

	protected void refreshComponent() {
		super.refreshComponent();
		String in_text = getXmlItem().getText().trim();
		mEncode = (getXmlItem().getAttributeInt("Encode", 0) != 0);
		if (mEncode)
			try {
				in_text = java.net.URLDecoder.decode(in_text.trim(), MemoAction.ENCODE);
			} catch (UnsupportedEncodingException e) {
				in_text = "";
				e.printStackTrace();
			}
		else
			in_text = Tools.replace(in_text, "{[newline]}", "\n");
		cmp_text.setValue(in_text);
		cmp_text.setStringSize(getXmlItem().getAttributeInt("stringSize", 0));
		cmp_text.setFirstLine(getXmlItem().getAttributeInt("FirstLine", -1));
		cmp_text.addFocusListener(this);
		MemoAction.fillSubMenu(this, SubMenu, getXmlItem().getSubTag("SUBMENU"));
		SubMenu.setVisible(SubMenu.getMenuCount() > 0);
		GCTools.postOrderGC();
	}

	protected boolean hasChanged() {
		return !cmp_text.getValue().equals(getXmlItem().getText().trim());
	}

}
