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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.lucterios.engine.presentation.Observer.MapContext;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.Tools;
import org.lucterios.utils.graphic.LineNumberedPaper;
import org.lucterios.utils.graphic.PopupListener;

public class CmpMemo extends CmpAbstractEvent implements CaretListener {
	final static public String ENCODE = "ISO-8859-1";
	private static final long serialVersionUID = 1L;
	private static final String SUB_MENU_SPECIAL = "@MENU@";
	private LineNumberedPaper cmp_text;
	private PopupListener popupListener;
	private JMenu SubMenu;
	private javax.swing.JScrollPane scrl_txt;
	private boolean mEncode = false;

	public CmpMemo() {
		super();
		mFill = GridBagConstraints.BOTH;
		mWeightx = 1.0;
		mWeighty = 1.0;
		setFocusable(false);
	}

	public void requestFocus() {
		cmp_text.requestFocus();
	}

	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		cmp_text.setEnabled(aEnabled);
	}

	public MapContext getRequete(String aActionIdent) {
		MapContext tree_map = new MapContext();
		String out_text = cmp_text.getText().trim();
		if (mEncode)
			try {
				out_text = java.net.URLEncoder.encode(out_text.trim(), ENCODE);
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
		return mNeeded && (cmp_text.getText().trim().length() == 0);
	}

	public void forceFocus() {
		cmp_text.requestFocus();
	}

	protected void initComponent() {
		setLayout(new java.awt.BorderLayout());
		cmp_text = new LineNumberedPaper();
		cmp_text.setEditable(true);
		cmp_text.setText("");
		cmp_text.setFocusable(true);
		cmp_text.setName(getName());
		scrl_txt = new javax.swing.JScrollPane();
		scrl_txt.setName("scrl_cst");
		scrl_txt.setViewportView(cmp_text);
		scrl_txt.setFocusable(false);
		add(scrl_txt, java.awt.BorderLayout.CENTER);
		scrl_txt.setMinimumSize(new Dimension(HMin, VMin));
		scrl_txt.setMaximumSize(new Dimension(HMax, VMax));

		popupListener = new PopupListener();
		popupListener.setActions(cmp_text.getActions());
		popupListener.addEditionMenu(true);
		SubMenu = new JMenu();
		SubMenu.setText(" => ");
		popupListener.getPopup().add(SubMenu);
		cmp_text.getDocument().addUndoableEditListener(popupListener.getUndo());
		cmp_text.addMouseListener(popupListener);
		cmp_text.addCaretListener(this);
	}

	protected JMenu getSubMenu(JMenu aMenu, String aName) {
		JMenu sub_menu = null;
		for (int index = 0; (sub_menu == null)
				&& (index < aMenu.getMenuComponentCount()); index++)
			if (JMenu.class.isInstance(aMenu.getMenuComponent(index))) {
				JMenu current_menu = (JMenu) aMenu.getMenuComponent(index);
				if (current_menu.getText().equals(aName))
					sub_menu = current_menu;
			}
		if (sub_menu == null) {
			sub_menu = new JMenu();
			sub_menu.setText(aName);
			aMenu.add(sub_menu);
		}
		return sub_menu;
	}

	protected void fillSubMenu(SimpleParsing[] aSubMenu) {
		SubMenu.removeAll();
		for (int index = 0; index < aSubMenu.length; index++) {
			String type = aSubMenu[index].getCDataOfFirstTag("TYPE");
			try {
				String name = java.net.URLDecoder.decode(aSubMenu[index]
						.getCDataOfFirstTag("NAME"), ENCODE);
				String value = java.net.URLDecoder.decode(aSubMenu[index]
						.getCDataOfFirstTag("VALUE"), ENCODE);
				JMenu current = getSubMenu(SubMenu, type);
				if ("-".equals( name ))
					current.addSeparator();
				else {
					JMenuItem variable_item = new JMenuItem();
					variable_item.setText(name);
					variable_item.setActionCommand(SUB_MENU_SPECIAL + value);
					variable_item.addActionListener(this);
					current.add(variable_item);
				}
			} catch (java.io.UnsupportedEncodingException e) {
			}
		}
	}

	protected void refreshComponent() throws LucteriosException {
		super.refreshComponent();
		cmp_text.getDocument().removeUndoableEditListener(
				popupListener.getUndo());
		String in_text = getXmlItem().getText().trim();
		mEncode = (getXmlItem().getAttributInt("Encode", 0) != 0);
		if (mEncode)
			try {
				in_text = java.net.URLDecoder.decode(in_text.trim(), ENCODE);
			} catch (UnsupportedEncodingException e) {
				in_text = "";
				e.printStackTrace();
			}
		else
			in_text = Tools.replace(in_text, "{[newline]}", "\n");
		cmp_text.setText(in_text);
		cmp_text.setStringSize(getXmlItem().getAttributInt("stringSize", 0));
		cmp_text.setFirstLine(getXmlItem().getAttributInt("FirstLine", -1));
		Dimension dim = cmp_text.getPreferredSize();
		scrl_txt.setSize(dim.width, Math.max(500, dim.height * 2));
		cmp_text.addFocusListener(this);
		cmp_text.getDocument().addUndoableEditListener(popupListener.getUndo());
		popupListener.resetUndoManager();
		fillSubMenu(getXmlItem().getSubTag("SUBMENU"));
		SubMenu.setVisible(SubMenu.getMenuComponentCount() > 0);
		Tools.postOrderGC();
	}

	public void actionPerformed(ActionEvent event) {
		String action_name = "";
		if (event != null)
			action_name = event.getActionCommand();
		if (action_name.startsWith(SUB_MENU_SPECIAL)) {
			String special_to_add = action_name.substring(SUB_MENU_SPECIAL
					.length());
			StyledDocument style = (StyledDocument) cmp_text.getDocument();
			try {
				if (mDot != mMark)
					style.remove(Math.min(mDot, mMark), Math.abs(mMark - mDot));
				style.insertString(Math.min(mDot, mMark), special_to_add, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else
			super.actionPerformed(event);
	}

	int mDot = 0;
	int mMark = 0;

	public void caretUpdate(CaretEvent e) {
		mDot = e.getDot();
		mMark = e.getMark();
	}

	protected boolean hasChanged() {
		return !cmp_text.getText().equals(getXmlItem().getText().trim());
	}

}
