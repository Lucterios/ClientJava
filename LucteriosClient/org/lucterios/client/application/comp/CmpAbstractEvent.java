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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import org.lucterios.client.application.ActionImpl;
import org.lucterios.engine.application.Action;
import org.lucterios.engine.application.Action.ActionList;
import org.lucterios.engine.presentation.Singletons;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.graphic.ExceptionDlg;

public abstract class CmpAbstractEvent extends Cmponent implements
		FocusListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Action mEventAction = null;

	public void fillActions(ActionList atns) {
		if (mEventAction != null)
			atns.add(mEventAction);
	}

	protected void refreshComponent() throws LucteriosException {
		focuslosted = false;
		SimpleParsing[] xml_items;
		SimpleParsing xml_item;
		mEventAction = null;

		SimpleParsing xml_actions = getXmlItem().getFirstSubTag("ACTIONS");
		if (xml_actions != null) {
			xml_items = xml_actions.getSubTag("ACTION");
			if (xml_items != null) {
				for (int action_idx = 0; (mEventAction == null)
						&& (action_idx < xml_items.length); action_idx++) {
					xml_item = xml_items[action_idx];
					mEventAction = new ActionImpl();
					mEventAction.initialize(getObsCustom(), Singletons
							.Factory(), xml_item);
					mEventAction.setCheckNull(false);
				}
			}
		}
	}

	protected abstract boolean hasChanged();

	public void focusGained(FocusEvent aEvent) {
		getObsCustom().setNameComponentFocused(getName());
	}

	private boolean focuslosted = false;

	public void focusLost(FocusEvent aEvent) {
		try {
			runJavaScript();
		} catch (LucteriosException e) {
			ExceptionDlg.throwException(e);
		}
		if (mEnabled && (mEventAction != null) && hasChanged() && !focuslosted) {
			focuslosted = true;
			aEvent.getComponent().removeFocusListener(this);
			Cmponent new_Cmponent_focused = getParentOfControle(aEvent
					.getOppositeComponent());
			if (new_Cmponent_focused != null) {
				getObsCustom().setNameComponentFocused(
						new_Cmponent_focused.getName());
				System.out.printf("edit:New focus Component(edit):%s:%s\n",
						new Object[] {
								new_Cmponent_focused.getClass().getName(),
								new_Cmponent_focused.getName() });
			} else
				System.out.print("edit:No focus Component(edit)\n");
			mEventAction.actionPerformed();
		}
	}

	boolean mActionPerforming = false;

	public void actionPerformed(ActionEvent aEvent) {
		if (!mActionPerforming) {
			mActionPerforming = true;
			try {
				try {
					runJavaScript();
				} catch (LucteriosException e) {
					ExceptionDlg.throwException(e);
				}
				if (mEnabled && (mEventAction != null) && hasChanged()
						&& !focuslosted) {
					focuslosted = true;
					Component comp = (Component) aEvent.getSource();
					comp.removeFocusListener(this);
					Cmponent new_Cmponent_focused = getParentOfControle(comp);
					if (new_Cmponent_focused != null) {
						getObsCustom().setNameComponentFocused(
								new_Cmponent_focused.getName());
						System.out.printf(
								"edit:New focus Component(edit):%s:%s\n",
								new Object[] {
										new_Cmponent_focused.getClass()
												.getName(),
										new_Cmponent_focused.getName() });
					} else
						System.out.print("edit:No focus Component(edit)\n");
					mEventAction.actionPerformed();
				}
			} finally {
				mActionPerforming = false;
			}
		}
	}

}
