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

package org.lucterios.client.presentation;

import java.util.Map;
import java.util.TreeMap;

import org.lucterios.client.application.Action;
import org.lucterios.client.application.ActionConstantes;
import org.lucterios.client.application.ActionImpl;
import org.lucterios.client.utils.Dialog;
import org.lucterios.client.utils.Form;
import org.lucterios.utils.LucteriosException;
import org.lucterios.utils.SimpleParsing;
import org.lucterios.utils.graphic.ExceptionDlg;

public abstract class ObserverAbstract implements Observer {
	protected Action mCloseAction = null;

	public ObserverAbstract() {
		super();
	}

	protected Observer mParent = null;

	public void setParent(Observer aParent) {
		mParent = aParent;
	}

	public Observer getParent() {
		return mParent;
	}

	protected String mExtension = "";
	protected String mAction = "";

	public void setSource(String aExtension, String aAction) {
		mExtension = aExtension;
		mAction = aAction;
	}

	public String getSourceExtension() {
		return mExtension;
	}

	public String getSourceAction() {
		return mAction;
	}

	protected SimpleParsing mContent;

	public void setContent(SimpleParsing aContent) {
		mContent = aContent;
		SimpleParsing xml_item_close = mContent.getFirstSubTag("CLOSE_ACTION");
		if (xml_item_close != null) {
			SimpleParsing[] xml_items = xml_item_close.getSubTag("ACTION");
			if (xml_items != null) {
				for (int action_idx = 0; (mCloseAction == null)
						&& (action_idx < xml_items.length); action_idx++) {
					SimpleParsing xml_item = xml_items[action_idx];
					mCloseAction = new ActionImpl();
					mCloseAction.initialize(this, Singletons.Factory(),
							xml_item);
					mCloseAction.setCheckNull(false);
					mCloseAction.setClose(true);
				}
			}
		}
	}

	public SimpleParsing getContent() {
		return mContent;
	}

	public String getContentText(){
		String content = mContent.getSubContent();
		int pos = content.indexOf("<CONTEXT/>");
		if (pos == -1)
			pos = content.indexOf("</CONTEXT>");
		if (pos != -1)
			content = content.substring(pos + 10);
		return content;
	}
	
	protected String mCaption = "";

	public void setCaption(String aCaption) {
		mCaption = aCaption;
		mTitle = aCaption;
	}

	protected Map mContext = null;

	public void setContext(Map aContext) {
		mContext = aContext;
	}

	public Map getContext() {
		return mContext;
	}

	protected String mTitle = "";

	public String getTitle() {
		return mTitle;
	}

	public void show(String aTitle) throws LucteriosException {
		if (!"".equals( aTitle ) && "".equals( mCaption ))
			mTitle = aTitle;
	}

	protected Form mGUIFrame = null;
	protected Dialog mGUIDialog = null;

	public Form getGUIFrame() {
		return mGUIFrame;
	}

	public Dialog getGUIDialog() {
		return mGUIDialog;
	}

	public void setActive(boolean aIsActive) {

	}

	public void eventForEnabled(boolean aBefore) {

	}

	boolean closed = false;

	public void close(boolean aMustRefreshParent) {
		if (!closed) {
			closed = true;
			if (mCloseAction != null)
				mCloseAction.actionPerformed(null);
			try {
				if (aMustRefreshParent && (getParent() != null))
					getParent().refresh();
			} catch (LucteriosException e) {
				ExceptionDlg.throwException(e);
			}
		}
	}

	public Map getParameters(String aActionId, int aSelect, boolean aCheckNull) throws LucteriosException {
		return new TreeMap();
	}

	public void refresh() throws LucteriosException {
		Action refresh = new ActionImpl();
		refresh.initialize(this, Singletons.Factory(), getTitle(),
				getSourceExtension(), getSourceAction());
		refresh.setFormType(ActionConstantes.FORM_REFRESH);
		refresh.setClose(false);
		refresh.setUsedContext(true);
		refresh.actionPerformed(null);
	}
}
