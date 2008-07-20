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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.lucterios.client.application.Action;
import org.lucterios.client.application.ActionConstantes;
import org.lucterios.client.application.Menu.FrameControle;
import org.lucterios.client.presentation.Observer;
import org.lucterios.client.presentation.ObserverFactory;
import org.lucterios.utils.SimpleParsing;

public class ActionLocal implements Action {

	static public FrameControle mFrameControle=null;

	private String mTitle = "";
	private char mMenomnic = 0;
	private ImageIcon mIcon = null;
	private ActionListener mAction = null;
	private KeyStroke mKey = null;

	public ActionLocal(String aTitle, char aMenomnic, ImageIcon aIcon,
			ActionListener aAction, KeyStroke aKey) {
		super();
		mTitle = aTitle;
		mMenomnic = aMenomnic;
		mIcon = aIcon;
		mAction = aAction;
		mKey = aKey;
	}

	public String getAction() {
		return "";
	}

	public boolean getCheckNull() {
		return false;
	}

	public boolean getClose() {
		return false;
	}

	public String getExtension() {
		return "";
	}

	public int getFormType() {
		return ActionConstantes.FORM_NOMODAL;
	}

	public String getID() {
		return "";
	}

	public ImageIcon getIcon() {
		return mIcon;
	}

	public String getIconName() {
		return mIcon.getDescription();
	}

	public KeyStroke getKeyStroke() {
		return mKey;
	}

	public char getMnemonic() {
		return mMenomnic;
	}

	public Observer getOwner() {
		return null;
	}

	public int getSelect() {
		return ActionConstantes.SELECT_NONE;
	}

	public String getTitle() {
		return mTitle;
	}

	public boolean getUsedContext() {
		return false;
	}

	public void initialize(Observer owner, ObserverFactory factory,
			SimpleParsing xml) {
	}

	public void initialize(Observer owner, ObserverFactory factory,
			String title, String extension, String action) {
	}

	public void runAction(Map param) {
		actionPerformed(null);
	}

	public void setCheckNull(boolean checkNull) {
	}

	public void setClose(boolean close) {
	}

	public void setFormType(int formType) {
	}

	public void setSelect(int select) {
	}

	public void setUsedContext(boolean usedContext) {
	}

	public void setKeyStroke(KeyStroke key) {
		mKey = key;
	}

	public void actionPerformed(ActionEvent e) {
		final ActionEvent event = e;
		if (mAction != null)
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					mFrameControle.setActive(false);
					try {
						mAction.actionPerformed(event);
					} finally {
						mFrameControle.setActive(true);
					}
				}
			});

	}

	public void addPropertyChangeListener(PropertyChangeListener arg0) {
	}

	public Object getValue(String arg0) {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public void putValue(String arg0, Object arg1) {
	}

	public void removePropertyChangeListener(PropertyChangeListener arg0) {
	}

	public void setEnabled(boolean arg0) {
	}

}
